package de.hawhamburg.se;


import javax.persistence.*;

//Todo: Add annotations!
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
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    @Column(name="number",length=50)
	public String getNumber() {
		return number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}


    @ManyToOne
    @JoinColumn(name="CUST_ID", nullable=false)
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
