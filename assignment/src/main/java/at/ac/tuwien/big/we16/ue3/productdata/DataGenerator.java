package at.ac.tuwien.big.we16.ue3.productdata;

import at.ac.tuwien.big.we16.ue3.model.*;
import at.ac.tuwien.big.we16.ue3.service.DBAccess;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataGenerator {
    private static int call = 0;
    private DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    public void generateData() {
        call++;
        if(call <= 1) {
            generateUserData();
            generateProductData();
            insertRelatedProducts();
        }
    }

    private void generateUserData() {
        // TODO add the computer user to the database
        if(DBAccess.getManager().find(User.class, "vanessa.kos@gmx.at") == null) {
            DBAccess.getManager().getTransaction().begin();
            User computer = new User("*", "Computer", "User", "computer.user@gmx.at", "IchLi3b3Pr#8raMmi3r3n!", new Date(), 0, 0, 0, 0);
            User me = new User("Frau", "Vanessa", "Kos", "vanessa.kos@gmx.at", "melody", new Date(), 150000, 0, 0, 0);
            DBAccess.getManager().persist(computer);
            DBAccess.getManager().persist(me);
            DBAccess.getManager().getTransaction().commit();
        }
    }

    private void generateProductData() {
        System.err.println("generate Product Data");
        // TODO load products via JSONDataLoader and write them to the database
        DBAccess.getManager().getTransaction().begin();
        for(JSONDataLoader.Book book : JSONDataLoader.getBooks()) {
            Product p = new Product();
            p.setName(book.getTitle());
            p.setImage(book.getImg());
            p.setImageAlt(book.getImageAlt());
            try {
                p.setAuctionEnd(df.parse(book.getAuctionEnd()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            p.setProducer(book.getAuthor());
            p.setYear(new Integer(book.getYear()));
            p.setType(ProductType.BOOK);
            DBAccess.getManager().persist(p);
        }
        for(JSONDataLoader.Movie movie : JSONDataLoader.getFilms()) {
            Product p = new Product();
            p.setName(movie.getTitle());
            p.setImage(movie.getImg());
            p.setProducer(movie.getDirector());
            p.setYear(new Integer(movie.getYear()));
            p.setImageAlt(movie.getImageAlt());
            try {
                p.setAuctionEnd(df.parse(movie.getAuctionEnd()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            p.setType(ProductType.FILM);
            DBAccess.getManager().persist(p);
        }
        for(JSONDataLoader.Music music : JSONDataLoader.getMusic()) {
            Product p = new Product();
            p.setName(music.getAlbum_name());
            p.setImage(music.getImg());
            p.setProducer(music.getArtist());
            p.setYear(new Integer(music.getYear()));
            p.setType(ProductType.ALBUM);
            p.setImageAlt(music.getImageAlt());
            try {
                p.setAuctionEnd(df.parse(music.getAuctionEnd()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DBAccess.getManager().persist(p);
            DBAccess.getManager().getTransaction().commit();
        }
    }

    private void insertRelatedProducts() {
        // TODO load related products from dbpedia and write them to the database
    }
}
