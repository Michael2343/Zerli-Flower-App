package Entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import Utilities.GenericUtilties;

// TODO: Auto-generated Javadoc
/**
 * The Class Complaint.
 */
public class Complaint implements Serializable{
	
	/** The user's ID in the system. */
	private String user_id;
	
	/** The complaint's ID. */
	private int complaint_id;
	
	/** The complaint in text. */
	private String complain_text;
	
	/** The complaint's answer in text. */
	private String answer_text;
	
	/** The complaint time - Timestap format. */
	private Timestamp complain_time;
	
	/** The cost of the item to complain. */
	private int cost;
	
	/** The branch to complain to. */
	private String branch;
	
	/** The ID number of the complaining customer . */
	private String IDnumber;
	
	/** The complaint's date. */
	private String Comp_date;
	
	/** Shows if the complaint is notified. */
	private int isNotNotified;
	
	/**
	 * Gets the notified status
	 *
	 * @return 1 if notified 0 otherwise
	 */
	public int getIsNotNotified() {
		return isNotNotified;
	}
	
	/**
	 * Sets the notified status.
	 *
	 * @param isNotNotified the new notified status
	 */
	public void setIsNotNotified(int isNotNotified) {
		this.isNotNotified = isNotNotified;
	}
	
	/**
	 * Gets the complainant's date.
	 *
	 * @return the complainant's date
	 */
	public String getComp_date() {
		return Comp_date;
	}	
	
	/**
	 * Gets the ID number of the complaining customer.
	 *
	 * @return the ID number of the complaining customer
	 */
	public String getIDnumber() {
		return IDnumber;
	}
	
	/**
	 * Sets the complainant's ID number.
	 *
	 * @param iDnumber the new complainant's ID number
	 */
	public void setIDnumber(String iDnumber) {
		IDnumber = iDnumber;
	}
	
	/**
	 * Gets the branch to complain to.
	 *
	 * @return the branch to complain to
	 */
	public String getBranch() {
		return branch;
	}
	
	/**
	 * Sets the branch to complain to.
	 *
	 * @param branch the new branch to complain to
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}
	
	/** The amount to refund. */
	private int refund;
	
	/**
	 * Gets the user's ID.
	 *
	 * @return the user's ID
	 */
	public String getUser_id() {
		return user_id;
	}
	
	/**
	 * Sets the user's ID.
	 *
	 * @param user_id the new user's ID
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	
	/**
	 * Gets the complain's text.
	 *
	 * @return the complain's text
	 */
	public String getComplain_text() {
		return complain_text;
	}
	
	/**
	 * Sets the complain's text.
	 *
	 * @param complain_text the new complain's text
	 */
	public void setComplain_text(String complain_text) {
		this.complain_text = complain_text;
	}
	
	/**
	 * Gets the complain's answer text.
	 *
	 * @return the complain's answer text
	 */
	public String getAnswer_text() {
		return answer_text;
	}
	
	/**
	 * Sets the complain's answer text.
	 *
	 * @param answer_text the new complain's answer text
	 */
	public void setAnswer_text(String answer_text) {
		this.answer_text = answer_text;
	}
	
	/**
	 * Gets the complain's time.
	 *
	 * @return the complain's time
	 */
	public Timestamp getComplain_time() {
		return complain_time;
	}
	
	/**
	 * Sets the complain's time.
	 *
	 * @param complain_time the new complain's time
	 */
	public void setComplain_time(Timestamp complain_time) {
		this.complain_time = complain_time;
		this.Comp_date=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(GenericUtilties.Convert_LocalDate_To_SQLDate(complain_time));
	}
	
	/**
	 * Gets the cost of the product to complain about.
	 *
	 * @return the cost of the product to complain about
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 * Sets the cost of the product to complain about.
	 *
	 * @param cost the new cost of the product to complain about
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/**
	 * Gets the amount to refund.
	 *
	 * @return the amount to refund
	 */
	public int getRefund() {
		return refund;
	}
	
	/**
	 * Sets the amount to refund.
	 *
	 * @param refund the new amount to refund
	 */
	public void setRefund(int refund) {
		this.refund = refund;
	}
	
	/**
	 * Gets the complaint's ID.
	 *
	 * @return the complaint's ID
	 */
	public int getComplaint_id() {
		return complaint_id;
	}
	
	/**
	 * Sets the complaint's ID.
	 *
	 * @param complaint_id the new complaint's ID
	 */
	public void setComplaint_id(int complaint_id) {
		this.complaint_id = complaint_id;
	}
	
	/**
	 * Returns a string containing all the attributes of a complaint.
	 *
	 * @return string containing all the attributes of a complaint
	 */
	@Override
	public String toString() {
		return "Complaint [user_id=" + user_id + ", complaint_id=" + complaint_id + ", complain_text=" + complain_text
				+ ", answer_text=" + answer_text + ", complain_time=" + complain_time + ", cost=" + cost + ", branch="
				+ branch + ", refund=" + refund + "]";
	}
}
