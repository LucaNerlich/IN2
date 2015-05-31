package de.hawhamburg.se;


import javax.persistence.*;

@Entity
public class CreditCard {

    private long id;
    private String number;
    private Customer holder;

    public CreditCard() {
        // empty
    }

    public CreditCard(final String number) {
        this.number = number;
    }

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

    public void setNumber(final String number) {
        this.number = number;
    }


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    public Customer getHolder() {
        return holder;
    }

    public void setHolder(final Customer holder) {
        this.holder = holder;
    }

    @Override
    public String toString() {
        return "CreditCard[id=" + getId() + ", number=" + getNumber() + "]";
    }

}
