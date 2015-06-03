package de.hawhamburg.se;

import javax.persistence.*;
import java.util.Set;

/**
 * Base declarations for class Bank.
 *
 * @author Bernd Kahlbrandt
 */
@Entity
public class Bank {

    private long id;
    private String name;
    private Set<Address> offices;

    public Bank() {
        //
    }

    public Bank(final String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "BANKGEN")
    @SequenceGenerator(name = "BANKGEN",sequenceName = "BANKSEQ")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "name", length = 50)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @OneToMany
    @JoinTable(
            name="office_address",
            joinColumns = @JoinColumn( name="bank_id"),
            inverseJoinColumns = @JoinColumn( name="address_id")
    )
    public Set<Address> getOffices() {
        return offices;
    }

    public void setOffices(Set<Address> offices) {
        this.offices = offices;
    }

    public void addOfficeByAddress(Address address){
        offices.add(address);
    }

    public void removeOffice(Address address){

        boolean officeremoved = offices.remove(address);

        System.out.println("Office removed: " + officeremoved);
    }

    @Override
    public String toString() {
        return "Bank[id=" + getId() + ", name=" + getName() + "]";
    }
}
