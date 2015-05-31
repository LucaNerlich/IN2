package de.hawhamburg.se;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Base declarations for class Customer.
 */
@Entity
public class Customer {

    private long id;
    private String name;
    private String surname;
    private Address homeAddress;
    private Set<Card> creditCards;
    private Set<Bank> banks;

    public Customer() {
        // empty
    }

    public Customer(final String surname, final String name) {
        this.name = name;
        this.surname = surname;
        banks = new HashSet<>();
        creditCards = new HashSet<>();

    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMERGEN")
    @SequenceGenerator(name = "CUSTOMERGEN", sequenceName = "CUSTOMERSEQ")
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "home_address_id", unique = true, nullable = false, updatable = false)
    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(final Address address) {
        this.homeAddress = address;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "holder")
    public Set<Card> getCreditCards() {
        if (creditCards == null) {
            creditCards = new HashSet<Card>();
        }
        return creditCards;
    }

    public void setCreditCards(final Set<Card> creditCards) {
        this.creditCards = creditCards;
    }

    public void addCreditCard(Card creditCard) {
        if (creditCard != null) {
            creditCards.add(creditCard);
        }
    }

    @ManyToMany
    @JoinTable(name = "BANK_CUSTOMER",
            joinColumns = @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "BANK_ID", referencedColumnName = "ID"))
    public Set<Bank> getBanks() {
        if (banks == null) {
            banks = new HashSet<Bank>();
        }
        return banks;
    }

    public void addBankByString(String bankName) {
        if (!bankName.equals("")) {
            Bank newBank = new Bank(bankName);
            banks.add(newBank);
        } else {
            System.err.println("bank add failed");
        }

    }

    public void addBank(Bank bank) {
        if (bank != null) {
            banks.add(bank);
        } else {
            System.err.println("Cannot add null to banks");
        }
    }

    public void removeBank(Bank bank) {
        banks.remove(bank);
    }

    public void removeBankByString(String bankName) {
        for (Bank bank : banks) {
            if (bank.getName().equals(bankName)) {
                banks.remove(bank);
            }
        }
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
