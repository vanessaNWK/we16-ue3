package at.ac.tuwien.big.we16.ue3.model;

import javax.persistence.*;

@Entity
@Table
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
