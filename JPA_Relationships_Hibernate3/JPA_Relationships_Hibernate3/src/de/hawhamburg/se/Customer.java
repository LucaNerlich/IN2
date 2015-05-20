package de.hawhamburg.se;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//Todo: Add annotations!
@Entity
public class Customer {

    private long id;
    private String name;
    private String surname;
    private Address homeAddress;
    private Set<CreditCard> creditCards;
    private Set<Bank> banks;

    public Customer() {
        // empty
    }

    public Customer(final String surname, final String name) {
        this.name = name;
        this.surname = surname;
    }

    @Id
    @Column(name = "CUST_ID", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Column(name = "surname", length = 50)
    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    @OneToOne(optional=false)
    @JoinColumn(name="CUST_ID", unique=true, nullable=false, updatable=false)
    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(final Address address) {
        this.homeAddress = address;
    }

    @OneToMany(cascade = ALL, mappedBy = "customer")
    public Set<CreditCard> getCreditCards() {
        if (creditCards == null) {
            creditCards = new HashSet<CreditCard>();
        }
        return creditCards;
    }

    public void setCreditCards(final Set<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    @OneToMany(cascade=ALL, mappedBy="customer")
    public Set<Bank> getBanks() {
        if (banks == null) {
            banks = new HashSet<Bank>();
        }
        return banks;
    }

    public void setBanks(final Set<Bank> banks) {
        this.banks = banks;
    }

    @Override
    public String toString() {
        return "Customer[id=" + getId() + ", name=" + getName() + ", surname="
                + getSurname() + "]";
    }
}
