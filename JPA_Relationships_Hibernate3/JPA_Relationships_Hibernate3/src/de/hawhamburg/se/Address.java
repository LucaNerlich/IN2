package de.hawhamburg.se;

//Todo: Add annotations!
public class Address {

	private long id;
	private String street;

	public Address() {
		// empty
	}

	public Address(final String street) {
		this.street = street;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
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
