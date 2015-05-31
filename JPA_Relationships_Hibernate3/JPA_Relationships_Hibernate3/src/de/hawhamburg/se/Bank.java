package de.hawhamburg.se;

import javax.persistence.*;

@Entity
public class Bank {

    private long id;
    private String name;

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

    @Override
    public String toString() {
        return "Bank[id=" + getId() + ", name=" + getName() + "]";
    }

}
