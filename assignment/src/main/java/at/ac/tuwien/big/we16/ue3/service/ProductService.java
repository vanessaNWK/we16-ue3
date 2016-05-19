package at.ac.tuwien.big.we16.ue3.service;

import at.ac.tuwien.big.we16.ue3.exception.ProductNotFoundException;
import at.ac.tuwien.big.we16.ue3.model.Bid;
import at.ac.tuwien.big.we16.ue3.model.Product;
import at.ac.tuwien.big.we16.ue3.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class ProductService {
    public Collection<Product> getAllProducts() {
        TypedQuery<Product> productTypedQuery = DBAccess.getManager().createQuery("FROM Product", Product.class);
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
                            productSold(product,user);
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

    public void productSold(Product product,User user) {
        ProductSoldRestResponse response= getUUIDFromJsonForSell(getRestPostReesponse("https://lectures.ecosio.com/b3a/api/v1/bids",getJsonFromObjectsForSell(product,user)));
        TwitterStatusMessage tw = null;
        tw = new TwitterStatusMessage(user.getFullName(), response.getId(), product.getAuctionEnd());
        String message = tw.getTwitterPublicationString();
        Twitter twitter = TwitterFactory.getSingleton();
        try {
            Status status = twitter.updateStatus(message);
        } catch (TwitterException e) {
            System.err.println("Beim Versuch auf Twitter zu posten ist ein Fehler aufgetreten. " + e.getMessage());
        }
    }

    public String getRestPostReesponse(String surl, String requestData) {

        String response="";
        HttpURLConnection httpCon=null;
        try {

            URL url = new URL(surl);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            httpCon.setRequestProperty("Content-Type", "application/json");

            httpCon.getOutputStream().write(requestData.getBytes());
            httpCon.getOutputStream().flush();

            if (httpCon.getResponseCode() != 200) {
                String error="REST Request failed: HTTP error code: " + httpCon.getResponseCode();
                System.err.println(error);
                //throw new RuntimeException(error);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((httpCon.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                response+=output;
            }
            System.out.println("REST Request successful");
            return response;

        } catch (IOException e) {
            String error="REST Request failed: Error in Serverconnection: "+e.getMessage();
            System.err.println(error);
            //throw new RuntimeException(error);
            return "";
        }
        finally {
            if(httpCon!=null)
                httpCon.disconnect();

        }


    }
    public String getJsonFromObjectsForSell(Product product, User user) {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DecimalFormat df2=new DecimalFormat("0.00");
        return  "{\"name\": \"" + user.getFullName() + "\", \"product\": \"" + product.getName() + "\", \"price\": \"" + df2.format(product.getHighestBid().getAmount() / 100) + "\", \"date\": \"" + df1.format(product.getAuctionEnd()) + "\" }";
    }
    public ProductSoldRestResponse getUUIDFromJsonForSell(String json) {
        if(!"".equals(json)) {
            Gson gson = new GsonBuilder().create();
            ProductSoldRestResponse response=gson.fromJson(json, ProductSoldRestResponse.class);
            System.out.println("Received UUID: "+response.getId());
            return response;
        }
        else return null;
    }
}
