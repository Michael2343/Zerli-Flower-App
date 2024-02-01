/*
 * 
 */
package ProtocolHandler;
// TODO: Auto-generated Javadoc: Missing a few!

/**
 * A RequestType is an enumeration of all possible requests
 * that can come from the client.
 */
public enum RequestType {
	
	/**  Updates order data. */
	UpdateOrder,
	
	/**  Pings - echoes the client transaction used for connection debugging. */
	Ping,
	
	/**  The Authenticate user is used for authenticating user credentials and responding with the correct access level. */
	AuthenticateUser,
	
	/**  The SetLogout is used for safely disconnecting a user. */
	SetLogOut, 
	
	/** The GetCatalog command is used for
	 *  sending a specific catalog
	 * . */
	GetCatalog,
	
	/**  Is used for getting a customer saved buying cart . */
	GetCart,
	
	/** The Get branches is used to retrieve all
	 * available branches. */
	GetBranches,
	
	/**  The Get user currency is used to retrieve a customer's Z-Coins count. */
	GetUserCurrency, 
	
	/**  The Update zerli coins is used to update Z-Coins count of a customer. */
	UpdateZerliCoins,
	
	/**  The Insert order is used to register a new order in the system. */
	InsertOrder,
	
	/**  The Get branch is used to get a specific branch. */
	GetBranch,
	
	/**  The Get orders by branch is used to retrieve all orders of a specific branch. */
	GetOrdersByBranch,
	
	/**  The Get orders by username is used to retrieve all orders of a specific customer. */
	GetOrdersByUsername,
	
	/**  The Get items of order is used to retrieve all the items that were ordered in a specific order. */
	GetItemsOfOrder,
	
	/**  The Confirm order is used to alter an order status to confirmed. */
	ConfirmOrder,
	
	/** The Add to cart is used to update a customers cart
	 * with the recent items. */
	AddToCart,
	
	/** The Get all customer orders is used to retrieve
	 * all of the orders that were made by a customer. */
	GetAllCustomerOrders,
	
	/** The Delete item from cart is used to remove an item from
	 * the saved cart. */
	DeleteItemFromCart,
	
	/**  The Get notification is used to retrieve all notification of a certain user. */
	GetNotification,
	
	/**  The Update notification is used to update a notification from read/unread. */
	UpdateNotification,
	
	/**  The End order is used to complete an order upon delivery. */
	EndOrder,
	
	/**  The Cancel order is used to cancel an order. */
	CancelOrder,
	
	/**  The Reset new customer is used to reset a new customers 20% coupon. */
	ResetNewCustomer,
	
	/** The Update new item in cart command is used to update a cart with an item */
	UpdateNewItemInCart, 
	
	/** The Delete item from new item list command */
	DeleteItemFromNewItemList, 
	
	/** The Delete new item from cart is used to delete item from cart. */
	DeleteNewItemFromCart, 
	
	/** The Delete all new item from cart is used to delete all the items from cart. */
	DeleteAllnewItemFromCart,
	
	/**  The Remove item by ID is used to remove an item from the catalog using its ID. */
	RemoveItemByID,
	
	/**  The Get item by ID is used to retrieve an Item object using its ID. */
	GetItemByID,
	
	/**  The Update item is used to update a catalog item data using its ID. */
	UpdateItem,
	
	/**  The Add item is used to add a whole new item to a specific catalog. */
	AddItem,
	
	/** The Get complaints is used to get all complaints. */
	GetComplaints,
	
	/** The Complaint response command is used to store a response for a command. */
	ComplaintResponse,
	
	/** The Set complaint alerted is used to set a complaint as already notified */
	SetComplaintAlerted,
	
	/** The Make complaint is used to create a complaint */
	MakeComplaint,
	
	/** The Get pending client is used to get a pending client */
	GetPendingClient,
	
	/** The Register client is used to register a client's data */
	RegisterClient,
	
	/** The Get all customers info is used to get a customers information (details..)*/
	GetAllCustomersInfo,
	
	/** The Get all employees info is used to get an employee full information */
	GetAllEmployeesInfo,
	
	/** The Update account access is used to alter an account's access level */
	UpdateAccountAccess,
	
	/** The Update employee role is used to update employee role */
	UpdateEmployeeRole,
	
	/** The Save survey is used to save a survey pattern and questions */
	SaveSurvey,
	
	/** The Get surveys names is used to get the survey names */
	GetSurveysNames,
	
	/** The Get survey is used to get a survey entity */
	GetSurvey,
	
	/** The Save survey answers is used to store a survey answer */
	SaveSurveyAnswers,
	
	/** The Get survey histogram is used to get a survey in histogram form */
	GetSurveyHistogram,
	
	/** The Save PDF is used to store the survey expert report as pdf */
	SavePDF,
	/** The Get report is used to get various reports. */
	GetReport,
	
	/** The Get customer info i used to get the customer full information */
	GetCustomerInfo,
	
	/** The Send notification is used to send a notificaton */
	SendNotification,
	
	/** The Get report by branch is used to get a report by branch */
	GetReportByBranch,
	
	/** The Get ready surveys is used to get all surveys which have expert report loaded */
	GetReadySurveys,
}
