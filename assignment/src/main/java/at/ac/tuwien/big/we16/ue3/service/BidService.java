package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.InvalidBidException;
import at.ac.tuwien.big.we16.ue3.exception.UserNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.Bid;
import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.User;
import org.omg.CORBA.DynAnyPackage.Invalid;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.math.BigDecimal;

public class BidService {

    public void makeBid(User user, Product product, int centAmount) throws InvalidBidException, UserNotFoundException {
        if (product.hasAuctionEnded() || !product.isValidBidAmount(centAmount)) {
            throw new InvalidBidException();
        }
        if(!user.getEmail().equals("computer.user@gmx.at")) {
            if(!user.hasSufficientBalance(centAmount)) {
                throw new InvalidBidException();
            }
        }

        // possible cases:
        // * product has no bids
        //   -> decrease balance by total, increment running
        // * product's highest bid is by the user
        //   -> decrease balance by diff, don't increment running
        // * some other bid on the product is by the user
        //   -> decrease balance by total, don't increment running, reimburse the current highest bidder
        // * product has bids, but none by the user
        //   -> decrease balance by total, increment running, reimburse the current highest bidder


        int decreaseAmount = centAmount;
        User highestBidder = null;

        if (product.hasBids()) {
            if (product.getHighestBid().isBy(user)) {
                // The given user already is the highest bidder, so we only substract the difference.
                decreaseAmount = centAmount - product.getHighestBid().getAmount();
            }
            else {
                // TODO reimburse current highest bidder
                //computeruser beachten
                highestBidder = product.getHighestBid().getUser();
                if(!highestBidder.getEmail().equals("computer.user@gmx.at")) {
                    highestBidder.increaseBalance(product.getHighestBid().getAmount());
                    ServiceFactory.getNotifierService().notifyReimbursement(highestBidder);
                }
            }
        }

        if (!product.hasBidByUser(user)) {
            user.incrementRunningAuctions();
        }

        user.decreaseBalance(decreaseAmount);
        Bid bid = new Bid(centAmount, user);
        bid.setProduct(product);
        product.addBid(bid);
        //TODO: write to db
        DBAccess.getManager().getTransaction().begin();
        DBAccess.getManager().merge(user);
        DBAccess.getManager().persist(bid);
        DBAccess.getManager().merge(product);
        DBAccess.getManager().getTransaction().commit();

        ServiceFactory.getNotifierService().notifyAllAboutBid(bid);
    }

    public void makeBid(User user, Product product, BigDecimal amount) throws InvalidBidException, UserNotFoundException {
        try {
            int centAmount = amount.movePointRight(2).intValueExact();
            this.makeBid(user, product, centAmount);
        } catch (ArithmeticException e) {
            throw new InvalidBidException();
        }
    }
}
