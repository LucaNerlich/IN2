package de.hawhamburg.se;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//http://stackoverflow.com/questions/18092103/typesafe-named-native-query-in-hibernate
@Entity
@NamedQueries({
        //5.1
        @NamedQuery(name = "selectCustomersWithCardType",
                query = "Select c FROM Customer c JOIN c.creditCards cc WHERE cc.type = :ccType ORDER BY c.id"),
        //5.2
        @NamedQuery(name = "selectCustomersWithBankNumber",
                query = "Select c FROM Customer c JOIN c.creditCards cc WHERE cc.holder =  c.id ORDER BY c.id"),
        //5.3
        @NamedQuery(name = "selectCustomerOffices",
                query = "SELECT NEW de.hawhamburg.se.CustomerWithBankOfficeAddress(c.name, o.street)"
                        + "from Customer c join c.banks b join b.offices o where c.homeAddress.postcode = o.postcode"),
        //5.4
        @NamedQuery(name = "selectCustomerWithAllCards",
                query = "FROM Card where holder.name = :name")
})
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
        this.surname = surname;
        this.name = name;
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "home_address_id", unique = true, nullable = false, updatable = false)
    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(final Address address) {
        this.homeAddress = address;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "holder", orphanRemoval = true)
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

    @ManyToMany(cascade = CascadeType.ALL)
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
