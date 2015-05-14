package de.hawhamburg.se;

import java.util.HashSet;
import java.util.Set;

//Todo: Add annotations!
public class Customer {

	private long id;
	private String name;
	private String surname;
	private Address homeAddress;
	private Set<CreditCard> creditCards;
	private Set<Bank> banks;

	public Customer() {
		// empty
	}

	public Customer(final String surname, final String name) {
		this.name = name;
		this.surname = surname;
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(final Address address) {
		this.homeAddress = address;
	}
	
	public Set<CreditCard> getCreditCards() {
		if (creditCards == null) {
			creditCards = new HashSet<CreditCard>();
		}
		return creditCards;
	}

	public void setCreditCards(final Set<CreditCard> creditCards) {
		this.creditCards = creditCards;
	}
	public Set<Bank> getBanks() {
		if (banks == null) {
			banks = new HashSet<Bank>();
		}
		return banks;
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
