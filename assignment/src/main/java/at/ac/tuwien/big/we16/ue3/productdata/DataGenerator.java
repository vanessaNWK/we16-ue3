package at.ac.tuwien.big.we16.ue3.productdata;

import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
import at.ac.tuwien.big.we.dbpedia.api.SelectQueryBuilder;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPedia;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPediaOWL;
import at.ac.tuwien.big.we16.ue3.model.*;
import at.ac.tuwien.big.we16.ue3.service.DBAccess;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class DataGenerator {
    private static int call = 0;
    private DateFormat df = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss,SSS");

    private List<Product> buecher = new LinkedList<>();

    private List<Product> musics = new LinkedList<>();

    private List<Product> movies = new LinkedList<>();


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
            DBAccess.getManager().getTransaction().begin();
            User computer = new User("*", "Computer", "User", "computer.user@gmx.at", "Test", new Date(), 0, 0, 0, 0);
            User me = new User("Herr", "Max", "Mustermann", "vanessa.kos@gmx.at", "melody", new Date(), 150000, 0, 0, 0);
            DBAccess.getManager().persist(computer);
            DBAccess.getManager().persist(me);
            DBAccess.getManager().getTransaction().commit();
    }

    private void generateProductData() {
        // TODO load products via JSONDataLoader and write them to the database
        DBAccess.getManager().getTransaction().begin();
        for(JSONDataLoader.Book book : JSONDataLoader.getBooks()) {
            Product p = new Product();
            p.setName(book.getTitle());
            p.setImage(book.getImg());
            p.setImageAlt(book.getImageAlt());
            p.setDbpedia(book.getDbpedia());
            try {
                p.setAuctionEnd(df.parse(book.getAuctionEnd()));
            } catch (ParseException e) {
                System.err.println("AuctionEnd for Product with Name " + p.getName() + " could not be parsed");
            }
            p.setProducer(book.getAuthor());
            p.setYear(new Integer(book.getYear()));
            p.setType(ProductType.BOOK);
            DBAccess.getManager().persist(p);
            buecher.add(p);
            }
        for(JSONDataLoader.Movie movie : JSONDataLoader.getFilms()) {
            Product p = new Product();
            p.setName(movie.getTitle());
            p.setImage(movie.getImg());
            p.setProducer(movie.getDirector());
            p.setYear(new Integer(movie.getYear()));
            p.setImageAlt(movie.getImageAlt());
            p.setDbpedia(movie.getDbpedia());
            try {
                p.setAuctionEnd(df.parse(movie.getAuctionEnd()));
            } catch (ParseException e) {
                System.err.println("AuctionEnd for Product with Name " + p.getName() + " could not be parsed");
            }
            p.setType(ProductType.FILM);
            DBAccess.getManager().persist(p);
            movies.add(p);
        }
        for(JSONDataLoader.Music music : JSONDataLoader.getMusic()) {
            Product p = new Product();
            p.setName(music.getAlbum_name());
            p.setImage(music.getImg());
            p.setProducer(music.getArtist());
            p.setYear(new Integer(music.getYear()));
            p.setType(ProductType.ALBUM);
            p.setDbpedia(music.getDbpedia());
            p.setImageAlt(music.getImageAlt());
            try {
                p.setAuctionEnd(df.parse(music.getAuctionEnd()));
            } catch (ParseException e) {
                System.err.println("AuctionEnd for Product with Name " + p.getName() + " could not be parsed");
            }
            DBAccess.getManager().persist(p);
            musics.add(p);
        }
        DBAccess.getManager().getTransaction().commit();
    }

    private void insertRelatedProducts() {
        // TODO load related products from dbpedia and write them to the database
        if (!DBPediaService.isAvailable())
            return;
        for (Product b : buecher) {
            String autor = b.getProducer();
            autor = autor.replaceAll(" ", "_");
            Resource allezumautor = DBPediaService.loadStatements(DBPedia.createResource(autor));

            SelectQueryBuilder bookQuery = DBPediaService.createQueryBuilder()
                    .setLimit(5)// at most five statements
                    .addWhereClause(RDF.type, DBPediaOWL.Book)
                    .addPredicateExistsClause(FOAF.name)
                    .addWhereClause(DBPediaOWL.author, allezumautor)
                    .addFilterClause(RDFS.label, Locale.GERMAN);

            Model buecher = DBPediaService.loadStatements(bookQuery.toQueryString());

            List<String> buechernameDE = DBPediaService.getResourceNames(buecher, Locale.GERMAN);

            DBAccess.getManager().getTransaction().begin();
            for (String name : buechernameDE) {
                RelatedProduct neu = new RelatedProduct();
                neu.setName(name);
                neu.setProduct(b);
                DBAccess.getManager().persist(neu);
                b.addRelated(neu);
                DBAccess.getManager().merge(b);
            }
            DBAccess.getManager().getTransaction().commit();
        }

        for (Product b : movies) {
            String id = b.getId();
            String director = b.getProducer();
            director = director.replaceAll(" ", "_");
            Resource directors = DBPediaService.loadStatements(DBPedia.createResource(director));

            SelectQueryBuilder movieQuery = DBPediaService.createQueryBuilder()
                    .setLimit(5)// at most five statements
                    .addWhereClause(RDF.type, DBPediaOWL.Film)
                    .addPredicateExistsClause(FOAF.name)
                    .addWhereClause(DBPediaOWL.director, directors)
                    .addFilterClause(RDFS.label, Locale.GERMAN);
            Model movies = DBPediaService.loadStatements(movieQuery.toQueryString());

            List<String> movienamenDE = DBPediaService.getResourceNames(movies, Locale.GERMAN);

            DBAccess.getManager().getTransaction().begin();
            for (String name : movienamenDE) {
                RelatedProduct neu = new RelatedProduct();
                neu.setName(name);
                neu.setProduct(b);
                DBAccess.getManager().persist(neu);
                b.addRelated(neu);
                DBAccess.getManager().merge(b);
            }
            DBAccess.getManager().getTransaction().commit();
        }

        for (Product b : musics) {
            String id = b.getId();
            String artist = b.getProducer();
            artist = artist.replaceAll(" ", "_");
            Resource artists = DBPediaService.loadStatements(DBPedia.createResource(artist));

            SelectQueryBuilder musicQuery = DBPediaService.createQueryBuilder()
                    .setLimit(5)// at most five statements
                    .addWhereClause(RDF.type, DBPediaOWL.MusicalWork)
                    .addPredicateExistsClause(FOAF.name)
                    .addWhereClause(DBPediaOWL.artist, artists)
                    .addFilterClause(RDFS.label, Locale.GERMAN);

            Model cds = DBPediaService.loadStatements(musicQuery.toQueryString());

            List<String> CDDE = DBPediaService.getResourceNames(cds, Locale.GERMAN);

            DBAccess.getManager().getTransaction().begin();
            for (String name : CDDE) {
                RelatedProduct neu = new RelatedProduct();
                neu.setName(name);
                neu.setProduct(b);
                DBAccess.getManager().persist(neu);
                b.addRelated(neu);
                DBAccess.getManager().merge(b);

            }
            DBAccess.getManager().getTransaction().commit();
        }
    }
}
