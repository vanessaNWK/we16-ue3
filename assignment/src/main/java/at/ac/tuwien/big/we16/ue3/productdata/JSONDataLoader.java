package at.ac.tuwien.big.we16.ue3.productdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JSONDataLoader {

    private static Product products;

    public static Music[] getMusic() {
        if (products == null)
            loadProducts();
        return products.getMusic();
    }

    public static Movie[] getFilms() {
        if (products == null)
            loadProducts();
        return products.getMovies();
    }

    public static Book[] getBooks() {
        if (products == null)
            loadProducts();
        return products.getBooks();
    }

    private static void loadProducts() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("products.json");
        Reader reader = new InputStreamReader(is);
        Gson gson = new GsonBuilder().create();
        products = gson.fromJson(reader, Product.class);
    }

    public class Product {
        private Music[] music;
        private Book[] books;
        private Movie[] movies;

        public Music[] getMusic() {
            return music;
        }

        public void setMusic(Music[] music) {
            this.music = music;
        }

        public Book[] getBooks() {
            return books;
        }

        public void setBooks(Book[] books) {
            this.books = books;
        }

        public Movie[] getMovies() {
            return movies;
        }

        public void setMovies(Movie[] movies) {
            this.movies = movies;
        }
    }

    public class Music {
        private String dbpedia;
        private String album_name;
        private String artist;
        private String year;
        private String img;
        private String auctionEnd;
        private String imageAlt;

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getAuctionEnd() {
            return auctionEnd;
        }

        public String getImageAlt() {
            return imageAlt;
        }

        public String getDbpedia() {
            return dbpedia;
        }

        public Music setDbpedia(final String dbpedia) {
            this.dbpedia = dbpedia;
            return this;
        }

        public Music setAuctionEnd(final String auctionEnd) {
            this.auctionEnd = auctionEnd;
            return this;
        }

        public Music setImageAlt(final String imageAlt) {
            this.imageAlt = imageAlt;
            return this;
        }
    }

    public class Book {
        private String dbpedia;
        private String title;
        private String author;
        private String year;
        private String img;
        private String auctionEnd;
        private String imageAlt;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Book setAuctionEnd(final String auctionEnd) {
            this.auctionEnd = auctionEnd;
            return this;
        }

        public Book setImageAlt(final String imageAlt) {
            this.imageAlt = imageAlt;
            return this;
        }

        public String getAuctionEnd() {
            return auctionEnd;
        }

        public String getImageAlt() {
            return imageAlt;
        }

        public String getDbpedia() {
            return dbpedia;
        }

        public Book setDbpedia(final String dbpedia) {
            this.dbpedia = dbpedia;
            return this;
        }
    }

    public class Movie {
        private String dbpedia;
        private String title;
        private String director;
        private String year;
        private String img;
        private String auctionEnd;
        private String imageAlt;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Movie setAuctionEnd(final String auctionEnd) {
            this.auctionEnd = auctionEnd;
            return this;
        }

        public Movie setImageAlt(final String imageAlt) {
            this.imageAlt = imageAlt;
            return this;
        }

        public String getAuctionEnd() {
            return auctionEnd;
        }

        public String getImageAlt() {
            return imageAlt;
        }

        public String getDbpedia() {
            return dbpedia;
        }

        public Movie setDbpedia(final String dbpedia) {
            this.dbpedia = dbpedia;
            return this;
        }
    }

}
