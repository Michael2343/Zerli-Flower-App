package server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import Entities.*;
import Utilities.GenericUtilties;
import javafx.scene.image.Image;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerConnSQL is responsible for the connection to the sql server
 * and is used as the main driver for different data request and data storage
 * protocol commands.
 */
public class ServerConnSQL {

	/** The Main JDBC Connection element, used to drive the communication. */
	private static Connection conn;
	// private String mySQLpassword = "123456";

	/**
	 * Starts the sql connection.
	 *
	 * @param mySQL password the sql database password
	 * @return true, if successful
	 */
	public static boolean startConn(String mySQLpassword) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/midproject?useLegacyDatetimeCode=false&serverTimezone=Israel","root",mySQLpassword);
			System.out.println("SQL connection succeed");
			return true;
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		}
	}
	public static void importData() {
		Server.Log("Database", "Executing importData");
		String[] users = {
				"INSERT INTO `midproject`.`user_details` (`user_id`, `first_name`, `last_name`, `id`, `email`, `phone`, `role`) VALUES ('haim', 'haim ', 'svhvili', '951951951', 'hs@gmail.com', '0523654123', 'customer');",
				"INSERT INTO `midproject`.`user_details` (`user_id`, `first_name`, `last_name`, `id`, `email`, `phone`, `role`) VALUES ('naor', 'naor', 'azran', '010203040', 'na@gmail.com', '0583216545', 'customer');",
				"INSERT INTO `midproject`.`user_details` (`user_id`, `first_name`, `last_name`, `id`, `email`, `phone`, `role`) VALUES ('liran', 'liran ', 'greyshirt', '090807060', 'lgs@gmail.com', '0598745212', 'customer');"
				};
		PreparedStatement stmt;
		 try {
			 	for(String user : users) {
			 		stmt = conn.prepareStatement(user);
				 	stmt.executeUpdate();
			 	}
		 }
		 catch (Exception e) {
	            e.printStackTrace();
	        	Server.Log("Database", "Executing importData : FAILED");
	        }	
		 Server.Log("Database", "Executing importData : SUCCESS");
	}

	/**
	 * Authenticates a user data.
	 *
	 * @param username the username of the attempter
	 * @param password the password of the attempter
	 * @return the object[] holds the transaction data, [0] - loggedin, [1] - access, [2] - role
	 */
	public Object[] Authenticate(String username, String password) {
		Server.Log("Database", "Executing Authenticate");
		PreparedStatement stmt = null;
		Object[] logindetails = new Object[3];
		ArrayList<String> listOfUserStrings=new ArrayList<String>();
		int Loggedin;
		Roles role;
		Access access;
		ResultSet rs;
		try {
			
			stmt = conn.prepareStatement("SELECT ld.loggedin,ld.access,ud.role FROM login_details ld,user_details ud "
					+ "WHERE ld.user_id=? AND ld.user_id = ud.user_id " + "And ld.password=?");
			stmt.setString(1, username);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			rs.next();
			Loggedin = rs.getInt(1);
			access = Access.valueOf(rs.getString(2));
			role = Roles.valueOf(rs.getString(3));
			
			if((role == Roles.customer && Loggedin == 0)  || 
					(role != Roles.customer && access != Access.inactive && Loggedin == 0)) {
				stmt = conn.prepareStatement("UPDATE login_details SET loggedin=? WHERE user_id=?");
				stmt.setInt(1, 1);
				stmt.setString(2, username);
				stmt.executeUpdate();
			}
			logindetails[0] = Loggedin;
			logindetails[1] = access;
			logindetails[2] =role;
			Server.Log("Database", "Executing Authenticate: SUCCESS");
			return logindetails;
			
		} catch (SQLException e1) {
			logindetails[1] = Access.noaut;
			return logindetails;
		}

	}

	/**
	 * Logging out operation.
	 *
	 * @param username the username that asks to log out
	 */
	public void LoggedOut(String username) {
    	Server.Log("Database", "Executing LoggedOut");

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("UPDATE login_details SET loggedin='0' WHERE user_id=?");
			stmt.setString(1, username);
			stmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
			Server.Log("Database", "Executing LoggedOut: FAILED");
		}
    	Server.Log("Database", "Executing LoggedOut: SUCCESS");

	}

	/**
	 * Gets the cart items.
	 *
	 * @param username the username the owns the cart
	 * @param cartItems the cart items collection to be altered
	 */
	public void getCartItems(String username,ArrayList<ItemInList> cartItems) {
    	Server.Log("Database", "Executing getCartItems");
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT quantity FROM cart_item WHERE cart_id=(SELECT cart_id FROM carts WHERE user_id= ?)");
			stmt.setString(1, username);
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		ArrayList<Integer> quantityList=new ArrayList<Integer>();
		ResultSet rs;
		 try {
			 	rs=stmt.executeQuery();
			 	while (rs.next()) {
			 		quantityList.add(rs.getInt(1));
			 	}
		 }
		 catch (Exception e) {
	            e.printStackTrace();
	        }
		try {
			stmt = conn.prepareStatement("SELECT * FROM items WHERE item_id IN  (SELECT item_id FROM Cart_item WHERE cart_id=(SELECT cart_id FROM carts WHERE user_id= ?)) AND isAvailable=1");
			stmt.setString(1, username);
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		ItemInList itemInListlist=null;
		Blob image;
		InputStream stream;
		int i=0;
		 try {
			 	rs=stmt.executeQuery();
	            while (rs.next()) {
	            	Image bufferImage;
	            	image=rs.getBlob(7);
	            	if (image == null)
	            	{
	            		stream = getClass().getResourceAsStream("/png/no-image.png");
	            	}
	            	else {
	            		stream = image.getBinaryStream();
	            	}
	            	/*** take sale price or regular price ***/
	            	int price;
	            	if(rs.getInt(8) == 1)
	            		price = rs.getInt(9);
	            	else
	            		price = rs.getInt(3);
	            	itemInListlist = new ItemInList(stream.readAllBytes(),rs.getInt(1),quantityList.get(i),
	            			price, rs.getString(2),ItemType.valueOf(rs.getString(5)),
	            			CatalogType.valueOf(rs.getString(4)));
	            	cartItems.add(itemInListlist);
	            	i++;          
	            	}
/////////////////////////////////////////////////////////////////////////////////////////////
	            stmt = conn.prepareStatement("select new_item_id from cart_new_item where cart_id in "
            			+ "(select cart_id from carts where user_id=?)");
	            stmt.setString(1, username);
            	rs=stmt.executeQuery();
            	while(rs.next())
            	{
	            NewItem newItem = new NewItem();
	            newItem.setImage(getClass().getResourceAsStream("/png/New.png").readAllBytes());
	            newItem.setItem_id(rs.getInt(1));
	            PreparedStatement stmt2 = null;
	            ResultSet rs2;
	            stmt2 = conn.prepareStatement("select * from items where item_id in "
	            		+ "(select catalog_item_id from new_item_spec where new_item_id =?)");	
	            stmt2.setInt(1, newItem.getItem_id());
	            rs2=stmt2.executeQuery();
	            ///////
	            PreparedStatement stmt3 = null;
	            ResultSet rs3;
            	stmt3 = conn.prepareStatement("select quantity from new_item_spec where new_item_id =? ");
            	stmt3.setInt(1, newItem.getItem_id());
            	rs3=stmt3.executeQuery();
            	int price=0;
	            while (rs2.next()) {
            	image=rs2.getBlob(7);
            	if (image == null)
            	{
            		stream = getClass().getResourceAsStream("/png/no-image.png");
            	}
            	else {
            		stream = image.getBinaryStream();
            	}
            	
            	itemInListlist = new ItemInList(stream.readAllBytes(),rs2.getInt(1),0,
            			rs2.getInt(3), rs2.getString(2),ItemType.valueOf(rs2.getString(5)),
            			CatalogType.valueOf(rs2.getString(4)));
            	
            	rs3.next();
            	/*** get sale price or regular price ***/
            	int actual;
            	if(rs2.getInt(8) == 1)
            		actual = rs2.getInt(9);
            	else
            		actual = rs2.getInt(3);
            	price+=actual*rs3.getInt(1);
            	itemInListlist.setQuantity(rs3.getInt(1));
            	newItem.addItem(itemInListlist);
		 		}//while end
	            ///////
	            newItem.setPrice(price);
            	stmt2 = conn.prepareStatement("SELECT quantity FROM cart_new_item WHERE cart_id="
            			+ " (SELECT cart_id FROM carts WHERE user_id= ?) and new_item_id =?");
            	stmt2.setString(1, username);
            	stmt2.setInt(2, newItem.getItem_id());
            	rs2=stmt2.executeQuery();
            	rs2.next();
            	newItem.setQuantity(rs2.getInt(1));
            	////
            	stmt3 = conn.prepareStatement("SELECT new_item_name FROM new_items WHERE"
            			+ " new_item_id=?");
            	stmt3.setInt(1, newItem.getItem_id());
            	rs3=stmt3.executeQuery();
            	rs3.next();
            	newItem.setItemName(rs3.getString(1));
            	///
            	cartItems.add(newItem);
            	}
     
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 Server.Log("Database", "Executing getCartItems: SUCCESS");
	}
	
	/**
	 * Removes the item by ID.
	 *
	 * @param id the item id
	 */
	public void removeItemByID(int id)
	{
		Server.Log("Database", "Executing removeItemByID");
		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("UPDATE Items SET isAvailable=0 WHERE item_id=?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
    		Server.Log("Database", "Executing RemoveItemByID: FAILED");
    		e.printStackTrace();
		}	
	}
	
	/**
	 * Gets the item by ID.
	 *
	 * @param id the item id
	 * @return the item entity
	 */
	public Item getItemByID(int id) {
		Server.Log("Database", "Executing getItemByID");
		Item item=null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM items where item_id=?");
			stmt.setInt(1, id);
			} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		Blob image;
		ResultSet rs;
		InputStream stream;
		 try {
	           	rs = stmt.executeQuery();
	            while (rs.next()) {

	            	//need to change blob to long blob 
	            	Image bufferImage;
	            	image=rs.getBlob(7);
	            	if (image == null)
	            	{
	            		stream = getClass().getResourceAsStream("/png/no-image.png");
	            		//stream =new FileInputStream("/png/no-image.png");
	            	}
	            	else {
	            		stream = image.getBinaryStream();
		            	//bufferImage = new Image(input);
	            	}
	            	
	            	item = new Item(rs.getInt(1), rs.getString(2), rs.getInt(3),CatalogType.valueOf(rs.getString(4)), 
	            			ItemType.valueOf(rs.getString(5)), 
	            			Color.valueOf(rs.getString(6)==null?"non_color":rs.getString(6)),stream.readAllBytes(),rs.getInt(8) == 1? true : false,rs.getInt(9));
	            	return item;
	            }
	        } catch (Exception e) {
	    		Server.Log("Database", "Executing GetItemByID: FAILED");
	    		e.printStackTrace();
	        }
		 return item;
	}

	/**
	 * Gets the catalog items.
	 *
	 * @param catalogItems the catalog items collection to be altered
	 * @param catalogType - the catalog type which the requester asks
	 */
	public void getCatalogItems(ArrayList<Item> catalogItems, CatalogType catalogType) {
		Server.Log("Database", "Executing GetCatalogItems");
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM items where catalog_type=? AND isAvailable=1");
			stmt.setString(1, catalogType.toString());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		Blob image;
		Item item = null;
		ResultSet rs;
		InputStream stream;
		try {
			rs = stmt.executeQuery();
			while (rs.next()) {

				// need to change blob to long blob
				Image bufferImage;
				image = rs.getBlob(7);
				if (image == null) {
					stream = getClass().getResourceAsStream("/png/no-image.png");
					// stream =new FileInputStream("/png/no-image.png");
				} else {
					stream = image.getBinaryStream();
					// bufferImage = new Image(input);
				}

				item = new Item(rs.getInt(1), rs.getString(2), rs.getInt(3), CatalogType.valueOf(rs.getString(4)),
						ItemType.valueOf(rs.getString(5)),
						Color.valueOf(rs.getString(6) == null ? "non_color" : rs.getString(6)), stream.readAllBytes(),rs.getInt(8) > 0 ? true : false , rs.getInt(9));
				catalogItems.add(item);
			}
		} catch (Exception e) {
			Server.Log("Database", "Executing GetCatalogItems: FAILED");
			e.printStackTrace();
		}
	}

	/**
	 * Gets the branches.
	 *
	 * @return an array list of type string that holds the branches
	 */
	public ArrayList<String> GetBranches() {
		Server.Log("Database", "Executing GetBranches");
		ArrayList<String> branches = new ArrayList<String>();
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			stmt = conn.prepareStatement("SELECT Distinct branch_name FROM branch_employees WHERE branch_name != 'zerli'");
			rs = stmt.executeQuery();
			while (rs.next())
				branches.add(rs.getString(1));
		} catch (SQLException e1) {
			Server.Log("Database", "Executing GetCatalogItems: FAILED");
			e1.printStackTrace();
		}
		return branches;
	}

	/**
	 * Gets the currency (credit card, zcoins, etc..) of a user.
	 *
	 * @param requestee the user that requests his currency
	 * @return the string[] holds the currency data (full credit card, zcoins etc..)
	 */
	public String[] GetCurrency(String requestee) {
		Server.Log("Database", "Executing GetCurrency");
		String[] details = null;
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			stmt = conn.prepareStatement(
					"SELECT credit_card,cvv,exp_month,exp_year,zerli_coin,new_customer FROM customer_details WHERE user_id='"
							+ requestee + "'");
			rs = stmt.executeQuery();
			while (rs.next())
				details = new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6) };
		} catch (SQLException e1) {
			Server.Log("Database", "Executing GetCurrency: FAILED");
			e1.printStackTrace();
		}
		return details;
	}

	/**
	 * Update zerli coins of a user.
	 *
	 * @param requestee the user that wants to get zcoins
	 * @param data the new value of zcoins
	 */
	public void UpdateZerliCoins(String requestee, int data) {
		Server.Log("Database", "Executing UpdateZerliCoins");
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("UPDATE customer_details SET zerli_coin=? WHERE user_id=?");
			stmt.setInt(1, data);
			stmt.setString(2, requestee);
			stmt.executeUpdate();
		} catch (SQLException e1) {
			Server.Log("Database", "Executing UpdateZerliCoins: FAILED");
			e1.printStackTrace();
		}
	}

	/**
	 * Insert order.
	 *
	 * @param order the Order entity of the order to insert
	 * @param requestee the user who wishes to place the order
	 */
	public void InsertOrder(Order order,String requestee)
	{
		Server.Log("Database", "Executing InsertOrder");
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO orders (user_id,payment_method,shipping_method,order_date,shipping_date,branch_name,greeting_card,total_price,status,address,city)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setString(1, requestee);
			stmt.setString(2, order.getPaymentMethod().toString());
			stmt.setString(3, order.getShippingMethod().toString());
			stmt.setTimestamp(4, order.getOrderDate());
			if(order.getShippingDate() != null)
				stmt.setTimestamp(5, order.getShippingDate());
			else
				stmt.setNull(5,Types.TIMESTAMP);;
			stmt.setString(6, order.getBranchName());
			if(order.getGreetingCard() != null)
				stmt.setString(7, order.getGreetingCard());
			else
				stmt.setNull(7,java.sql.Types.VARCHAR);
			stmt.setInt(8, order.getTotalPrice());
			stmt.setString(9,order.getStatus().toString());
			if(order.getAddress() != null)
				stmt.setString(10, order.getAddress());
			else
				stmt.setNull(10,Types.VARCHAR);;
			if(order.getCity() != null)
				stmt.setString(11, order.getCity());
			else
				stmt.setNull(11,Types.VARCHAR);;
           	stmt.executeUpdate();
           	
           	
           	
           	/*
           	 * 
           	 */
           	stmt = conn.prepareStatement("SELECT MAX(order_id) FROM orders");
           	ResultSet rs = stmt.executeQuery();
           	int orderID = -1;
           	while(rs.next())
           	{
           		orderID = rs.getInt(1);
           	}
           	if(orderID == -1){
        		Server.Log("Database", "Executing InsertOrder: FAILED could not track OrderID");
           		throw new RuntimeException("ERROR");
           	}
           	/////
           	/////
           	for(ItemInList item : order.getItems())
           	{
           		if (item instanceof NewItem) 
           		{
           			NewItem newItem =(NewItem)item;
           			stmt = conn.prepareStatement("INSERT INTO order_new_item VALUES (?,?,?)");
               		stmt.setInt(1, orderID);
               		stmt.setInt(2, newItem.getItem_id());
               		stmt.setInt(3, newItem.getQuantity());
               		stmt.executeUpdate();
               		PreparedStatement stmt2 = null;
                    ResultSet rs2;
                    stmt2 = conn.prepareStatement("select price,is_sale,salePrice from items where item_id in "
                    		+ "(select catalog_item_id from new_item_spec where new_item_id =?)");	
                    stmt2.setInt(1, newItem.getItem_id());
                    rs2=stmt2.executeQuery();
                    ///////
                    PreparedStatement stmt3 = null;
                    ResultSet rs3;
                	stmt3 = conn.prepareStatement("select quantity from new_item_spec where new_item_id =? ");
                	stmt3.setInt(1, newItem.getItem_id());
                	rs3=stmt3.executeQuery();
                	int price=0;
                    while (rs2.next()) {
                    rs3.next();
                	/*** get sale price or regular price ***/
                	int actual;
                	if(rs2.getInt(2) == 1)
                		actual = rs2.getInt(3);
                	else
                		actual = rs2.getInt(1);
                	price+=actual*rs3.getInt(1);
        	 		}
                    stmt3 = conn.prepareStatement("UPDATE new_items Set price  =? where new_item_id =? ");
                    stmt3.setInt(1,price);
                	stmt3.setInt(2, newItem.getItem_id());
                	stmt3.executeUpdate();
				}
           		else
           		{
           			stmt = conn.prepareStatement("INSERT INTO order_item VALUES (?,?,?)");
               		stmt.setInt(1, orderID);
               		stmt.setInt(2, item.getItem_id());
               		stmt.setInt(3, item.getQuantity());
               		stmt.executeUpdate();
				}
               
           	}
           	
           	stmt = conn.prepareStatement("DELETE FROM cart_item WHERE "
           			+ "cart_id = (SELECT cart_id FROM carts Where user_id=?)" );
			stmt.setString(1, requestee);
           	stmt.executeUpdate();
           	
           	stmt = conn.prepareStatement("DELETE FROM cart_new_item WHERE "
           			+ "cart_id = (SELECT cart_id FROM carts Where user_id=?)" );
			stmt.setString(1, requestee);
           	stmt.executeUpdate();
           	

		} 
		catch (SQLException e1) {
    		Server.Log("Database", "Executing InsertOrder: FAILED");
			e1.printStackTrace();
		}
	}

	/**
	 * Gets the branch of an employee.
	 *
	 * @param user_id the user id of the employee who seeks to find out his allocated branch
	 * @return the branch the user id is allocated to
	 */
	public String GetBranch(String user_id) {
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
	
			stmt = conn.prepareStatement("SELECT branch_name FROM branch_employees WHERE user_id = ?");
			stmt.setString(1, user_id);
			rs = stmt.executeQuery();
			if (rs.next() == false) {

				return "no branch";
			} else
				return rs.getString(1);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return "";

		}
	}
	
	/**
	 * Gets the orders placed on a specific branch.
	 *
	 * @param orders - the orders collection to be altered
	 * @param branch_name - the branch name to look on
	 * @param role - the role of the requester
	 */
	public void GetOrdersByBranch(ArrayList<Order> orders, String branch_name,String role) {
		PreparedStatement stmt = null;
		ResultSet rs;
		Order newOrder;
		try {
			if(role.equals("manager")) {
				Server.Log("Database", "Executing GetOrdersByBranch: Manager");
				stmt = conn.prepareStatement("SELECT * FROM orders WHERE branch_name = ? AND "
						+ "status = 'pending_confirm' OR status = 'pending_cancel' ");
				stmt.setString(1, branch_name);
			}
			else {
				Server.Log("Database", "Executing GetOrdersByBranch: Delivery");
				stmt = conn.prepareStatement("SELECT * FROM orders WHERE branch_name = ? AND "
						+ "status = 'confirmed' AND shipping_method='shipping'");
				stmt.setString(1, branch_name);
			}
				rs = stmt.executeQuery();
				while (rs.next()) {
					newOrder = new Order();
					newOrder.setUserID(rs.getString(1));
					newOrder.setOrderID(rs.getString(2));
					newOrder.setPaymentMethod(PaymentMethods.valueOf(rs.getString(3)));
					newOrder.setShippingMethod(ShippingMethods.valueOf(rs.getString(4)));
					newOrder.setOrderDate(rs.getTimestamp(5));
					newOrder.setShippingDate(rs.getTimestamp(6));
					newOrder.setTotalPrice(rs.getInt(9));
					newOrder.setStatus(OrderStatus.valueOf(rs.getString(10)));
					newOrder.setAddress(rs.getString(11));
					newOrder.setCity(rs.getString(12));
					
					PreparedStatement stmt1 = null;
					ResultSet rs1;
					stmt1 = conn.prepareStatement("SELECT first_name,last_name,phone "
							+ "FROM user_details WHERE user_id = ?");
					stmt1.setString(1, rs.getString(1));
					rs1 = stmt1.executeQuery();
					rs1.next();
					newOrder.setFullname(rs1.getString(1)+ " "+ rs1.getString(2));
					newOrder.setPhone(rs1.getString(3));
					orders.add(newOrder);
				}
		} catch (SQLException e1) {
			Server.Log("Database", "Executing GetOrdersByBranch: FAILED");
			e1.printStackTrace();
		}
		Server.Log("Database", "Executing GetOrdersByBranch to branch" + branch_name + ": Success");

	}

	/**
	 * Gets the items of an order.
	 *
	 * @param itemsOfOrder - The items in order collection to be altered
	 * @param order_id - the order id of an order
	 */
	public void GetItemsOfOrder(ArrayList<ItemInList> itemsOfOrder, int order_id) {
		PreparedStatement stmt = null;
		ResultSet rs;
		ResultSet rs2;
		ItemInList newItemInList;
		try {
			stmt = conn.prepareStatement(
					"SELECT * FROM items WHERE item_id IN (SELECT item_id FROM order_item WHERE order_id = ?)");
			stmt.setInt(1, order_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				newItemInList = new ItemInList();
				stmt = conn.prepareStatement(
						"SELECT quantity FROM order_item WHERE item_id = ? AND order_id =? ");
				stmt.setInt(1, rs.getInt(1));
				stmt.setInt(2, order_id);
				rs2 = stmt.executeQuery();
				rs2.next();
				newItemInList.setQuantity(rs2.getInt(1));
				newItemInList.setItem_id(rs.getInt(1));
				newItemInList.setItemName(rs.getString(2));
				newItemInList.setPrice(rs.getInt(3));
				newItemInList.setCatalogType(CatalogType.valueOf(rs.getString(4)));
				newItemInList.setItemType(ItemType.valueOf(rs.getString(5)));
				itemsOfOrder.add(newItemInList);
			}
			
			stmt = conn.prepareStatement("SELECT new_item_id from order_new_item where order_id=?");
       		stmt.setInt(1,order_id);
       		rs2 = stmt.executeQuery();
       		ResultSet rs3;
       		while(rs2.next()) {
       			NewItem newItem=new NewItem() ;
       			newItem.setItem_id(rs2.getInt(1));
       			stmt = conn.prepareStatement("SELECT ni.new_item_name,oni.quantity,ni.price from order_new_item oni,new_items ni "
       					+ "where ni.new_item_id=oni.new_item_id and ni.new_item_id=?");
           		stmt.setInt(1,rs2.getInt(1));
           		rs3 = stmt.executeQuery();
           		rs3.next();
           		newItem.setItemName(rs3.getString(1));
           		newItem.setQuantity(rs3.getInt(2));
           		newItem.setPrice(rs3.getInt(3));
           		newItem.setCatalogType(CatalogType.new_item);
           		/// get all items of new item
           		
           		
           	  stmt = conn.prepareStatement("select * from items where item_id in "
	            		+ "(select catalog_item_id from new_item_spec where new_item_id =?)");	
           	  stmt.setInt(1, rs2.getInt(1));
           	  ResultSet rs5;
           	  rs5 = stmt.executeQuery();
           	  while(rs5.next()) {
           		ItemInList assemble=new ItemInList(); 
           		assemble.setItem_id(rs5.getInt(1));
           		assemble.setItemName(rs5.getString(2));
           		assemble.setCatalogType(CatalogType.valueOf(rs5.getString(4)));
           		assemble.setItemType(ItemType.valueOf(rs5.getString(5)));
           		assemble.setPrice(rs5.getInt(3));
           		  
           		stmt = conn.prepareStatement("select quantity from new_item_spec where new_item_id= ? "
  	            		+ " AND catalog_item_id = ?");	
           		stmt.setInt(1,rs2.getInt(1));
           		stmt.setInt(2,rs5.getInt(1));
           		ResultSet rs4;
           		rs4 = stmt.executeQuery();
           		rs4.next();
           		assemble.setQuantity(rs4.getInt(1));
           		newItem.addItem(assemble);
           	  }
           	  itemsOfOrder.add(newItem);
       		}
		} catch (SQLException e1) {
			Server.Log("Database", "Executing GetItemsOfOrder: FAILED");
			e1.printStackTrace();
		}

	}

	/**
	 * Confirms an order.
	 *
	 * @param order_id the order id to be confirmed
	 * @param status the status in string value
	 * @return the refund amount
	 */
	// confirm + cancel
	public int ConfirmOrder(int order_id, String status) {
		int refundCancel = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("UPDATE orders SET status = ? WHERE order_id = ?");
			stmt.setString(1, status);
			stmt.setInt(2, order_id);
			stmt.executeUpdate();
			
			if(status.equals("confirmed")) {
				stmt = conn.prepareStatement("SELECT shipping_date FROM orders WHERE order_id=?");
				stmt.setInt(1, order_id);
				ResultSet rs = stmt.executeQuery();
				rs.next();
				LocalDateTime currentTime = LocalDateTime.now();
				LocalDateTime shippingTime = Utilities.GenericUtilties.
						Convert_LocalDate_To_SQLDate(rs.getTimestamp(1));
				
				long diff = ChronoUnit.MINUTES.between(currentTime,shippingTime);
				if(diff < 3*60) {
					currentTime = currentTime.plusHours(3);
					stmt = conn.prepareStatement("UPDATE orders SET shipping_date=? WHERE order_id=?");
					stmt.setTimestamp(1, GenericUtilties.Convert_LocalDate_To_SQLDate(currentTime));
					stmt.setInt(2, order_id);
					stmt.executeUpdate();
				}
			}
			else if(status.equals("canceled")) {
				stmt = conn.prepareStatement("SELECT user_id,refund_zerli FROM orders WHERE order_id=?");
				stmt.setInt(1, order_id);
				ResultSet rs = stmt.executeQuery();
				rs.next();
				String user = rs.getString(1);
				int refund = rs.getInt(2);
				refundCancel = refund;
				stmt = conn.prepareStatement("UPDATE customer_details SET "
						+ "zerli_coin = zerli_coin + ? WHERE user_id = ?");
				stmt.setInt(1, refund);
				stmt.setString(2, user);
				stmt.executeUpdate();
			}
			
			
		} catch (SQLException e1) {
			Server.Log("Database", "Executing ConfirmOrder: FAILED");
			e1.printStackTrace();
		}
		return refundCancel;

	}

	/**
	 * Adds an item of a certain quantity into the cart 
	 *
	 * @param username the username of cart owner
	 * @param item_id the item id to be added
	 * @param quantity the quantity of that item
	 * @return the quantity
	 */
	@SuppressWarnings("resource")
	public int AddToCart(String username, int item_id, int quantity) {
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			stmt = conn.prepareStatement
					("SELECT ci.quantity FROM cart_item ci WHERE item_id=? "
							+ "AND cart_id = (SELECT c.cart_id FROM carts c WHERE user_id=?)");
			stmt.setInt(1,item_id);
			stmt.setString(2,username);
           	rs = stmt.executeQuery();
           	if(rs.next() == false) {
           		//insert
           		stmt = conn.prepareStatement("INSERT INTO cart_item "
           				+ "VALUES ((SELECT c.cart_id FROM carts c WHERE user_id=?),?,?)");
           		stmt.setString(1,username);
           		stmt.setInt(2,item_id);
           		stmt.setInt(3,quantity);
           		stmt.executeUpdate();
            	
           	}
           	else {
           	//update
           		stmt = conn.prepareStatement("UPDATE cart_item SET quantity=? WHERE item_id=? "
           				+ "AND cart_id = (SELECT c.cart_id FROM carts c WHERE user_id=?)");
           		stmt.setInt(1, quantity + rs.getInt(1));
           		stmt.setInt(2,item_id);
           		stmt.setString(3,username);
           		stmt.executeUpdate();
           		
           	}
           		
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		 return quantity;
		
	}

	/**
	 * Gets all of the orders of a customer.
	 *
	 * @param username the username of the customer
	 * @param customerOrders the collection of orders to be altered
	 */
	public void GetAllCustomerOrders(String username, ArrayList<Order> customerOrders) {
		PreparedStatement stmt = null;
		ResultSet rs1,rs2;
		Order order;
		ItemInList itemInList;
		ArrayList<ItemInList> itemList ;
		try {
			stmt = conn.prepareStatement("SELECT * FROM orders WHERE user_id=?");
			stmt.setString(1,username);
           	rs1 = stmt.executeQuery();
           	while(rs1.next()) {
           		order = new Order();
           		order.setOrderID(String.valueOf(rs1.getInt(2)));
           		order.setPaymentMethod(PaymentMethods.valueOf(rs1.getString(3)));
           		order.setShippingMethod(ShippingMethods.valueOf(rs1.getString(4)));
           		order.setOrderDate(rs1.getTimestamp(5));
           		order.setShippingDate(rs1.getTimestamp(6));
           		order.setBranchName(rs1.getString(7));
           		order.setGreetingCard(rs1.getString(8));
           		order.setTotalPrice(rs1.getInt(9));
           		order.setStatus(OrderStatus.valueOf(rs1.getString(10)));
           		order.setAddress(rs1.getString(11));
           		order.setCity(rs1.getString(12));
         
   	       		stmt = conn.prepareStatement("SELECT i.name ,i.catalog_type,"
                   		+"i.item_type,i.price, oi.quantity " 
                   		+"FROM items i, order_item oi "
                   		+"WHERE i.item_id = oi.item_id" 
                   		+" AND oi.order_id=? " 
                   		+"AND i.item_id IN (SELECT item_id "
                   		+"from order_item"
                   		+" where order_id=?)");
           		stmt.setInt(1,rs1.getInt(2));
           		stmt.setInt(2,rs1.getInt(2));
           		rs2 = stmt.executeQuery();
           		itemList = new ArrayList<>();
           		while(rs2.next()) {
           			itemInList = new ItemInList();
           			itemInList.setItemName(rs2.getString(1));
           			itemInList.setCatalogType(CatalogType.valueOf((rs2.getString(2))));
           			itemInList.setItemType(ItemType.valueOf(rs2.getString(3)));
           			itemInList.setPrice(rs2.getInt(4));
           			itemInList.setQuantity(rs2.getInt(5));
           			itemList.add(itemInList);
           		}
//////
           		
           		stmt = conn.prepareStatement("SELECT new_item_id from order_new_item where order_id=?");
           		stmt.setInt(1,rs1.getInt(2));
           		rs2 = stmt.executeQuery();
           		ResultSet rs3;
           		while(rs2.next()) {
           			NewItem newItem=new NewItem() ;
           			newItem.setItem_id(rs2.getInt(1));
           			stmt = conn.prepareStatement("SELECT ni.new_item_name,oni.quantity,ni.price from order_new_item oni,new_items ni "
           					+ "where ni.new_item_id=oni.new_item_id and ni.new_item_id=?");
               		stmt.setInt(1,rs2.getInt(1));
               		rs3 = stmt.executeQuery();
               		rs3.next();
               		newItem.setItemName(rs3.getString(1));
               		newItem.setQuantity(rs3.getInt(2));
               		newItem.setPrice(rs3.getInt(3));
               		newItem.setCatalogType(CatalogType.new_item);
               		/// get all items of new item
               		
               		
               	  stmt = conn.prepareStatement("select * from items where item_id in "
  	            		+ "(select catalog_item_id from new_item_spec where new_item_id =?)");	
               	  stmt.setInt(1, rs2.getInt(1));
               	  ResultSet rs5;
               	  rs5 = stmt.executeQuery();
               	  while(rs5.next()) {
               		ItemInList assemble=new ItemInList(); 
               		assemble.setItem_id(rs5.getInt(1));
               		assemble.setItemName(rs5.getString(2));
               		assemble.setCatalogType(CatalogType.valueOf(rs5.getString(4)));
               		assemble.setItemType(ItemType.valueOf(rs5.getString(5)));
               		assemble.setPrice(rs5.getInt(3));
               		  
               		stmt = conn.prepareStatement("select quantity from new_item_spec where new_item_id= ? "
      	            		+ " AND catalog_item_id = ?");	
               		stmt.setInt(1,rs2.getInt(1));
               		stmt.setInt(2,rs5.getInt(1));
               		ResultSet rs4;
               		rs4 = stmt.executeQuery();
               		rs4.next();
               		assemble.setQuantity(rs4.getInt(1));
               		newItem.addItem(assemble);
               	  }
               		///
               		itemList.add(newItem);
           		}
           		/////           	
           		order.setItems(itemList);
           		customerOrders.add(order);
           	}

		
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
	
	}

	/**
	 * Delete item from cart.
	 *
	 * @param username the username of the cart owner
	 * @param data the the item_id of the deleted item
	 */
	public void DeleteItemFromCart(String username, int data) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(
					"DELETE FROM cart_item WHERE item_id=? and cart_id =(select cart_id from carts WHERE user_id=? )");
			stmt.setInt(1, data);
			stmt.setString(2, username);
			stmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Gets notifications allocated to a username 
	 *
	 * @param username - the username who request his notifications
	 * @param notificationList the collection of notifications to be altered
	 */
	public void getNotification(String username, ArrayList<NotificationInTable> notificationList) {
		Server.Log("Database", "Executing getNotification");
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM notifications WHERE user_id= ?");
			stmt.setString(1, username);
		} catch (SQLException e1) {
			Server.Log("Database", "getNotification createStatement : FAILED");
			e1.printStackTrace();
		}
		ResultSet rs;
		try {
			rs = stmt.executeQuery();
			while (rs.next()) {
				notificationList.add(new NotificationInTable(rs.getInt(1), 
						rs.getString(3), rs.getString(4), rs.getString(5)));
			}
		} catch (Exception e) {
			Server.Log("Database", "getNotification ot an exception : FAILED");
			e.printStackTrace();
		}
		Server.Log("Database", "Executing getNotification: SUCCESS");
	}

	/**
	 * Sets a notification state into "read".
	 *
	 * @param username - the owner of the notification
	 * @param num the number of the notification
	 */
	public void UpdateNotification(String username, int num) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("UPDATE notifications SET status = 'read' WHERE notification_id = ? ");
			stmt.setInt(1, num);
			stmt.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Send Notification, Adds a new notification allocated to a user.
	 *
	 * @param notification - Instance of notification alert with all the data
	 */
	public void SendNotification(NotificationInTable notification) {
		Server.Log("Database", "Executing SendNotification");
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO notifications "
					+ "(user_id, from_role, content, status) VALUES "
					+ "(?, ?, ?, ?)");
		 	stmt.setString(1, notification.getTo());
		 	stmt.setString(2, notification.getFrom());
		 	stmt.setString(3, notification.getContent());
			stmt.setString(4, "unread");
		 	stmt.executeUpdate();
		} catch (SQLException e1) {
			Server.Log("Database", "Executing SendNotification: FAILED");
			e1.printStackTrace();
		}
		Server.Log("Database", "Executing SendNotification: SUCCESS");
	}

	/**
	 * End order.
	 *
	 * @param order_id the order id which to be ended
	 * @return the refund (if necessary)
	 */
	public int EndOrder(int order_id) {
		PreparedStatement OrderDetailsStmt = null;
		PreparedStatement SetStatusStmt = null;
		int refund = 0;
		ResultSet rs;
		try {
			OrderDetailsStmt = conn.prepareStatement("SELECT user_id, shipping_date,total_price FROM orders WHERE order_id =?");
			SetStatusStmt = conn.prepareStatement("UPDATE orders SET status = ? WHERE order_id = ?");
			OrderDetailsStmt.setInt(1, order_id);
			SetStatusStmt.setInt(2, order_id);
			SetStatusStmt.setString(1, "completed");
			rs = OrderDetailsStmt.executeQuery();
			if(rs.next())
			{
			
				String user_id = rs.getString(1);
				String currency[]=GetCurrency(user_id);
				int zCoin = Integer.valueOf(currency[4]);
				int orderCost = rs.getInt(3);
				java.sql.Timestamp  ts=  rs.getTimestamp(2);
				LocalDateTime requested = Utilities.GenericUtilties.Convert_LocalDate_To_SQLDate(ts);
				Duration difference = Duration.between(requested, LocalDateTime.now()); //requested - now
				if(difference.getSeconds() > 0 )
				{
					refund = orderCost;
					zCoin +=  orderCost;
				}
				UpdateZerliCoins(user_id, zCoin);
				SetStatusStmt.executeUpdate();
			}
			
			
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return refund;
	}

	/**
	 * Cancels an order.
	 *
	 * @param refundZerli the refund total
	 * @param orderID the order ID to be cancelled
	 */
	public void cancelOrder(Integer refundZerli, Integer orderID) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("UPDATE orders SET status = ?,"
					+ "refund_zerli = ? WHERE order_id = ? " );
			stmt.setString(1,OrderStatus.pending_cancel.toString());
			stmt.setInt(2,refundZerli);
			stmt.setInt(3,orderID);
           	stmt.executeUpdate(); 
			}
		catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Removes the 20% new customer bonus
	 *
	 * @param user_id the username of the customer
	 */
	public void resetNewCustomer(String user_id) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement
					("UPDATE customer_details "
					+ "SET new_customer = 0 "
					+ "WHERE user_id = ?");
			stmt.setString(1,user_id);
           	stmt.executeUpdate(); 
			}
		catch (SQLException e1) {
			e1.printStackTrace();
		}
	} 

	/**
	 * Update and Create a new custom item in the cart.
	 *
	 * @param username the customer who creates/updates the new list
	 * @param newItem the id of the custom newly created item
	 */
	public void UpdateNewItemInCart(String username,NewItem newItem) {
		int newItemId;
		PreparedStatement stmt;
		ResultSet rs;
		//1
		
		
		 try {
			 
			 	stmt = conn.prepareStatement("INSERT INTO new_items (new_item_name) VALUES (?)");
			 	stmt.setString(1, newItem.getItemName());
			 	stmt.executeUpdate();
			 	///
			 	stmt = conn.prepareStatement("SELECT LAST_INSERT_ID();");
			 	rs=stmt.executeQuery();
			 	rs.next();		
			 	newItemId=rs.getInt(1);
			 	///
			 	
			 	///
			 	for (ItemInList item : newItem.getAssemble()) {
			 		stmt = conn.prepareStatement("INSERT INTO new_item_spec values(? , ? ,?) ");
			 		stmt.setInt(1, newItemId);
			 		stmt.setInt(2,item.getItem_id());
			 		stmt.setInt(3,item.getQuantity());
			 		stmt.executeUpdate();
				}
			 	///
			 	stmt = conn.prepareStatement("INSERT INTO cart_new_item values((select cart_id from carts where user_id=?), ?, ?)");
			 	stmt.setString(1, username);
			 	stmt.setInt(2, newItemId);
			 	stmt.setInt(3, newItem.getQuantity());
		 		stmt.executeUpdate();

		 
		 }
		 catch (Exception e) {
	            e.printStackTrace();
	        }		
	}

	/**
	 * Delete item from new item list.
	 *
	 * @param new_item the new custom item id
	 * @param catalog_item the actual catalog item id to be removed
	 */
	public void DeleteItemFromNewItemList(int new_item,int catalog_item) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("DELETE FROM new_item_spec WHERE new_item_id = ? AND catalog_item_id =? " );
			stmt.setInt(1, new_item);
			stmt.setInt(2, catalog_item);
           	stmt.executeUpdate(); 	
           
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Deletes a new item from cart.
	 *
	 * @param requestee the cart owner
	 * @param data the item id of the item to be removed
	 */
	public void DeleteNewItemFromCart(String requestee, int data) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("DELETE FROM cart_new_item WHERE new_item_id=? AND cart_id = (SELECT cart_id FROM carts Where user_id=?)" );
			stmt.setInt(1, data);
			stmt.setString(2, requestee);
           	stmt.executeUpdate(); 
           	/////
           	stmt = conn.prepareStatement("DELETE FROM new_items WHERE new_item_id = ?" );
			stmt.setInt(1, data);
           	stmt.executeUpdate(); 
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Delete all new (custom) items from cart.
	 *
	 * @param requestee the cart owner
	 * @param data the item id to be removed
	 */
	public void DeleteAllnewItemFromCart(String requestee, int data) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("DELETE FROM cart_new_item WHERE cart_id = (SELECT cart_id FROM carts Where user_id=?)" );
			stmt.setString(1, requestee);
           	stmt.executeUpdate();
           	////
           	stmt = conn.prepareStatement("DELETE FROM new_item_spec WHERE new_item_id = ?" );
			stmt.setInt(1, data);
           	stmt.executeUpdate(); 	
           	/////
           	stmt = conn.prepareStatement("DELETE FROM new_items WHERE new_item_id = ?" );
			stmt.setInt(1, data);
           	stmt.executeUpdate(); 
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
	}
	

	/**
	 * Updates a catalog item properties.
	 *
	 * @param The newly altered item
	 */
	public void UpdateItem(Item data) {
		Server.Log("Database", "Executing UpdateItem");
		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("UPDATE Items SET name=?,price=?, item_type=?,catalog_type=?,color=?,is_sale=?,salePrice=? WHERE item_id=?");
			stmt.setString(1, data.getName());
			stmt.setInt(2, data.getPrice());
			stmt.setString(3, data.getItemType().toString());
			stmt.setString(4, data.getCatalogType().toString());
			stmt.setString(5, data.getColor().toString());
			stmt.setInt(6, data.isOnSale() ? 1 : 0);
			stmt.setInt(7, data.getSalePrice());
			stmt.setInt(8, data.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {e.printStackTrace(); 		Server.Log("Database", "Executing UpdateItem: FAILED");}		

	}
	
	/**
	 * Insert an item to a catalog.
	 *
	 * @param data - an item entity data
	 */
	public void InsertItem(Item data)
	{
		Server.Log("Database", "Executing InsertItem");
		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("INSERT INTO Items (name,price,catalog_type,item_type,color,image,is_sale,salePrice) VALUES (?,?,?,?,?,?,?,?)");
			stmt.setString(1, data.getName());
			stmt.setInt(2, data.getPrice());
			stmt.setString(3, data.getCatalogType().toString());
			stmt.setString(4, data.getItemType().toString());
			stmt.setString(5, data.getColor().toString());
			if(data.getImage() != null)
				stmt.setBlob(6, new ByteArrayInputStream(data.getImage()));
			else
				stmt.setNull(6,Types.BLOB);
			stmt.setInt(7, data.isOnSale() ? 1 : 0);
			stmt.setInt(8, data.getSalePrice());
			stmt.executeUpdate();
		} catch (SQLException e) {e.printStackTrace();		
		Server.Log("Database", "Executing InsertItem: FAILED");
		}
	}

