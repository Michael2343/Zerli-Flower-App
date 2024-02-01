package unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entities.Access;
import Entities.Roles;
import controllers.LoginController;

class LoginControllerTest {
	

	LoginController loginController;

	@BeforeEach
	void setUp() throws Exception {
		loginController = new LoginController();
	}

	/**
	 * Test Name: Login_Customer_Active
	 * Description: Simulate a login request for an active customer
	 * Inputs: Stubbed response: [customer,active,0]
	 * Expected Outputs: The URIs of the customer FXML paths
	 */
	@Test
	void Login_Customer_Active() {
		String[] expectedURIs = new String[] {"/gui/mainframes/CustomerMainScreen.fxml",
											"/gui/usercontrols/CustomerHomePage.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.customer,Access.active, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}
	
	/**
	 * Test Name: Login_Customer_Frozen
	 * Description: Simulate a login request for a frozen customer
	 * Inputs: Stubbed response: [customer,frozen,0]
	 * Expected Outputs: The URIs of the customer FXML paths
	 */
	@Test
	void Login_Customer_Frozen() {
		String[] expectedURIs = new String[] {"/gui/mainframes/CustomerMainScreen.fxml",
												"/gui/usercontrols/CustomerHomePage.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.customer,Access.active, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}
	
	/**
	 * Test Name: Login_Customer_Inactive
	 * Description: Simulate a login request for an inactive customer
	 * Inputs: Stubbed response: [customer,inactive,0]
	 * Expected Outputs: The URIs of the customer FXML paths
	 */
	@Test
	void Login_Customer_Inactive() {
		String[] expectedURIs = new String[] {"/gui/mainframes/CustomerMainScreen.fxml",
											"/gui/usercontrols/CustomerHomePage.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.customer,Access.active, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}
	
	/**
	 * Test Name: Login_Manager
	 * Description: Simulate a login request for a manager
	 * Inputs: Stubbed response: [manager,active,0]
	 * Expected Outputs: The URIs of the manager FXML paths
	 */
	@Test
	void Login_Manager() {
		String[] expectedURIs = new String[] {"/gui/mainframes/ManagerMainScreen.fxml","/gui/usercontrols/ManagerHomePage.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.manager,Access.active, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}
	
	/**
	 * Test Name: Login_CEO
	 * Description: Simulate a login request for a ceo
	 * Inputs: Stubbed response: [ceo,active,0]
	 * Expected Outputs: The URIs of the ceo FXML paths
	 */
	@Test
	void Login_CEO() {
		
		String[] expectedURIs = new String[] {"/gui/mainframes/CEOMainScreen.fxml","/gui/usercontrols/ServiceHomePage.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.ceo,Access.active, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}
	
	/**
	 * Test Name: Login_Delivery
	 * Description: Simulate a login request for a delivery guy
	 * Inputs: Stubbed response: [delivery,active,0]
	 * Expected Outputs: The URIs of the delivery-guy FXML paths
	 */
	@Test
	void Login_Delivery() {
		String[] expectedURIs = new String[] {"/gui/mainframes/DeliveryMainScreen.fxml","/gui/usercontrols/DeliveryOrderManager.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.delivery,Access.active, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}

	/**
	 * Test Name: Login_Marketing
	 * Description: Simulate a login request for a marketing
	 * Inputs: Stubbed response: [marketing,active,0]
	 * Expected Outputs: The URIs of the marketing FXML paths
	 */
	@Test
	void Login_Marketing() {
		String[] expectedURIs = new String[] {"/gui/mainframes/MarketingMainScreen.fxml","/gui/usercontrols/MarketingCatalogEditor.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.marketing,Access.active, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}
	
	/**
	 * Test Name: Login_Service
	 * Description: Simulate a login request for a service
	 * Inputs: Stubbed response: [service,active,0]
	 * Expected Outputs: The URIs of the service FXML paths
	 */
	@Test
	void Login_Service() {
		String[] expectedURIs = new String[] {"/gui/mainframes/CustomerServiceMainScreen.fxml","/gui/usercontrols/ServiceHomePage.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.service,Access.active, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}
	
	/**
	 * Test Name: Login_Expert
	 * Description: Simulate a login request for an expert
	 * Inputs: Stubbed response: [expert,active,0]
	 * Expected Outputs: The URIs of the expert FXML paths
	 */
	@Test
	void Login_Expert() {
		String[] expectedURIs = new String[] {"/gui/mainframes/ExpertMainScreen.fxml","/gui/usercontrols/ExpertHomePage.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.expert,Access.active, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}
	
	/**
	 * Test Name: Login_AlreadyLoggedIn
	 * Description: Simulate a login request for an already logged in account
	 * Inputs: Stubbed response: [customer,active,1]
	 * Expected Outputs: "already logged in" alert
	 */
	@Test
	void Login_AlreadyLoggedIn() {
		String[] expectedURIs = new String[] {"already logged in"};
		String[] result = loginController.guiElements.getPath(Roles.customer,Access.active, 1);
		assertEquals(expectedURIs[0],result[0]);
	}
	
	/**
	 * Test Name: Login_WrongCreds
	 * Description: Simulate a login request for non-existing user
	 * Inputs: Stubbed response: [null,noaut,0]
	 * Expected Outputs: "wrong creds" alert
	 */
	@Test
	void Login_WrongCreds() {
		String[] expectedURIs = new String[] {"wrong creds"};
		String[] result = loginController.guiElements.getPath(null,Access.noaut, 0);
		assertEquals(expectedURIs[0],result[0]);
	}
	
	/**
	 * Test Name: Login_Frozen_for_employee
	 * Description: Simulate a login request for impossible mode - frozen employee
	 * Inputs: Stubbed response: [ceo,frozen,0]
	 * Expected Outputs: The CEO will login anyway since frozen is impossible: The URIs of the ceo FXML paths
	 */
	@Test
	void Login_Frozen_for_employee() {
		String[] expectedURIs = new String[] {"/gui/mainframes/CEOMainScreen.fxml","/gui/usercontrols/ServiceHomePage.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.ceo,Access.frozen, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}
	
	/**
	 * Test Name: Login_Inactive_for_employee
	 * Description: Simulate a login request for impossible mode - inactive employee
	 * Inputs: Stubbed response: [ceo,inactive,0]
	 * Expected Outputs: The CEO will login anyway since frozen is impossible: The URIs of the ceo FXML paths
	 */
	@Test
	void Login_Inactive_for_employee() {
		String[] expectedURIs = new String[] {"/gui/mainframes/CEOMainScreen.fxml","/gui/usercontrols/ServiceHomePage.fxml"};
		String[] result = loginController.guiElements.getPath(Roles.ceo,Access.inactive, 0);
		assertEquals(expectedURIs[0],result[0]);
		assertEquals(expectedURIs[1],result[1]);
	}
	
	/**
	 * Test Name: Login_Nulls
	 * Description: Simulate a login request with null values
	 * Inputs: Stubbed response: [null,null,0]
	 * Expected Outputs: "inactive" alert
	 */
	@Test 
	void Login_Nulls()
	{		
		String[] expectedURIs = new String[] {"inactive"};
		try
		{
			String[] result = loginController.guiElements.getPath(null,null, 0);
			assertEquals(expectedURIs[0],result[0]);
		}
		catch(NullPointerException e)
		{
			fail("Exception not handled in null cases");
		}
	}
	
	/**
	 * Test Name: Login_InvalidLoggedIn
	 * Description: Simulate a login request with invalid loggedIn value
	 * Inputs: Stubbed response: [ceo,active,2]
	 * Expected Outputs: "invalid" alert
	 */
	@Test 
	void Login_InvalidLoggedIn()
	{		
		String[] expectedURIs = new String[] {"inactive"};
		try
		{
			String[] result = loginController.guiElements.getPath(Roles.ceo,Access.active, 2);
			assertEquals(expectedURIs[0],result[0]);
		}
		catch(Exception e)
		{
			fail("Exception not handled in special case of invalid loggedIn");
		}
	}
	
	/**
	 * Test Name: Login_NonExisting_LoggedIn
	 * Description: Simulate a login request for a non-existing user but loggedIn = 1
	 * Inputs: Stubbed response: [null,noauth,1]
	 * Expected Outputs: "wrong creds" alert
	 */
	@Test 
	void Login_NonExisting_LoggedIn()
	{		
		String[] expectedURIs = new String[] {"wrong creds"};
		try
		{
			String[] result = loginController.guiElements.getPath(null,Access.noaut, 1);
			assertEquals(expectedURIs[0],result[0]);
		}
		catch(Exception e)
		{
			fail("Exception not handled in special case of invalid user that is logged in");
		}
	}
}
