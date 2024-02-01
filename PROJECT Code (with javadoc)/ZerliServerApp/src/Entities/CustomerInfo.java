package Entities;

import java.io.Serializable;

/**
 * The Class CustomerInfo.
 */
public class CustomerInfo implements Serializable{
	
	/** The customer's first name. */
	private String firstName;
	
	/** The customer's last name. */
	private String lastName;
	
	/** The customer's ID. */
	private String ID;
	
	/** The customer's email. */
	private String email;
	
	/** The customer's phone number. */
	private String phone;
	
	/** The customer's credit card number. */
	private String creditCard;
	
	/** The customer's cvv. */
	private String cvv;
	
	/** The expiration month of the customer's credit card. */
	private String expMonth;
	
	/** The expiration year of the customer's credit card. */
	private String expYear;
	
	/** The zerli coins the customer's has. */
	private String zerliCoins;
	
	/** Flag for new customers. */
	private boolean newCustomer;
	
	/**
	 * Instantiates a new customer info.
	 */
	public CustomerInfo() {}

	/**
	 * Gets the customer's first name.
	 *
	 * @return the customer's first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the customer's first name.
	 *
	 * @param firstName the new customer's first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the customer's last name.
	 *
	 * @return the customer's last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the customer's last name.
	 *
	 * @param lastName the new customer's last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the customer's ID.
	 *
	 * @return the customer's ID
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Sets the customer's ID.
	 *
	 * @param iD the new customer's ID
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * Gets the customer's email.
	 *
	 * @return the customer's email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the customer's email.
	 *
	 * @param email the new customer's email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the customer's phone number.
	 *
	 * @return the customer's phone number
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the customer's phone number.
	 *
	 * @param phone the new customer's phone number
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the customer's credit card.
	 *
	 * @return the customer's credit card
	 */
	public String getCreditCard() {
		return creditCard;
	}

	/**
	 * Sets the customer's credit card.
	 *
	 * @param creditCard the new customer's credit card
	 */
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	/**
	 * Gets the customer's cvv.
	 *
	 * @return the customer's cvv
	 */
	public String getCvv() {
		return cvv;
	}

	/**
	 * Sets the customer's cvv.
	 *
	 * @param cvv the new customer's cvv
	 */
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	/**
	 * Gets the expiration month of the customer's credit card.
	 *
	 * @return the expiration month of the customer's credit card
	 */
	public String getExpMonth() {
		return expMonth;
	}

	/**
	 * Sets the expiration month of the customer's credit card
	 *
	 * @param expMonth the new expiration month of the customer's credit card
	 */
	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	/**
	 * Gets expiration year of the customer's credit card
	 *
	 * @return the expiration year of the customer's credit card
	 */
	public String getExpYear() {
		return expYear;
	}

	/**
	 * Sets the expiration year of the customer's credit card
	 *
	 * @param expYear the new expiration year of the customer's credit card
	 */
	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

	/**
	 * Gets the customer's zerli coins.
	 *
	 * @return the customer's zerli coins
	 */
	public String getZerliCoins() {
		return zerliCoins;
	}

	/**
	 * Sets the customer's zerli coins.
	 *
	 * @param zerliCoins the new customer's zerli coins
	 */
	public void setZerliCoins(String zerliCoins) {
		this.zerliCoins = zerliCoins;
	}

	/**
	 * Checks if a customer is new.
	 *
	 * @return true, if a customer is new
	 */
	public boolean isNewCustomer() {
		return newCustomer;
	}

	/**
	 * Sets the new customer flag
	 *
	 * @param newCustomer the new customer flag
	 */
	public void setNewCustomer(boolean newCustomer) {
		this.newCustomer = newCustomer;
	}
	
}
