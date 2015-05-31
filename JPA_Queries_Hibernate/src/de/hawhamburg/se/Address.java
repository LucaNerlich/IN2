package de.hawhamburg.se;

import javax.persistence.*;

/**
 * Base declarations for class Address.
 *
 * @author Bernd Kahlbrandt
 */
@Entity
public class Address {

    private long id;
    private String postcode;
    private String city;
    private String street;

    public Address() {
        //
    }

    public Address(String postcode, String city, String street) {
        this.postcode = postcode;
        this.city = city;
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

    @Column(name = "postcode", length = 50)
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Column(name = "city", length = 50)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "street", length = 50)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
