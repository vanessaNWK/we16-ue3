package at.ac.tuwien.big.we16.ue3.model;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(generator="uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2", parameters = {@Parameter(name = "separator", value = "-")})
    private String id;

    @Column(name="salutation")
    private String salutation;

    @Column(name="firstname")
    private String firstname;

    @Column(name="lastname")
    private String lastname;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name="balance")
    private int balance;

    @Column(name="runningAuctionsCount")
    private int runningAuctionsCount;

    @Column(name="wonAuctionsCount")
    private int wonAuctionsCount;

    @Column(name="lostAuctionsCount")
    private int lostAuctionsCount;

    public User(final String salutation, final String firstname, final String lastname, final String email, final String password, final Date date, final int balance, final int runningAuctionsCount, final int wonAuctionsCount, final int lostAuctionsCount) {
        this.salutation = salutation;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.date = date;
        this.balance = balance;
        this.runningAuctionsCount = runningAuctionsCount;
        this.wonAuctionsCount = wonAuctionsCount;
        this.lostAuctionsCount = lostAuctionsCount;
    }

    public User() {}

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public float getConvertedBalance() {
        float convertedBalance = (float)this.balance;
        return convertedBalance / 100;
    }

    public void decreaseBalance(int amount) {
        this.balance -= amount;
    }

    public void increaseBalance(int amount) {
        this.balance += amount;
    }

    public int getRunningAuctionsCount() {
        return this.runningAuctionsCount;
    }

    public void incrementRunningAuctions() {
        this.runningAuctionsCount++;
    }

    public void decrementRunningAuctions() {
        this.runningAuctionsCount--;
    }

    public int getWonAuctionsCount() {
        return this.wonAuctionsCount;
    }

    public int getLostAuctionsCount() {
        return this.lostAuctionsCount;
    }

    public void incrementLostAuctionsCount() {
        this.lostAuctionsCount++;
    }

    public void incrementWonAuctionsCount() {
        this.wonAuctionsCount++;
    }

    public boolean hasSufficientBalance(int amount) {
        return this.balance >= amount;
    }

    public String getEmail() {
        return email;
    }

    public String getSalutation() {
        return salutation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
