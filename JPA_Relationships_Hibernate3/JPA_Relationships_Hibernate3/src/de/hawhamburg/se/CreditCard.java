package de.hawhamburg.se;

//Todo: Add annotations!
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
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}
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
