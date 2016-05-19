package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.ProductNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.Bid;
import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.ProductType;
import at.ac.tuwien.big.we16.ue3.model.User;
import org.h2.expression.Variable;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ProductService {
    public Collection<Product> getAllProducts() {
        TypedQuery<Product> productTypedQuery =
                DBAccess.getManager().createQuery("FROM Product",
                        Product.class);
        return productTypedQuery.getResultList();
    }

    public Product getProductById(String id) throws ProductNotFoundException {

       //TODO: read from db
        Product p = DBAccess.getManager().find(Product.class, id);
        if(p == null) {
            throw new ProductNotFoundException();
        }
        return p;
    }

    //TODO: write changed users and products to db
    public Collection<Product> checkProductsForExpiration() {
        Collection<Product> newlyExpiredProducts = new ArrayList<>();
        for (Product product : this.getAllProducts()) {
            if (!product.hasExpired() && product.hasAuctionEnded()) {
                product.setExpired();
                newlyExpiredProducts.add(product);
                if (product.hasBids()) {
                    Bid highestBid = product.getHighestBid();
                    for (User user : product.getUsers()) {
                        user.decrementRunningAuctions();
                        if (highestBid.isBy(user)) {
                            user.incrementWonAuctionsCount();
                        }
                        else {
                            user.incrementLostAuctionsCount();
                        }
                        DBAccess.getManager().getTransaction().begin();
                        DBAccess.getManager().merge(user);
                        DBAccess.getManager().getTransaction().commit();
                    }
                }
                DBAccess.getManager().getTransaction().begin();
                DBAccess.getManager().merge(product);
                DBAccess.getManager().getTransaction().commit();
            }
        }
        return newlyExpiredProducts;
    }
}
