package ProtocolHandler;

import ProtocolHandler.Handlers.ArrayListToObservableHandler;
import ProtocolHandler.Handlers.CreditParserHandler;
import ProtocolHandler.Handlers.PingHandler;
import ProtocolHandler.Handlers.ImageFromServerHandler;

/**
 * The Class HandlersRegisterer has a single static method that registers a protocol handler to
 * a request type.
 */
public class HandlersRegisterer {
	
	/**
	 *  Register Response Handlers registers a pre created handler to be invoked
	 *  whenever a request of a certain type is echoed back (see more in IHandler.java)
	 */
	
	public static void RegisterHandlers()
	{
		Protocol.RegisterHandler(RequestType.Ping, new PingHandler());
		Protocol.RegisterHandler(RequestType.UpdateOrder, null);
		Protocol.RegisterHandler(RequestType.AuthenticateUser,null);
		Protocol.RegisterHandler(RequestType.SetLogOut,null);
		Protocol.RegisterHandler(RequestType.GetCatalog,new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.GetCart,new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.GetBranches, new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.GetUserCurrency, new CreditParserHandler());
		Protocol.RegisterHandler(RequestType.UpdateZerliCoins, null);
		Protocol.RegisterHandler(RequestType.InsertOrder, null);
		Protocol.RegisterHandler(RequestType.GetBranch, null);
		Protocol.RegisterHandler(RequestType.GetOrdersByBranch, new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.GetItemsOfOrder, new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.ConfirmOrder, null);
		Protocol.RegisterHandler(RequestType.AddToCart, null);
		Protocol.RegisterHandler(RequestType.GetAllCustomerOrders, new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.DeleteItemFromCart, null);
		Protocol.RegisterHandler(RequestType.GetNotification, new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.UpdateNotification,null);
		Protocol.RegisterHandler(RequestType.CancelOrder,null);
		Protocol.RegisterHandler(RequestType.ResetNewCustomer,null);
		Protocol.RegisterHandler(RequestType.UpdateNewItemInCart,null);
		Protocol.RegisterHandler(RequestType.DeleteItemFromNewItemList,null);
		Protocol.RegisterHandler(RequestType.DeleteNewItemFromCart,null);
		Protocol.RegisterHandler(RequestType.DeleteAllnewItemFromCart,null);
		Protocol.RegisterHandler(RequestType.EndOrder,null);
		Protocol.RegisterHandler(RequestType.GetItemByID, null);
		Protocol.RegisterHandler(RequestType.RemoveItemByID,null);
		Protocol.RegisterHandler(RequestType.UpdateItem, null);
		Protocol.RegisterHandler(RequestType.AddItem, null);
		Protocol.RegisterHandler(RequestType.ComplaintResponse, null);
		Protocol.RegisterHandler(RequestType.GetComplaints, new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.MakeComplaint, null);
		Protocol.RegisterHandler(RequestType.GetPendingClient, null);
		Protocol.RegisterHandler(RequestType.RegisterClient, null);
		Protocol.RegisterHandler(RequestType.GetAllCustomersInfo, new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.GetAllEmployeesInfo, new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.UpdateAccountAccess, null);
		Protocol.RegisterHandler(RequestType.UpdateEmployeeRole, null);
		Protocol.RegisterHandler(RequestType.SaveSurvey, null);
		Protocol.RegisterHandler(RequestType.GetSurveysNames, new ArrayListToObservableHandler());
		Protocol.RegisterHandler(RequestType.SaveSurveyAnswers, null);
		Protocol.RegisterHandler(RequestType.GetReport, null);
		Protocol.RegisterHandler(RequestType.SendNotification, null);
		Protocol.RegisterHandler(RequestType.GetCustomerInfo, null);
		Protocol.RegisterHandler(RequestType.GetSurveyHistogram, new ImageFromServerHandler ());
		Protocol.RegisterHandler(RequestType.SavePDF, null);
		Protocol.RegisterHandler(RequestType.GetReportByBranch, null);
		Protocol.RegisterHandler(RequestType.SetComplaintAlerted, null);
		Protocol.RegisterHandler(RequestType.GetReadySurveys, null);
	}
}
