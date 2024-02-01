package Entities;

import java.io.Serializable;
import java.util.Arrays;

// TODO: Auto-generated Javadoc
/**
 * 
 */
public class PendingClientInfo implements Serializable{
	
	/** The User ID. */
	private String UserID;
	
	/** The id number of the user. */
	private String ID;
	
	/** The first name. */
	private String firstName;
	
	/** The Last name. */
	private String LastName;
	
	/** The email. */
	private String email;
	
	/** The phone. */
	private String phone;
	
	/** The credit phrases,credit card divide to 4 phrases . */
	private String[] creditPhrases;
	
	/** The cvv of the credit card. */
	private String CVV;
	
	/** The expiration month of the credit card. */
	private String expirationMonth;
	
	/** The expiration year  of the credit card. */
	private String expirationYear;
	
	/** The password of the user to enter the system. */
	private String password;
		
	/**
	 * Instantiates a new pending client info.
	 */
	public PendingClientInfo() {}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the user ID.
	 *
	 * @return the user ID
	 */
	public String getUserID() {
		return UserID;
	}

	/**
	 * Sets the user ID.
	 *
	 * @param userID the new user ID
	 */
	public void setUserID(String userID) {
		UserID = userID;
	}

	/**
	 * Gets the array credit phrases.
	 *
	 * @return the array credit phrases
	 */
	public String[] getArrayCreditPhrases() {
		return creditPhrases;
	}
	
	/**
	 * Gets the string credit phrases.
	 *
	 * @return the string credit phrases as 1 string
	 */
	public String getStringCreditPhrases() {
		return creditPhrases[0]+creditPhrases[1]+creditPhrases[2]+creditPhrases[3];
	}
	
	/**
	 * Sets the credit phrases.
	 *
	 * @param creditPhrases the new credit phrases
	 */
	public void setCreditPhrases(String[] creditPhrases) {
		this.creditPhrases = creditPhrases;
	}
	
	/**
	 * Gets the cvv.
	 *
	 * @return the cvv
	 */
	public String getCVV() {
		return CVV;
	}
	
	/**
	 * Sets the cvv.
	 *
	 * @param cVV the new cvv
	 */
	public void setCVV(String cVV) {
		CVV = cVV;
	}
	
	/**
	 * Gets the expiration month.
	 *
	 * @return the expiration month
	 */
	public String getExpirationMonth() {
		return expirationMonth;
	}
	
	/**
	 * Sets the expiration month.
	 *
	 * @param expirationMonth the new expiration month
	 */
	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}
	
	/**
	 * Gets the expiration year.
	 *
	 * @return the expiration year
	 */
	public String getExpirationYear() {
		return expirationYear;
	}
	
	/**
	 * Sets the expiration year.
	 *
	 * @param expirationYear the new expiration year
	 */
	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID() {
		return ID;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param iD the new id
	 */
	public void setID(String iD) {
		ID = iD;
	}
	
	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return LastName;
	}
	
	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the phone.
	 *
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * Sets the phone.
	 *
	 * @param phone the new phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "PendingClientInfo [UserID=" + UserID + ", ID=" + ID + ", firstName=" + firstName + ", LastName="
				+ LastName + ", email=" + email + ", phone=" + phone + ", creditPhrases="
				+ Arrays.toString(creditPhrases) + ", CVV=" + CVV + ", expirationMonth=" + expirationMonth
				+ ", expirationYear=" + expirationYear + ", password=" + password + "]";
	}

}
