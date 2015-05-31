package de.hawhamburg.se;


import javax.persistence.*;

@Entity
public class Address {

    private long id;
    private String street;

    public Address() {
        // empty
    }

    public Address(final String street) {
        this.street = street;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESSGEN")
    @SequenceGenerator(name = "ADDRESSGEN", sequenceName = "ADDRESSSEQ")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "street", length = 50)
    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address[id=" + getId() + ", street=" + getStreet() + "]";
    }

}
