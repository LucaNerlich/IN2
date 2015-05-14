package de.hawhamburg.se;

//Add Annotations!
//Add Optimistic Locking!
public class Customer {
	private long id;
	private String name;
	private String surname;

	public Customer() {}

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
	@Override
	public String toString() {
		return "Customer[id=" + getId() + ", name=" + getName() + ", surname="
				+ getSurname() + "]";
	}
}
