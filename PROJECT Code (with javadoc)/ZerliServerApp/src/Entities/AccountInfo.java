package Entities;

import java.io.Serializable;

import Utilities.GenericUtilties;


/**
 * The Class AccountInfo holds useful information about an account.
 */
public class AccountInfo implements Serializable{

	/** The user's ID in the system. */
	private String userID;
	
	/** The user's first name. */
	private String firstName;
	
	/** The user's last name. */
	private String lastName;
	
	/** The user's id. */
	private String ID;
	
	/** The user's role. */
	private Roles role;
	
	/** The user's access status. */
	private Access access;
	
	/**
	 * Instantiates a new account info.
	 */
	public AccountInfo() {}

	/**
	 * Instantiates a new account info.
	 *
	 * @param userID the user's ID in the system
	 * @param firstName the user's first name
	 * @param lastName the user's last name
	 * @param iD the user's ID
	 */
	public AccountInfo(String userID, String firstName, String lastName, String iD) {
		super();
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		ID = iD;
	}
	
	/**
	 * Gets the user's system ID.
	 *
	 * @return the user ID in the system
	 */
	public String getUserID() {
		return userID;
	}
	
	/**
	 * Sets the user's system ID.
	 *
	 * @param userID the new user's system ID
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	/**
	 * Gets the user's first name.
	 *
	 * @return the user's first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the user's first name.
	 *
	 * @param firstName the new user's first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Gets the user's last name.
	 *
	 * @return the user's last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Sets the user's last name.
	 *
	 * @param lastName the new user's last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Gets the user's id.
	 *
	 * @return the user's id
	 */
	public String getID() {
		return ID;
	}
	
	/**
	 * Sets the user's id.
	 *
	 * @param iD the new user's id
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * Gets the user's role.
	 *
	 * @return the user's role
	 */
	public Roles getRole() {
		return role;
	}

	/**
	 * Sets the user's role.
	 *
	 * @param role the new user's role
	 */
	public void setRole(Roles role) {
		this.role = role;
	}

	/**
	 * Gets the user's access status.
	 *
	 * @return the user's access status
	 */
	public Access getAccess() {
		return access;
	}

	/**
	 * Sets the user's access status.
	 *
	 * @param access the new user's access status
	 */
	public void setAccess(Access access) {
		this.access = access;
	}

}