package Entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;

import Utilities.GenericUtilties;

// TODO: Auto-generated Javadoc
/**
 * contain order details to transfer between the server and the client.
 */
public class Order implements Serializable {
	
	/** The user ID. */
	private String userID;
	
	/** The order ID. */
	private String orderID;
	
	/** The payment method. */
	private PaymentMethods paymentMethod;
	
	/** The shipping method. */
	private ShippingMethods shippingMethod;
	
	/** The data which the order been created. */
	private Timestamp orderDate;
	
	/** Estimate shipping date. */
	private Timestamp shippingDate;
	
	/** The name of the branch to which the order is associated. */
	private String branchName;
	
	/** The total price of the order. */
	private int totalPrice;
	
	/** The status of the order use OrderStatus enum. */
	private OrderStatus status;
	
	/** The list of items that make up the order. */
	private ArrayList<ItemInList> items;

	/** The greeting card. */
	private String greetingCard;
	
	/** The city. */
	private String city;
	
	/** The address. */
	private String address;
	
	/** The full name of the customer. */
	private String fullname;
	
	/** The phone number of the customer. */
	private String phone;
	
	/** The order date in string . */
	private String order_date;
	
	/** The shipping date in string . */
	private String shipping_date;
	
	/** The payment method use PaymentMethod. */
	private String payment_method;
	
	/** The shipping method. */
	private String shipping_method;
	
	/** The order status. */
	private String order_status;
	
	/**
	 * Instantiates a new order.
	 *
	 * @param userID the user ID
	 * @param orderID the order ID
	 * @param paymentMethod the payment method
	 * @param shippingtMethod the shippingt method
	 * @param orderDate the order date
	 * @param shippingDate the shipping date
	 * @param branchName the branch name
	 * @param totalPrice the total price
	 * @param status the status
	 * @param items the items
	 * @param city the city
	 * @param address the address
	 */
	public Order(String userID, String orderID, PaymentMethods paymentMethod, 
			ShippingMethods shippingtMethod, Timestamp orderDate,
			Timestamp shippingDate, String branchName, int totalPrice, 
			OrderStatus status, ArrayList<ItemInList> items,String city,String address) {
		this.userID = userID;
		this.orderID = orderID;
		this.paymentMethod = paymentMethod;
		this.shippingMethod = shippingtMethod;
		this.orderDate = orderDate;
		this.shippingDate = shippingDate;
		this.branchName = branchName;
		this.totalPrice = totalPrice;
		this.status = status;
		this.items = items;
		this.city = city;
		this.address = address;
	}
	
	/**
	 * Instantiates a new order.
	 */
	public Order()
	{
		
	}
	
	/**
	 * Gets the user ID.
	 *
	 * @return the user ID
	 */
	public String getUserID() {
		return userID;
	}
	
	/**
	 * Sets the user ID.
	 *
	 * @param userID the new user ID
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	/**
	 * Gets the order ID.
	 *
	 * @return the order ID
	 */
	public String getOrderID() {
		return orderID;
	}
	
	/**
	 * Sets the order ID.
	 *
	 * @param orderID the new order ID
	 */
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	
	/**
	 * Gets the payment method.
	 *
	 * @return the payment method
	 */
	public PaymentMethods getPaymentMethod() {
		return paymentMethod;
	}
	
