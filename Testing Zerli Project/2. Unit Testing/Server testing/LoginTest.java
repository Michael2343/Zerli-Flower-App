package unittests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entities.Access;
import Entities.Roles;
import reports.LoggingService;
import server.Server;
import server.ServerConnSQL;

class LoginTest {

	/* Name: LoginTest
	 * Description: Unit Testing class for Login
	 * Class Under Testing: ServerConnSQL {Method: Authenticate}
	 * Dependencies:
	 * - Server.Log : Refactored into a LoggingService and replaced with a fake that redirects to stdout
	 */

		
		/*
		 * Test Double Name: FakeConsoleLogger
		 * 
		 * Test Double Type: Fake
		 * 
		 * Description: The actual server uses a singleton Logger
		 * 				with the purpose of logging various actions, among them report generation
		 *				when testing the gui does not exist so the logger returns an exception
		 *				to remove this dependency we used property injection and refactored the singleton
		 *				logger as a service provided by users
		 *				the FakeConsoleLogger redirects the logging to the console which is available
		 *				during junit test running
		 */			
		class FakeConsoleLogger implements LoggingService
		{
			@Override
			public void Log(String system, String message) {
				System.out.println(String.format("[%s]: %s", system,message));
			}
			
		}
	
	FakeConsoleLogger fakeLogger = new FakeConsoleLogger();
		
	@BeforeAll
	static void connectToDB()
	{
		Server.SqlServerManager = new ServerConnSQL();
		ServerConnSQL.startConn("11223344"); /** This is my personal mySQL Password **/

	}

	@BeforeEach
	void setUp() throws Exception {
		ServerConnSQL.SetLoggingService(fakeLogger);
	}

	/**
	 * Test Name: Login_Customer_Active
	 * Description: Attempt to login as active customer
	 * Inputs: <Username: berto>,<Password: 1234>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [0,active,customer]
	 */
	@Test
	void Login_Customer_Active() {
		String username = "berto";
		String password = "1234";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		int isLoggedIn = (int)data[0];
		Access access = (Access)data[1];
		Roles role = (Roles)data[2]; 
		assertEquals(0,isLoggedIn);
		assertEquals(Access.active,access);
		assertEquals(Roles.customer,role);
	}
	
	/**
	 * Test Name: Login_Customer_Frozen
	 * Description: Attempt to login as frozen customer
	 * Inputs: <Username: nurit>,<Password: 1234>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [0,frozen,customer]
	 */
	@Test
	void Login_Customer_Frozen() {
		String username = "nurit";
		String password = "1234";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		int isLoggedIn = (int)data[0];
		Access access = (Access)data[1];
		Roles role = (Roles)data[2]; 
		assertEquals(0,isLoggedIn);
		assertEquals(Access.frozen,access);
		assertEquals(Roles.customer,role);
	}
	
	/**
	 * Test Name: Login_Customer_Inactive
	 * Description: Attempt to login as inactive customer
	 * Inputs: <Username: ido>,<Password: 1234>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [0,inactive,customer]
	 */
	@Test
	void Login_Customer_Inactive()
	{
		String username = "ido";
		String password = "1234";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		int isLoggedIn = (int)data[0];
		Access access = (Access)data[1];
		Roles role = (Roles)data[2]; 
		assertEquals(0,isLoggedIn);
		assertEquals(Access.inactive,access);
		assertEquals(Roles.customer,role);
	}
	
	/**
	 * Test Name: Login_Manager
	 * Description: Attempt to login as manager
	 * Inputs: <Username: ido>,<Password: 1234>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [0,active,manager]
	 */
	@Test
	void Login_Manager() {
		String username = "meshi";
		String password = "1234";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		int isLoggedIn = (int)data[0];
		Access access = (Access)data[1];
		Roles role = (Roles)data[2]; 
		assertEquals(0,isLoggedIn);
		assertEquals(Access.active,access);
		assertEquals(Roles.manager,role);
	}
	
	/**
	 * Test Name: Login_Delivery
	 * Description: Attempt to login as Delivery
	 * Inputs: <Username: eli>,<Password: 1234>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [0,active,delivery]
	 */
	@Test
	void Login_Delivery() {
		String username = "eli";
		String password = "1234";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		int isLoggedIn = (int)data[0];
		Access access = (Access)data[1];
		Roles role = (Roles)data[2]; 
		assertEquals(0,isLoggedIn);
		assertEquals(Access.active,access);
		assertEquals(Roles.delivery,role);
	}
	