/**
 * * Gets a report **.
 *
	 * @param reportType the report type [income,order,quarterly,ceo]
	 * @param isMonthly - true: monthly, false: quarterly
	 * @param reportDate the report date (month or quarter)
	 * @return the pdf file containing the actual report
 */
	public byte[] GetReport(ReportType reportType, boolean isMonthly, String requester, Date reportDate) {
		Server.Log("Database", "Executing GetReport");
		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("SELECT report FROM reports WHERE date = ? AND is_monthly = ? AND branch = (SELECT branch_name FROM branch_employees WHERE user_id = ?) AND reportType = ?");
			stmt.setDate(1, reportDate);
			stmt.setInt(2, isMonthly? 1 : 0);
			stmt.setString(3, requester);
			stmt.setString(4,reportType.toString());
			ResultSet res = stmt.executeQuery();
			if(res.next())
			{
				try {
					return res.getBlob(1).getBinaryStream().readAllBytes();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {e.printStackTrace();	
		Server.Log("Database", "Executing GetReport: FAILED");
		}
		return null;
	}
	
	/**
	 * Gets a report of a branch.
	 *
	 * @param reportType the report type [income,order,quarterly,ceo]
	 * @param isMonthly - true: monthly, false: quarterly
	 * @param branch the branch to report is made on
	 * @param reportDate the report date (month or quarter)
	 * @return the pdf file containing the actual report
	 */
	public byte[] GetReportOfBranch(ReportType reportType, boolean isMonthly,String branch, Date reportDate) {
		Server.Log("Database", "Executing GetReportOfBranch");
		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("SELECT report FROM reports WHERE date = ? AND is_monthly = ? AND branch = ? AND reportType = ?");
			stmt.setDate(1, reportDate);
			stmt.setInt(2, isMonthly? 1 : 0);
			stmt.setString(3, branch);
			stmt.setString(4,reportType.toString());
			ResultSet res = stmt.executeQuery();
			if(res.next())
			{
				try {
					return res.getBlob(1).getBinaryStream().readAllBytes();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {e.printStackTrace();	
		Server.Log("Database", "Executing GetReportOfBranch: FAILED");
		}
		return null;
	}
	
	/**
	 * Insert a report.
	 *
	 * @param reportType the report type [income,order,quarterly,ceo]
	 * @param isMonthly - true: monthly, false: quarterly
	 * @param branch the branch to report is made on
	 * @param reportDate the report date (month or quarter)
	 * @param pdf the pdf file containing the actual report
	 */
	public void InsertReport(ReportType reportType, boolean isMonthly, String branch, Date reportDate,byte[] pdf) {
		Server.Log("Database", "Executing InsertReport");
		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("INSERT INTO reports (reportType,report,branch,is_monthly,date) VALUES (?,?,?,?,?)");
			stmt.setString(1, reportType.toString());
			stmt.setBlob(2, new ByteArrayInputStream(pdf));
			stmt.setString(3, branch);
			stmt.setInt(4, isMonthly? 1:0);
			stmt.setDate(5, reportDate);
			stmt.executeUpdate();
		} catch (SQLException e) {e.printStackTrace();	
		Server.Log("Database", "Executing InsertReport: FAILED");
		}
	}
	
	/**
	 * Gets the daily financial income for a branch.
	 *
	 * @param branch the branch to be looked on
	 * @param date the date to be calculated
	 * @return an income array where [0]: - value from orders, [1]: value from refunds, [2]: [1] + [2]
	 */
	public Integer[] GetDailyFinancialIncomeForBranch(String branch,Date date) //Gross, Refunds
	{
		//Server.Log("Database", "Executing GetDailyFinancialIncomeForBranch");
		Integer[] records = new Integer[3];
		records[0] = 0;
		records[1] = 0;
		PreparedStatement stmt;
		ResultSet rs;
		try 
		{
			//NET = [Orders Completed]COMPLETED + [Order Cancelled](CANCEL_PRICE - CANCEL_REFUNDS)
			stmt = conn.prepareStatement("SELECT SUM(total_price) FROM orders WHERE status = 'completed' AND branch_name = ? AND DATE(order_date) = ?");
			stmt.setString(1, branch);
			stmt.setDate(2,date);
			rs = stmt.executeQuery();
			while (rs.next()) {
				records[0] = rs.getInt(1);
			}
			stmt = conn.prepareStatement("SELECT SUM(total_price)-SUM(refund_zerli) FROM orders WHERE branch_name = ? AND status = 'canceled' AND DATE(order_date) = ?");
			stmt.setString(1, branch);
			stmt.setDate(2,date);
			rs = stmt.executeQuery();
			while (rs.next()) {
				records[1] = rs.getInt(1);
			}
			records[2] = records[1] + records[0];
			return records;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing GetDailyFinancialIncomeForBranch: FAILED");
			return null;
		}
	}

	/**
	 * Gets the count of orders containing a certain item within a period
	 *
	 * @param t the type of the item to be looked upon
	 * @param a the start date
	 * @param b the end date
	 * @param branch the branch to be looked on
	 * @return the count of orders containing the item within a and b
	 */
	public Integer GetOrderCountWithItemWithinPeriod(ItemType t,Date a,Date b,String branch)
	{
		//Server.Log("Database", "Executing GetOrderCountWithItemWithinPeriod");
		PreparedStatement stmt;
		try 
		{
			
			/*stmt = conn.prepareStatement("SELECT COUNT(distinct order_id) FROM order_item WHERE item_id IN (SELECT item_id FROM items WHERE item_type = ?) AND order_id IN (SELECT order_id FROM orders WHERE DATE(order_date) BETWEEN CAST('2022-04-01' AS DATE) AND CAST('2022-04-31' AS DATE))");*/
			stmt = conn.prepareStatement("SELECT COUNT(order_id) FROM order_item WHERE item_id IN (SELECT item_id FROM items WHERE item_type = ?) AND order_id IN (SELECT order_id FROM orders WHERE DATE(order_date) BETWEEN ? AND ? AND branch_name = ?)");
			stmt.setString(1, t.toString());
			stmt.setDate(2,a);
			stmt.setDate(3, b);
			stmt.setString(4, branch);
			ResultSet res = stmt.executeQuery();
			if(res.next())
			{
				return res.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//Server.Log("Database", "Executing GetOrderCountWithItemWithinPeriod: FAILED");
		}
		return 0;
	}


	/**
	 * Checks whether a report exists for that parameters.
	 *
	 * @param t the report type
	 * @param isMonthly - true if the reports is monthly, false if it is quarterly report
	 * @param branch the branch that the report was made on
	 * @param reportDate the report date (if quarterly then the month part is the quarter)
	 * @return true, if exists
	 */
	public boolean ReportExists(ReportType t, boolean isMonthly, String branch, Date reportDate) {
		//Server.Log("Database", "Executing ReportExists");
		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("SELECT reports_id FROM reports WHERE date = ? AND is_monthly = ? AND branch = ? AND reportType = ?");
			stmt.setDate(1, reportDate);
			stmt.setInt(2, isMonthly? 1 : 0);
			stmt.setString(3, branch);
			stmt.setString(4, t.toString());
			ResultSet res = stmt.executeQuery();
			if(res.next())
			{
				return true;
			}
		} catch (SQLException e) {e.printStackTrace();	
		//Server.Log("Database", "Executing ReportExists: FAILED");
		}
		return false;
	}
	
	/**
	 * Gets the complaint count of branch in a certain date
	 *
	 * @param branch the branch to seek compaints on
	 * @param a - the date to be looked on
	 * @return the count of complaints
	 */
	public Integer GetComplaintCountOfBranch(String branch,Date a)
	{			
		//Server.Log("Database", "Executing GetComplaintCountOfBranch");
		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = conn.prepareStatement("SELECT COUNT(complaint_id) FROM complaints WHERE branch = ? AND DATE(complain_time) = ?");
			stmt.setString(1, branch);
			stmt.setDate(2,a);
			//stmt.setDate(3, b);
			rs=stmt.executeQuery();
			while(rs.next())
			{
				return rs.getInt(1);
			}
		}catch (SQLException e) {
			e.printStackTrace();
			//Server.Log("Database", "Executing GetComplaintCountOfBranch: FAILED");
		}		
		throw new RuntimeException("SQL Error in complaints count");
	}
	
	/**
	 * Gets the complaints of a certain handler in status pending.
	 *
	 * @param complaints the complaints collection to be altered
	 * @param handler the customer service employee who took the complaint
	 */
	public void GetComplaints(ArrayList<Complaint> complaints,String handler)
	{
		Server.Log("Database", "Executing GetComplaints");
		PreparedStatement stmt;
		ResultSet rs;
		try
		{
			stmt = conn.prepareStatement("SELECT * FROM complaints WHERE response = 'pending' AND handler = ?");
			stmt.setString(1, handler);
			rs=stmt.executeQuery();
			while(rs.next())
			{
				Complaint newComplaint = new Complaint();
				newComplaint.setComplaint_id(rs.getInt(1));
				newComplaint.setUser_id(rs.getString(2));
				newComplaint.setComplain_text(rs.getString(3));
				newComplaint.setComplain_time(rs.getTimestamp(5));
				newComplaint.setBranch(rs.getString(7));
				newComplaint.setIsNotNotified(rs.getInt(9));
				complaints.add(newComplaint);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();		
			Server.Log("Database", "Executing GetComplaints: FAILED");
		}
		
	}
	
	/**
	 * Creates a complaint.
	 *
	 * @param complaint the complaint to be created
	 * @param handler the handler of the report (customer service employee)
	 * @return status message
	 */
	public String MakeComplaint(Complaint complaint,String handler)
	{
		Server.Log("Database", "Executing MakeComplaint");
		PreparedStatement stmt;
		PreparedStatement stmt2;
		ResultSet rs;
		try
		{
			stmt2 = conn.prepareStatement("SELECT user_id FROM user_details WHERE id =? AND role ='customer'");
			stmt2.setString(1, complaint.getIDnumber());
			rs=stmt2.executeQuery();
			if(rs.next())
			{
				stmt = conn.prepareStatement("INSERT INTO complaints (user_id,complain_text,complain_time,response,branch,handler) VALUES (?,?,?,?,?,?)");
				stmt.setString(1, rs.getString(1));
				stmt.setString(2, complaint.getComplain_text());
				stmt.setTimestamp(3, complaint.getComplain_time() );
				stmt.setString(4, "pending");
				stmt.setString(5, complaint.getBranch());
				stmt.setString(6, handler);
				stmt.executeUpdate();
				return "done";
			}
			return "No such user";
		} catch (SQLException e) {e.printStackTrace();}		
		Server.Log("Database", "Executing MakeComplaint: FAILED");
		return "sql error";
		
	}
	
	/**
	 * Sets the complaint as already alerted, therefore 24hours have passed.
	 *
	 * @param the id of the complaint
	 */
	public void SetComplaintAlerted(int id)
	{
		Server.Log("Database", "Executing SetComplaintAlerted");
		PreparedStatement stmt;
		try
		{
			stmt = conn.prepareStatement("UPDATE complaints SET open = 0 WHERE complaint_id = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			Server.Log("Database", "Executing SetComplaintAlerted: FAILED");
		}
	}
	
	/**
	 * Issues a response to a complaint.
	 *
	 * @param Complaint entity with the response
	 */
	public void ComplaintResponse(Complaint complaint)
	{
		PreparedStatement stmt;
		try
		{
			stmt = conn.prepareStatement("UPDATE complaints SET response = 'done', answer_text = ?, refund = ?, open = 0 WHERE complaint_id =?");
			stmt.setString(1, complaint.getAnswer_text());
			stmt.setInt(2, complaint.getRefund());
			stmt.setInt(3, complaint.getComplaint_id());
			if(complaint.getRefund() != 0)
			{
				String currency[]=GetCurrency(complaint.getUser_id());
				int zCoin = Integer.valueOf(currency[4]);
				UpdateZerliCoins(complaint.getUser_id(), zCoin+complaint.getRefund());
			}
			stmt.executeUpdate();
		
		} catch (SQLException e) {e.printStackTrace();}		
		Server.Log("Database", "Executing MakeComplaint: FAILED");
	}
	
	/**
	 * Gets a customer who has not been registered yet
	 *
	 * @param ID the id of the customer
	 * @return the pending client info
	 */
	public PendingClientInfo GetPendingClient(String ID) {
		Server.Log("Database", "Executing GetPendingClient");
		
		PreparedStatement stmt;
		ResultSet rs;
		PendingClientInfo clientInfo = new PendingClientInfo();
		try 
		{
			stmt = conn.prepareStatement("SELECT * FROM user_details WHERE id = ?");
			stmt.setString(1,ID);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				stmt = conn.prepareStatement("SELECT * FROM user_details WHERE id =? AND role = 'customer' "
						+ "AND NOT EXISTS (SELECT * FROM customer_details WHERE user_id = "
						+ "(SELECT user_id FROM user_details WHERE id =? AND role = 'customer'))");
				stmt.setString(1, ID);
				stmt.setString(2, ID);
				rs = stmt.executeQuery();
				
				if(rs.next() == false) {
					return null;
				}
				else {
					clientInfo.setUserID(rs.getString(1));
					clientInfo.setFirstName(rs.getString(2));
					clientInfo.setLastName(rs.getString(3));
					clientInfo.setID(rs.getString(4));
					clientInfo.setEmail(rs.getString(5));
					clientInfo.setPhone(rs.getString(6));
				}
			}
			else
			{
				clientInfo.setUserID("-1");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing GetPendingClient: FAILED");
		}
		
		Server.Log("Database", "Executing GetPendingClient: SUCCESS");
		return clientInfo;
		
	}

	/**
	 * Activates a client be registering it.
	 *
	 * @param clientInfo the client info to be registered
	 * @param userID the user ID of the customer who seeks registration
	 */
	public void ActivateClient(PendingClientInfo clientInfo, String userID) {
		Server.Log("Database", "Executing ActivateClient");
		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement("INSERT INTO customer_details "
					+ "VALUES (?,?,?,?,?,0,1)");
			stmt.setString(1, userID);
			stmt.setString(2, clientInfo.getStringCreditPhrases());
			stmt.setString(3, clientInfo.getCVV());
			stmt.setString(4, clientInfo.getExpirationMonth());
			stmt.setString(5, clientInfo.getExpirationYear());
			stmt.executeUpdate();
			
			stmt = conn.prepareStatement("INSERT INTO login_details VALUES (?,?, 'active', 0)");
			stmt.setString(1, userID);
			stmt.setString(2, clientInfo.getPassword());
			stmt.executeUpdate();
			
			stmt = conn.prepareStatement("INSERT INTO carts (user_id) VALUES (?)");
			stmt.setString(1, userID);
			stmt.executeUpdate();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing ActivateClient: FAILED");
		}
		Server.Log("Database", "Executing ActivateClient: SUCCESS");
		
	}



	/**
	 * Gets all of the customers information.
	 *
	 * @param customers the customers collection to be altered
	 */
	public void getAllCustomersInfo(ArrayList<AccountInfo> customers) {
		Server.Log("Database", "Executing getAllCustomersInfo");
		PreparedStatement stmt;
		ResultSet rs;
		try 
		{
			stmt = conn.prepareStatement("SELECT ud.user_id , ud.first_name , ud.last_name , ud.id , ld.access "
					+ "FROM user_details ud,login_details ld "
					+ "WHERE ud.user_id = ld.user_id AND ud.role = 'customer'");
			rs = stmt.executeQuery();
			while (rs.next()) {
				AccountInfo accountinfo = new AccountInfo(rs.getString(1), rs.getString(2), 
						rs.getString(3), rs.getString(4));
				accountinfo.setAccess(Access.valueOf(rs.getString(5)));
				customers.add(accountinfo);
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing getAllCustomersInfo: FAILED");
		}
		Server.Log("Database", "Executing getAllCustomersInfo: SUCCESS");
		
	}

	/**
	 * Gets all of the employees info.
	 *
	 * @param employees the employees collection to be altered
	 * @param managerUserID the user id of the manager who asks the information
	 */
	public void getAllEmployeesInfo(ArrayList<AccountInfo> employees,String managerUserID) {
		Server.Log("Database", "Executing getAllEmployeesInfo");
		PreparedStatement stmt;
		ResultSet rs;
		try 
		{
			
			stmt = conn.prepareStatement("SELECT ud.user_id , ud.first_name , ud.last_name , ud.id , ud.role "
					+ "FROM user_details ud, login_details ld , branch_employees be "
					+ "WHERE ud.role != 'customer' AND ud.role != 'manager' AND ud.role != 'ceo' "
					+ "AND ud.user_id = ld.user_id AND ud.user_id = be.user_id "
					+ "AND be.branch_name = (SELECT branch_name FROM branch_employees WHERE user_id = ?)");
			stmt.setString(1, managerUserID);
			rs = stmt.executeQuery();
			while (rs.next()) {
				AccountInfo accountinfo = new AccountInfo(rs.getString(1), rs.getString(2), 
						rs.getString(3), rs.getString(4));
				accountinfo.setRole(Roles.valueOf(rs.getString(5)));
				employees.add(accountinfo);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing getAllEmployeesInfo: FAILED");
		}
		Server.Log("Database", "Executing getAllEmployeesInfo: SUCCESS");
	}

	/**
	 * Update a customer's account access level.
	 *
	 * @param userID the user ID of the customer
	 * @param access the access level to be granted
	 */
	public void UpdateAccountAccess(String userID, String access) {
		Server.Log("Database", "Executing UpdateAccountAccess");
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("UPDATE login_details SET access=? WHERE user_id=?");
			stmt.setString(1, access);
			stmt.setString(2, userID);
			stmt.executeUpdate();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing UpdateAccountAccess: FAILED");
		}
		Server.Log("Database", "Executing UpdateAccountAccess: SUCCESS");
	}



	/**
	 * Update an employee's role.
	 *
	 * @param userID the user ID of the employee
	 * @param role the new role to be designated
	 */
	public void UpdateEmployeeRole(String userID, String role) {
		Server.Log("Database", "Executing UpdateEmployeeRole");
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement("UPDATE user_details SET role=? WHERE user_id=?");
			stmt.setString(1, role);
			stmt.setString(2, userID);
			stmt.executeUpdate();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing UpdateEmployeeRole: FAILED");
		}
		Server.Log("Database", "Executing UpdateEmployeeRole: SUCCESS");
		
	}



	/**
	 * Saves a survey.
	 *
	 * @param survey - the survey to be saved
	 */
	public void SaveSurvey(Survey survey) {
		Server.Log("Database", "Executing Save Survey");
		PreparedStatement stmt;
		ResultSet rs;
		try 
		{
			stmt = conn.prepareStatement("insert into midproject.surveys (survey_content,q1,q2,q3,q4,q5,q6) values(?,?,?,?,?,?,?)");
			stmt.setString(1, survey.getContent());
			stmt.setString(2, survey.getQuestions()[0]);
			stmt.setString(3, survey.getQuestions()[1]);
			stmt.setString(4, survey.getQuestions()[2]);
			stmt.setString(5, survey.getQuestions()[3]);
			stmt.setString(6, survey.getQuestions()[4]);
			stmt.setString(7, survey.getQuestions()[5]);
			stmt.executeUpdate();
		} catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing Save Survey: FAILED");
		}		
		Server.Log("Database", "Executing Save Survey: SUCCESS");
	}



	/**
	 * Gets the surveys names.
	 *
	 * @param list - the list to be altered with the survey names
	 */
	public void GetSurveysNames(ArrayList<String> list) {
		Server.Log("Database", "Executing Get Surveys Names");
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			stmt = conn.prepareStatement("SELECT survey_id,survey_content FROM midproject.surveys WHERE ready = 0");
           	rs = stmt.executeQuery();
           	while(rs.next()) {
           		String string=rs.getString(1)+" "+rs.getString(2);
           		list.add(string);
           	}
		}catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing Get Surveys Names: FAILED");
		}		
		Server.Log("Database", "Executing Get Surveys Names: SUCCESS");
	}

	/**
	 * Gets a survey.
	 *
	 * @param the survey to be loaded with the data
	 */
	public void GetSurvey(Survey survey) {
		Server.Log("Database", "Executing Get Survey");
		PreparedStatement stmt;
		ResultSet rs;
		try 
		{
			stmt = conn.prepareStatement("SELECT q1,q2,q3,q4,q5,q6,survey_content FROM midproject.surveys where survey_id =?");
			stmt.setInt(1,survey.getId());
			rs = stmt.executeQuery();
			rs.next();
			survey.getQuestions()[0]=(rs.getString(1));
			survey.getQuestions()[1]=(rs.getString(2));
			survey.getQuestions()[2]=(rs.getString(3));
			survey.getQuestions()[3]=(rs.getString(4));
			survey.getQuestions()[4]=(rs.getString(5));
			survey.getQuestions()[5]=(rs.getString(6));
			survey.setContent((rs.getString(7)));
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing Get Survey: FAILED");
		}		
		Server.Log("Database", "Executing Get Survey: SUCCESS");
	}

	/**
	 * Save survey answers.
	 *
	 * @param survey the survey with the answers
	 */
	public void SaveSurveyAnswers(Survey survey) {
		Server.Log("Database", "Executing Save Survey Answers");
		PreparedStatement stmt;
		try 
		{
			
				stmt = conn.prepareStatement("insert into midproject.surveys_questions ( survey_id,survey_content,answer_for_question_1,answer_for_question_2,answer_for_question_3,answer_for_question_4,answer_for_question_5,answer_for_question_6) values(?,?,?,?,?,?,?,?)");
				
				stmt.setInt(1, survey.getId());
				stmt.setString(2, survey.getContent());
				stmt.setInt(3, survey.getAnswers()[0]);
				stmt.setInt(4, survey.getAnswers()[1]);
				stmt.setInt(5, survey.getAnswers()[2]);
				stmt.setInt(6, survey.getAnswers()[3]);
				stmt.setInt(7, survey.getAnswers()[4]);
				stmt.setInt(8, survey.getAnswers()[5]);
				stmt.executeUpdate();
	
		} catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "Executing Save Survey Answers: FAILED");
		}		
		Server.Log("Database", "Executing Save Survey Answers: SUCCESS");
	}

	/**
	 * Gets the survey histogram.
	 *
	 * @param survey the survey of the histogram
	 * @param listOfSurveyAnswers the list of survey answers
	 */
	public void GetSurveyHistogram(Survey survey, ArrayList<int[]> listOfSurveyAnswers) 
	{
		
		Server.Log("Database", "Executing Get Survey Histogram");
		PreparedStatement stmt;
		ResultSet rs;
		try 
		{
			stmt = conn.prepareStatement("SELECT survey_content,answer_for_question_1,answer_for_question_2,answer_for_question_3,answer_for_question_4,answer_for_question_5,answer_for_question_6 FROM midproject.surveys_questions where survey_id=?");
			stmt.setInt(1,survey.getId());
			rs = stmt.executeQuery();
			while(rs.next())
			{
				survey.setContent(rs.getString(1));
				listOfSurveyAnswers.get(0)[rs.getInt(2)-1]++;
				listOfSurveyAnswers.get(1)[rs.getInt(3)-1]++;
				listOfSurveyAnswers.get(2)[rs.getInt(4)-1]++;
				listOfSurveyAnswers.get(3)[rs.getInt(5)-1]++;
				listOfSurveyAnswers.get(4)[rs.getInt(6)-1]++;
				listOfSurveyAnswers.get(5)[rs.getInt(7)-1]++;
			}
		} catch (SQLException e) 
		{
			e.printStackTrace(); 
			Server.Log("Database", "Executing Get Survey Histogram: FAILED");
		}		
		Server.Log("Database", "Executing Get Survey Histogram: SUCCESS");
	}



	/**
	 * Save PDF.
	 *
	 * @param userName the expert's username
	 * @param survey the survey that the pdf report is allocated to
	 * @param the pdfs byte data
	 */
	public void SavePDF(String userName, Survey survey,ArrayList<byte[]> Data) {
		Server.Log("Database", "Executing Save PDF");
		PreparedStatement stmt;
		try 
		{
			//Set PDF survey result
			Blob blob1 = new javax.sql.rowset.serial.SerialBlob(Data.get(1));//expertPDF
			Blob blob2 = new javax.sql.rowset.serial.SerialBlob(Data.get(0));//histoframPDF
			stmt = conn.prepareStatement("insert into midproject.pdf_from_expert (survey_id,expert_name,survey_content,pdf_file_from_expert,pdf_with_survey_answers) values(?,?,?,?,?)");
			stmt.setInt(1, survey.getId());
			stmt.setString(2,userName);
			stmt.setString(3,survey.getContent());
			stmt.setBlob(4, blob1);
			stmt.setBlob(5, blob2);
			stmt.executeUpdate();
			
			//Set ready to original survey
			stmt = conn.prepareStatement("UPDATE surveys SET ready = 1 WHERE survey_id = ?");
			stmt.setInt(1, survey.getId());
			stmt.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Server.Log("Database", "Executing Save PDF: FAILED");		
		}
		Server.Log("Database", "Executing Save PDF: SUCCESS");
	}

	/**
	 * Gets the customer information
	 *
	 * @param userID the customer user id
	 * @return the customers info
	 */
	public CustomerInfo GetCustomerInfo(String userID) {
		Server.Log("Database", "Executing GetCustomerInfo");
		CustomerInfo customer = new CustomerInfo();
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			stmt = conn.prepareStatement("SELECT ud.*,cd.* FROM user_details ud, customer_details cd "
					+ "WHERE cd.user_id = ud.user_id AND cd.user_id = ?");
			stmt.setString(1, userID);
			rs = stmt.executeQuery();
			while (rs.next()) {
				customer.setFirstName(rs.getString(2));
				customer.setLastName(rs.getString(3));
				customer.setID(rs.getString(4));
				customer.setEmail(rs.getString(5));
				customer.setPhone(rs.getString(6));
				customer.setCreditCard(rs.getString(9));
				customer.setCvv(rs.getString(10));
				customer.setExpMonth(rs.getString(11));
				customer.setExpYear(rs.getString(12));
				customer.setZerliCoins(String.valueOf(rs.getInt(13)));
				int newCustomer = rs.getInt(14);
				customer.setNewCustomer(newCustomer==1?true:false);
			}
		}catch (Exception e) {
			Server.Log("Database", "Executing GetCustomerInfo: FAILED");
			e.printStackTrace();
		}
		Server.Log("Database", "Executing GetCustomerInfo: SUCCESS");
		return customer;
	}

	/**
	 * Gets the survey report in pdf format.
	 *
	 * @param content the survey name
	 * @return the array list - ArrayList of the pdfs (result of survey, expert report)f
	 */
	public ArrayList<byte[]> GetSurveyReport(String content)
	{
		Server.Log("Database", " GetSurveyReport");
		ArrayList<byte[]> pdfs = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			stmt = conn.prepareStatement("SELECT pdf_file_from_expert,pdf_with_survey_answers FROM midproject.pdf_from_expert WHERE survey_content = ?");
           	stmt.setString(1, content);
			rs = stmt.executeQuery();
           	while(rs.next()) {
           		try
           		{
           			pdfs.add(rs.getBlob(1).getBinaryStream().readAllBytes());
           			pdfs.add(rs.getBlob(2).getBinaryStream().readAllBytes());
           		}
           		catch(Exception e)
           		{
           			e.printStackTrace();
           		}
           	}
		}catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "GetSurveyReport: FAILED");
		}		
		Server.Log("Database", "GetSurveyReport: SUCCESS");
		
		return pdfs;
	}
	
	/**
	 * Gets the ready surveys (that have an expert report on them).
	 *
	 * @return ArrayList containing the ready survey elements
	 */
	public ArrayList<SurveyResult> GetReadySurveys() {
		Server.Log("Database", " GetReadySurveys");
		ArrayList<SurveyResult> surveyData = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs;
		try {
			stmt = conn.prepareStatement("SELECT survey_id,survey_content,pdf_file_from_expert,pdf_with_survey_answers FROM midproject.pdf_from_expert");
           	rs = stmt.executeQuery();
           	while(rs.next()) {
           		try {
					surveyData.add(new SurveyResult(rs.getInt(1),rs.getString(2),rs.getBlob(3).getBinaryStream().readAllBytes(),rs.getBlob(4).getBinaryStream().readAllBytes()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           	}
		}catch (SQLException e) 
		{
			e.printStackTrace();
			Server.Log("Database", "GetReadySurveys: FAILED");
		}		
		Server.Log("Database", "GetReadySurveys: SUCCESS");
		
		return surveyData;
	}
	
	
}