	/**
	 * Sets the payment method.
	 *
	 * @param paymentMethod the new payment method
	 */
	public void setPaymentMethod(PaymentMethods paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	/**
	 * Gets the shipping method.
	 *
	 * @return the shipping method
	 */
	public ShippingMethods getShippingMethod() {
		return shippingMethod;
	}
	
	/**
	 * Sets the shipping method.
	 *
	 * @param shippingMethod the new shipping method
	 */
	public void setShippingMethod(ShippingMethods shippingMethod) {
		this.shippingMethod = shippingMethod;
	}
	
	/**
	 * Gets the order date.
	 *
	 * @return the order date
	 */
	public Timestamp getOrderDate() {
		return orderDate;
	}
	
	/**
	 * Sets the order date.
	 *
	 * @param orderDate the new order date
	 */
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	
	/**
	 * Gets the shipping date.
	 *
	 * @return the shipping date
	 */
	public Timestamp getShippingDate() {
		return shippingDate;
	}
	
	/**
	 * Sets the shipping date.
	 *
	 * @param shippingDate the new shipping date
	 */
	public void setShippingDate(Timestamp shippingDate) {
		this.shippingDate = shippingDate;
	}
	
	/**
	 * Gets the branch name.
	 *
	 * @return the branch name
	 */
	public String getBranchName() {
		return branchName;
	}
	
	/**
	 * Sets the branch name.
	 *
	 * @param branchName the new branch name
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	/**
	 * Gets the total price.
	 *
	 * @return the total price
	 */
	public int getTotalPrice() {
		return totalPrice;
	}
	
	/**
	 * Sets the total price.
	 *
	 * @param totalPrice the new total price
	 */
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public OrderStatus getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public ArrayList<ItemInList> getItems() {
		return items;
	}
	
	/**
	 * Sets the items.
	 *
	 * @param items the new items
	 */
	public void setItems(ArrayList<ItemInList> items) {
		this.items = items;
	}
	
	/**
	 * Gets the greeting card.
	 *
	 * @return the greeting card
	 */
	public String getGreetingCard() {
		return greetingCard;
	}
	
	/**
	 * Sets the greeting card.
	 *
	 * @param greetingCard the new greeting card
	 */
	public void setGreetingCard(String greetingCard) {
		this.greetingCard = greetingCard;
	}
	
	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Gets the order date.
	 *
	 * @return the order date
	 */
	public String getOrder_date() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(GenericUtilties.Convert_LocalDate_To_SQLDate(orderDate));
	}
	
	/**
	 * Sets the order date.
	 *
	 * @param order_date the new order date
	 */
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	
	/**
	 * Gets the shipping date.
	 *
	 * @return the shipping date
	 */
	public String getShipping_date() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(GenericUtilties.Convert_LocalDate_To_SQLDate(shippingDate));
		
	}	
	
	/**
	 * Sets the shipping date.
	 *
	 * @param shipping_date the new shipping date
	 */
	public void setShipping_date(String shipping_date) {
		this.shipping_date = shipping_date;
	}
	
	/**
	 * Gets the payment method.
	 *
	 * @return the payment method
	 */
	public String getPayment_method() {
		switch(getPaymentMethod()){
		case credit_card:  return "Credit card";
		case zerli_coins:  return "Zerli coins";
		default: break;
		}
		return null;
	}
	
	/**
	 * Sets the payment method.
	 *
	 * @param payment_method the new payment method
	 */
	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}
	
	/**
	 * Gets the shipping method.
	 *
	 * @return the shipping method
	 */
	public String getShipping_method() {
		switch(getShippingMethod()){
		case shipping:  return "Shipping";
		case pickup:  return "Pickup";
		default: break;
	}
	return null;
	}
	
	/**
	 * Sets the shipping method.
	 *
	 * @param shipping_method the new shipping method
	 */
	public void setShipping_method(String shipping_method) {
		this.shipping_method = shipping_method;
	}
	
	/**
	 * Gets the order status.
	 *
	 * @return the order status
	 */
	public String getOrder_status() {
		switch(getStatus()){
		case pending_confirm:  return "Pending confirm";
		case pending_cancel:  return "Pending cancel";
		case confirmed: return "Confirmed";
		case canceled:return  "Canceled" ;
		case completed:return "Completed";
		default: break;
	}
	return null;
	}
	
	/**
	 * Sets the order status.
	 *
	 * @param order_status the new order status
	 */
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	
	/**
	 * Gets the fullname.
	 *
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname;
	}
	
	/**
	 * Sets the fullname.
	 *
	 * @param fullname the new fullname
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
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
		return "Order [userID=" + userID + ", orderID=" + orderID + ", paymentMethod=" + paymentMethod
				+ ", shippingMethod=" + shippingMethod + ", orderDate=" + orderDate + ", shippingDate=" + shippingDate
				+ ", branchName=" + branchName + ", totalPrice=" + totalPrice + ", status=" + status + ", items="
				+ items + ", greetingCard=" + greetingCard + ", city=" + city + ", address=" + address + "]";
	}
	


}
