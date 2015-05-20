package de.hawhamburg.se;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

//Add Annotations!
//Add Optimistic Locking!
@Entity
public class Customer {
    private long id;
    private String name;
    private String surname;
    private Integer version = 1;

    public Customer() {
    }

    public Customer(final String surname, final String name) {
        this.name = name;
        this.surname = surname;
    }

    @Id
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

    @Version
    @Column(name="optlock")
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Customer[id=" + getId() + ", name=" + getName() + ", surname="
                + getSurname() + ", OPTLOCK=" + getVersion() + "]";
    }
}
