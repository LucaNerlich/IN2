package de.hawhamburg.se;

import javax.persistence.*;

/**
 * Base declarations for class CardIssuer.
 *
 * @author Bernd Kahlbrandt
 */
@Entity
public class CardIssuer {

    private long id;
    private String name;

    public CardIssuer() {
    }

    public CardIssuer(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARDISSUERGEN")
    @SequenceGenerator(name = "CARDISSUERGEN", sequenceName = "CARDISSUERSEQ")
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

    public void setName(String name) {
        this.name = name;
    }
}
