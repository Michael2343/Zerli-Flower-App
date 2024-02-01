package Entities;

import java.io.Serializable;

/**
 * The Class NotificationInTable.
 */
public class NotificationInTable implements Serializable {

	/** The number of notifications. */
	private int notificationnumber;
	
	/** The notification's content. */
	private String content;
	
	/** The notification's status. */
	private String status;
	
	/** Where the notification came from. *////////////////////
	private String from;
	
	/** Where to send the notification. *///////////////////////////
	private String to;
	
	/**
	 * Instantiates a new notification.
	 */
	public NotificationInTable() {}

	/**
	 * Instantiates a new notification.
	 *
	 * @param notificationnumber the number of notifications
	 * @param from Where the notification came from
	 * @param content the notification's content
	 * @param status the notification's status
	 */
	public NotificationInTable(int notificationnumber,String from, String content, String status) {
		this.notificationnumber = notificationnumber;
		this.content = content;
		this.status = status;
		this.from = from;
	}
	
	/**
	 * Gets where to send the notification 
	 *
	 * @return where to send the notification 
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Sets where to send the notification.
	 *
	 * @param to the new notification target
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Gets the notifications number.
	 *
	 * @return the notifications number
	 */
	public int getNotificationnumber() {
		return notificationnumber;
	}
	
	/**
	 * Sets the notifications number.
	 *
	 * @param notificationnumber the new notifications number
	 */
	public void setNotificationnumber(int notificationnumber) {
		this.notificationnumber = notificationnumber;
	}
	
	/**
	 * Gets the notification's content.
	 *
	 * @return the notification's content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Sets the notification's content.
	 *
	 * @param content the new notification's content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Gets the notification's status.
	 *
	 * @return the notification's status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the notification's status.
	 *
	 * @param status the new notification's status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Gets where the notification came from.
	 * @return where the notification came from
	 */
	public String getFrom() {
		return from;
	}
	
	/**
	 * Sets where the notification comes from.
	 * @param from the new notification's origin
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	
	/**
	 * Returns a string contacting the notifications attributes
	 * @return string contacting the notifications attributes
	 */
	@Override
	public String toString() {
		return "NotificationInTable [notificationnumber=" + notificationnumber  + ", content="
			+ content + ", status=" + status + ", from=" + from + "]";
	}

}
