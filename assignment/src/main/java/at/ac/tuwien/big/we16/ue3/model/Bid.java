package at.ac.tuwien.big.we16.ue3.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Bid {

    @Id
    @GeneratedValue(generator="uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2", parameters = {@org.hibernate.annotations.Parameter(name = "separator", value = "-")})
    private String id;

    @Column(name="amount")
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,
               cascade = CascadeType.ALL)
    private Product product;

    public Bid() {}

    public Bid(int centAmount, User user) {
        amount = centAmount;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public float getConvertedAmount() {
        float convertedAmount = (float)this.amount;
        return convertedAmount / 100;
    }

    public User getUser() {
        return user;
    }

    public boolean isBy(User user) {
        return this.user.equals(user);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
