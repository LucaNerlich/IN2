package de.hawhamburg.se;

import java.util.Set;

/**
 * Base declarations for class Customer.
 */
public class Customer {

	private long id;
	private String name;
	private String surname;
	private Address homeAddress;
	private Set<Card> creditCards;
	private Set<Bank> banks;


}
