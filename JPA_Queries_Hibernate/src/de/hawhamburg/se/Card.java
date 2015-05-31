package de.hawhamburg.se;

import javax.persistence.*;

/**
 * Base declarations for class CreditCard.
 *
 * @author Bernd Kahlbrandt
 */
@Entity
public class Card {

    private long id;
    private String number;
    private CardType type;
    private Customer holder;
    private CardIssuer issuer;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREDITCARDGEN")
    @SequenceGenerator(name = "CREDITCARDGEN", sequenceName = "CREDITCARDSEQ")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "ccnumber", length = 50)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "cardtype", length = 50)
    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    public Customer getHolder() {
        return holder;
    }

    public void setHolder(Customer holder) {
        this.holder = holder;
    }

    public CardIssuer getIssuer() {
        return issuer;
    }

    public void setIssuer(CardIssuer issuer) {
        this.issuer = issuer;
    }
}