	/**
	 * Test Name: Login_Service
	 * Description: Attempt to login as Service
	 * Inputs: <Username: raz>,<Password: 1234>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [0,active,service]
	 */
	@Test
	void Login_Service() {
		String username = "raz";
		String password = "1234";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		int isLoggedIn = (int)data[0];
		Access access = (Access)data[1];
		Roles role = (Roles)data[2]; 
		assertEquals(0,isLoggedIn);
		assertEquals(Access.active,access);
		assertEquals(Roles.service,role);
	}
	
	/**
	 * Test Name: Login_Expert
	 * Description: Attempt to login as Expert
	 * Inputs: <Username: expert>,<Password: 1234>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [0,active,expert]
	 */
	@Test
	void Login_Expert() {
		String username = "expert";
		String password = "1234";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		int isLoggedIn = (int)data[0];
		Access access = (Access)data[1];
		Roles role = (Roles)data[2]; 
		assertEquals(0,isLoggedIn);
		assertEquals(Access.active,access);
		assertEquals(Roles.expert,role);
	}
	
	/**
	 * Test Name: Login_CEO
	 * Description: Attempt to login as CEO
	 * Inputs: <Username: shlomo>,<Password: 1234>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [0,active,ceo]
	 */
	@Test
	void Login_CEO() {
		String username = "shlomo";
		String password = "1234";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		int isLoggedIn = (int)data[0];
		Access access = (Access)data[1];
		Roles role = (Roles)data[2]; 
		assertEquals(0,isLoggedIn);
		assertEquals(Access.active,access);
		assertEquals(Roles.ceo,role);
	}
	
	/**
	 * Test Name: Login_Marketing
	 * Description: Attempt to login as Marketing
	 * Inputs: <Username: daniel>,<Password: 1234>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [0,active,marketing]
	 */
	@Test
	void Login_Marketing() {
		String username = "daniel";
		String password = "1234";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		int isLoggedIn = (int)data[0];
		Access access = (Access)data[1];
		Roles role = (Roles)data[2]; 
		assertEquals(0,isLoggedIn);
		assertEquals(Access.active,access);
		assertEquals(Roles.marketing,role);
	}
	
	/**
	 * Test Name: Login_NoUser
	 * Description: Attempt to login with non-existing user
	 * Inputs: <Username: no_user>,<Password: no_user>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [null,noaut,null]
	 */
	@Test
	void Login_NoUser() {
		String username = "no_user";
		String password = "no_user";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		Access access = (Access)data[1];
		assertEquals(null,data[0]);
		assertEquals(Access.noaut,access);
		assertEquals(null,data[2]);
	}
	
	/**
	 * Test Name: Login_CheckLoggedIn
	 * Description: Attempt to login twice to the same account
	 * Inputs: <Username: berto>,<Password: 1234>
	 * Expected Outputs: 
	 * 	-first login: [AlreadyLoggedIn, Access, Role] = [0,active,customer]
	 *  -second login: [AlreadyLoggedIn, Access, Role] = [1,active,customer]
	 */
	@Test
	void Login_CheckLoggedIn() {
		String username = "berto";
		String password = "1234";
		/** Make Sure Is LoggedOut ***/
		Server.SqlServerManager.LoggedOut(username);
		/** Login First Time **/
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		int isLoggedIn = (int)data[0];
		Access access = (Access)data[1];
		Roles role = (Roles)data[2]; 
		assertEquals(0,isLoggedIn);
		assertEquals(Access.active,access);
		assertEquals(Roles.customer,role);
		/** Make Sure Cannot Login Second Time **/
		data = Server.SqlServerManager.Authenticate(username, password);
		isLoggedIn = (int)data[0];
		access = (Access)data[1];
		role = (Roles)data[2]; 
		assertEquals(1,isLoggedIn);
		assertEquals(Access.active,access);
		assertEquals(Roles.customer,role);
	}
	
	/**
	 * Test Name: Login_Login_Nulls
	 * Description: Attempt to login with null details
	 * Inputs: <Username: null>,<Password: null>
	 * Expected Outputs: [AlreadyLoggedIn, Access, Role] = [null,noaut,null]
	 */
	@Test
	void Login_Login_Nulls() {
		String username = null;
		String password = null;
		Object[] data = Server.SqlServerManager.Authenticate(username, password);
		Access access = (Access)data[1];
		assertEquals(null,data[0]);
		assertEquals(Access.noaut,access);
		assertEquals(null,data[2]);
	}
}
