package de.hawhamburg.se;

//Todo: Add annotations!

public class Bank {

	private long id;
	private String name;

	public Bank() {
		//
	}

	public Bank(final String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
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
