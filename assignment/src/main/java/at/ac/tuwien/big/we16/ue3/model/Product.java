package at.ac.tuwien.big.we16.ue3.model;

import at.ac.tuwien.big.we16.ue3.exception.InvalidBidException;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table
public class Product {
    @Id
    @GeneratedValue(generator="uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2", parameters = {@Parameter(name = "separator", value = "-")})
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="image")
    private String image;

    @Column(name="imageAlt")
    private String imageAlt;

    @Column(name="dbpedia")
    private String dbpedia;

    @Column(name="date")
    @Temporal(TemporalType.DATE)
    private Date auctionEnd;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name="year")
    private int year;

    @Column(name="producer")
    private String producer;

    @Column(name="expired")
    private boolean expired;

    @Transient
    private DateFormat df = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss,SSS");

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private List<RelatedProduct> relatedProducts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product")
    private List<Bid> bids = new ArrayList<>();

    public Bid getHighestBid() {
        Bid highest = null;
        int highestAmount = 0;
       for (Bid bid : this.bids) {
            if (bid.getAmount() > highestAmount) {
                highest = bid;
            }
        }
        return highest;
    }

    public boolean hasAuctionEnded() {
        return this.getAuctionEnd().before(new Date());
    }

    public void addBid(Bid bid) throws InvalidBidException {
        this.bids.add(bid);
    }

    public boolean hasExpired() {
        return expired;
    }

    public void setExpired() {
        this.expired = true;
    }

    public Set<User> getUsers() {
        Set<User> users = this.bids.stream().map(Bid::getUser).collect(Collectors.toSet());
        return users;
    }

    public boolean hasBids() {
        return this.bids.size() > 0;
    }

    public boolean isValidBidAmount(int amount) {
        return !this.hasBids() || this.getHighestBid().getAmount() < amount;
    }

    public boolean hasBidByUser(User user) {
        for (Bid bid : this.bids) {
            if (bid.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public List<RelatedProduct> getRelatedProducts() {
        return relatedProducts;
    }

    public Product setProducer(final String producer) {
        this.producer = producer;
        return this;
    }

    public Product setName(final String name) {
        this.name = name;
        return this;
    }

    public Product setImage(final String image) {
        this.image = image;
        return this;
    }

    public Product setImageAlt(final String imageAlt) {
        this.imageAlt = imageAlt;
        return this;
    }

    public Product setAuctionEnd(final Date auctionEnd) {
        this.auctionEnd = auctionEnd;
        return this;
    }

    public Product setYear(final int year) {
        this.year = year;
        return this;
    }
    public Product() {
    }

    public void addRelated(RelatedProduct rp) {
        this.relatedProducts.add(rp);
    }


    public Product(final String name, final String image, final String imageAlt, final String dbpedia,final Date auctionEnd, final ProductType type, final int year, final String producer, final boolean expired) {
        this.name = name;
        this.image = image;
        this.imageAlt = imageAlt;
        this.dbpedia = dbpedia;
        this.auctionEnd = auctionEnd;
        this.type = type;
        this.year = year;
        this.producer = producer;
        this.expired = expired;
    }
    public Product(final String name, final String image, final String imageAlt, final String dbpedia,final Date auctionEnd, final ProductType type, final int year, final String producer, final boolean expired, List<RelatedProduct> related, List<Bid> bids) {
        this.name = name;
        this.image = image;
        this.imageAlt = imageAlt;
        this.dbpedia = dbpedia;
        this.auctionEnd = auctionEnd;
        this.type = type;
        this.year = year;
        this.producer = producer;
        this.expired = expired;
        this.relatedProducts = related;
        this.bids = bids;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public int getYear() {
        return year;
    }

    public String getProducer() {
        return producer;
    }

    public boolean isExpired() {
        return expired;
    }

    public String getDbpedia() {
        return dbpedia;
    }

    public void setDbpedia(final String dbpedia) {
        this.dbpedia = dbpedia;
    }

    public String getAuctionStringEnd() {
        return df.format(this.auctionEnd);
    }

}
