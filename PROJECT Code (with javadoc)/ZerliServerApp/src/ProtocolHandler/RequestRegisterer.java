/*
 * 
 */
package ProtocolHandler;

import ProtocolHandler.Operations.*;

// TODO: Auto-generated Javadoc
/**
 * The Class RequestRegisterer has one method and is used
 * to encapsulate internal ProtocolHandler operations.
 * 
 */
public class RequestRegisterer {
	
	/**
	 * This method is where all the RegisterOperation methods reside
	 * When the SW Designer creates an operation to be invoked when a requestType is made
	 * he will map between the two here using the Protocol.RegisterOperation method (See Protocol.java for more info)
	 */
	public static void RegisterProtocol()
	{
		/*** Add the mapping between your requests and operations ***/
		Protocol.RegisterOperation(RequestType.Ping, new PingOperation());
		Protocol.RegisterOperation(RequestType.AuthenticateUser, new AuthenticationOperation());
		Protocol.RegisterOperation(RequestType.SetLogOut, new LoggedOutOperation());
		Protocol.RegisterOperation(RequestType.GetCatalog,new GetCatalogOperation());
		Protocol.RegisterOperation(RequestType.GetCart,new GetCartOperation());
		Protocol.RegisterOperation(RequestType.AddToCart,new AddToCartOperation());
		Protocol.RegisterOperation(RequestType.GetAllCustomerOrders,new GetAllCustomerOrdersOperation());
		Protocol.RegisterOperation(RequestType.DeleteItemFromCart,new DeleteItemFromCartOperation());
		Protocol.RegisterOperation(RequestType.GetNotification,new GetNotificationOperation());
		Protocol.RegisterOperation(RequestType.UpdateNotification,new UpdateNotificationOperation());
		Protocol.RegisterOperation(RequestType.GetOrdersByBranch,new GetOrdersByBranchOperation());
		Protocol.RegisterOperation(RequestType.GetItemsOfOrder,new GetItemsOfOrderOperation());
		Protocol.RegisterOperation(RequestType.ConfirmOrder,new ConfirmOrderOperation());
		Protocol.RegisterOperation(RequestType.GetBranches, new GetBranchesOperation());
		Protocol.RegisterOperation(RequestType.GetBranch, new GetBranchOperation());
		Protocol.RegisterOperation(RequestType.GetUserCurrency, new GetUserCurrencyOperation());
		Protocol.RegisterOperation(RequestType.UpdateZerliCoins, new UpdateZerliCoinsOperation());
		Protocol.RegisterOperation(RequestType.InsertOrder, new InsertOrderOperation());
		Protocol.RegisterOperation(RequestType.EndOrder,new EndOrderOperation());
		Protocol.RegisterOperation(RequestType.CancelOrder, new CancelOrderOperation());
		Protocol.RegisterOperation(RequestType.ResetNewCustomer, new ResetNewCustomerOperation());
		Protocol.RegisterOperation(RequestType.UpdateNewItemInCart, new UpdateNewItemInCartOperation());
		Protocol.RegisterOperation(RequestType.DeleteItemFromNewItemList, new DeleteItemFromNewItemListOperation());
		Protocol.RegisterOperation(RequestType.DeleteNewItemFromCart, new DeleteNewItemFromCartOperation());
		Protocol.RegisterOperation(RequestType.DeleteAllnewItemFromCart, new DeleteAllnewItemFromCartOperation());	
		Protocol.RegisterOperation(RequestType.RemoveItemByID, new RemoveItemOperation());
		Protocol.RegisterOperation(RequestType.GetItemByID, new GetItemByIDOperation());
		Protocol.RegisterOperation(RequestType.UpdateItem, new UpdateItemOperation());
		Protocol.RegisterOperation(RequestType.AddItem, new AddItemOperation());
		Protocol.RegisterOperation(RequestType.GetComplaints, new GetComplaintsOperation());
		Protocol.RegisterOperation(RequestType.ComplaintResponse, new ComplaintResponseOperation());
		Protocol.RegisterOperation(RequestType.MakeComplaint, new MakeComplaintOperation());
		Protocol.RegisterOperation(RequestType.GetPendingClient, new GetPendingClientOperation());
		Protocol.RegisterOperation(RequestType.RegisterClient, new RegisterClientOperation());
		Protocol.RegisterOperation(RequestType.GetAllCustomersInfo, new GetAllCustomersInfoOperation());
		Protocol.RegisterOperation(RequestType.GetAllEmployeesInfo, new GetAllEmployeesInfoOperation());
		Protocol.RegisterOperation(RequestType.UpdateAccountAccess, new UpdateAccountAccessOperation());
		Protocol.RegisterOperation(RequestType.UpdateEmployeeRole, new UpdateEmployeeRoleOperation());
		Protocol.RegisterOperation(RequestType.GetSurveyHistogram, new GetSurveyHistogramOperation());
		Protocol.RegisterOperation(RequestType.SaveSurvey, new SaveSurveyOperation());
		Protocol.RegisterOperation(RequestType.GetSurveysNames, new GetSurveysNamesOperation());
		Protocol.RegisterOperation(RequestType.GetSurvey, new GetSurveyOperation());
		Protocol.RegisterOperation(RequestType.SaveSurveyAnswers, new SaveSurveyAnswersOperation());
		Protocol.RegisterOperation(RequestType.GetReport, new GetReportOperation());
		Protocol.RegisterOperation(RequestType.SendNotification, new SendNotificationOperation());
		Protocol.RegisterOperation(RequestType.GetCustomerInfo, new GetCustomerInfoOperation());
		Protocol.RegisterOperation(RequestType.SavePDF, new SavePDFOperation());
		Protocol.RegisterOperation(RequestType.GetReportByBranch, new GetReportByBranchOperation());
		Protocol.RegisterOperation(RequestType.SetComplaintAlerted, new SetComplaintAlertedOperation());
		Protocol.RegisterOperation(RequestType.GetReadySurveys, new GetReadySurveysOperation());
	}
}
