package unittests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Entities.ItemType;
import Entities.ReportType;
import reports.IPDFGenerationService;
import reports.LoggingService;
import reports.ReportGenerator;
import server.Server;
import server.ServerConnSQL;


class ReportGeneratorTest {
	
/* Name: ReportGeneratorTest
 * Description: Unit Testing class for report generation in terms of data
 * 				gathering and processing
 * Class Under Testing: ReportGenerator
 * Dependencies:
 * - PDFGenerator: Refactored into an Interface Service and replaced with spy
 * 				   that can hold the data
 * - Server.Log : Refactored into a LoggingService and replaced with a fake that redirects to stdout
 * - ServerConnSQL: Since the database is active (to retrieve data)
 * 					, reports will be inserted in a normal fashion, dependency is not removed.
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
	
	/*
	 * Test Double Name: SpyPDFGenerator
	 * 
	 * Test Double Type: Spy
	 * 
	 * Description: This Spy will impersonate a PDF generator
	 * 				it will receive the data of the reports and store it in public variables (AKA SPY)
	 * 				accessible by the testing class, therefore - allowing
	 * 				the data under test to be exposed and tested
	 * 				the spy always returns a so called 'pdf' whose byte array
	 * 			    contains nothing but a single 0
	 * 			 	the purpose of this is to prevent alterations caused by
	 * 				dependencies such as the SQL Driver
	 */
	class SpyPDFGenerator implements IPDFGenerationService
	{
		public ArrayList<Integer[]> processedIncome;
		
		public ArrayList<Integer> processedComplaintsX,processedComplaintsY;
		
		public ArrayList<Integer> processedCEOX,processedCEOY;
		
		public HashMap<ItemType, Integer> processedOrders;
		
		@Override
		public byte[] createIncomeReportTable(String branch, String string, ArrayList<Integer[]> dailyIncomeData,
				ArrayList<LocalDate> dates) {
			processedIncome = dailyIncomeData;
			return new byte[] {0};
		}

		@Override
		public byte[] createComplaintsReportHistogram(String branch, String string, ArrayList<Integer> x,
				ArrayList<Integer> y) {
			processedComplaintsX = x;
			processedComplaintsY = y;
			return new byte[] {0};
		}

		@Override
		public byte[] createCEOReportForBranch(String branch, String string, ArrayList<Integer> x,
				ArrayList<Integer> y) {
			processedCEOX = x;
			processedCEOY = y;
			return new byte[] {0};
		}

		@Override
		public byte[] createOrderReportHistogram(String branch, String string, HashMap<ItemType, Integer> histogram) {
			processedOrders = histogram;
			return new byte[] {0};
		}
		
	}
	
	FakeConsoleLogger logger = new FakeConsoleLogger();
	SpyPDFGenerator pdfSpy;
	
	@BeforeAll
	static void connectToDB()
	{
		Server.SqlServerManager = new ServerConnSQL();
		ServerConnSQL.startConn("11223344"); /** This is my personal mySQL Password **/
	}
	
	@BeforeEach
	void setUp() throws Exception {
		pdfSpy = new SpyPDFGenerator();
	}

	/*---------- Income Reports ----------*/
	
	/**
	 * Test Name: generateMonthlyReport_Haifa_Income_March21
	 * Description: Generating monthly income report for haifa in march 21 where there are no reports
	 * Inputs: <ReportType: income>,<branch: Haifa> ,<LocalDate: '01/03/2021'>
	 * Expected Outputs: ArrayList: empty arrayList (no data)
	 */
	@Test
	void generateMonthlyReport_Haifa_Income_March21() {
		ReportType inputReport = ReportType.income;
		String inputBranch = "Haifa";
		LocalDate inputDate = LocalDate.of(2021, 3, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertTrue(pdfSpy.processedIncome.size() == 0);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Lod_Income_March21
	 * Description: Generating monthly income report for a branch that does not exist
	 * Inputs: <ReportType: income>,<branch: Lod> ,<LocalDate: '01/03/2021'>
	 * Expected Outputs: ArrayList [size = 0], no data since there is no branch
	 */
	@Test
	void generateMonthlyReport_Lod_Income_March21() {
		ReportType inputReport = ReportType.income;
		String inputBranch = "Lod";
		LocalDate inputDate = LocalDate.of(2021, 3, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertTrue(pdfSpy.processedIncome.size() == 0);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Akko_Income_Dec21
	 * Description: Generating monthly income report for a branch with data
	 * Inputs: <ReportType: income>,<branch: Akko> ,<LocalDate: '01/12/2021'>
	 * Expected Outputs: ArrayList R where R[i][2] = {1248,1446,205,482,369,237,301,426}
	 */
	@Test
	void generateMonthlyReport_Akko_Income_Dec21() {
		int expectedTotalIncome[] = {1248,1446,205,482,369,237,301,426};
		ReportType inputReport = ReportType.income;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2021, 12, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		for(int i = 0;i < pdfSpy.processedIncome.size();i++)
			assertEquals(pdfSpy.processedIncome.get(i)[2], expectedTotalIncome[i]);
	}

	/**
	 * Test Name: generateMonthlyReport_Akko_Income_Dec24
	 * Description: Generating monthly income report for a future month
	 * Inputs: <ReportType: income>,<branch: Akko> ,<LocalDate: '01/12/2024'>
	 * Expected Outputs: Empty arraylist - no data
	 */
	@Test
	void generateMonthlyReport_Akko_Income_Dec24() {
		ReportType inputReport = ReportType.income;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2024, 12, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(0,pdfSpy.processedIncome.size());
	}
	
	/**
	 * Test Name: generateMonthlyReport_NULL_Income_Dec21
	 * Description: Generating monthly income report for a NULL branch
	 * Inputs: <ReportType: income>,<branch: NULL> ,<LocalDate: '01/12/2021'>
	 * Expected Outputs: No data processed size of arraylist is zero
	 */
	@Test
	void generateMonthlyReport_NULL_Income_Dec21() {
		ReportType inputReport = ReportType.income;
		String inputBranch = null;
		LocalDate inputDate = LocalDate.of(2021, 12, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(pdfSpy.processedIncome.size(),0);
	}
	
	/**
	 * Test Name: generateMonthlyReport_NULL_Income_Dec21
	 * Description: Generating monthly income report for a NULL date
	 * Inputs: <ReportType: income>,<branch: Akko> ,<LocalDate: '01/12/2021'>
	 * Expected Outputs: No data processed size of arraylist is zero
	 */
	@Test
	void generateMonthlyReport_Akko_Income_NULL() {
		ReportType inputReport = ReportType.income;
		String inputBranch = "Akko";
		LocalDate inputDate = null;
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		try
		{
			ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
			assertEquals(pdfSpy.processedIncome.size(),0);
		}
		catch (NullPointerException e)
		{
			fail("Null date case is not handled");
		}
	}
	
	//quarterly
	/**
	 * Test Name: generateQuarterlyReport_Haifa_Income_March21
	 * Description: Generating quarterly income report for haifa in 1st quarter where there are no reports
	 * Inputs: <ReportType: income>,<branch: Haifa> ,<LocalDate: '01/01/2021' equivalent to 1st quarter of 2021>
	 * Expected Outputs: ArrayList R: |R| = 3 and R[i][2] = {0,0,0}
	 * 
	 */
	@Test
	void generateQuarterlyReport_Haifa_Income_March21() {
		ReportType inputReport = ReportType.income;
		String inputBranch = "Haifa";
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		for(Integer[] week : pdfSpy.processedIncome)
			assertEquals(week[2],0);
		assertEquals(3,pdfSpy.processedIncome.size());
	}
	
	/**
	 * Test Name: generateMonthlyReport_Lod_Income_March21
	 * Description: Generating quarterly income report for a branch that does not exist
	 * Inputs: <ReportType: income>,<branch: Lod> ,<LocalDate: '01/01/2021' equivalent to 1st quarter of 2021>
	 * Expected Outputs: ArrayList R [size = 3], R[i][2] = {0,0,0}
	 */
	@Test
	void generateQuarterlyReport_Lod_Income_March21() {
		ReportType inputReport = ReportType.income;
		String inputBranch = "Lod";
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		for(Integer[] week : pdfSpy.processedIncome)
			assertEquals(week[2],0);
		assertEquals(3,pdfSpy.processedIncome.size());
	}
	
	/**
	 * Test Name: generateQuarterlyReport_Akko_Income_Dec21
	 * Description: Generating quarterly income report for a branch with data
	 * Inputs: <ReportType: income>,<branch: Akko> ,<LocalDate: '01/04/2021' equivalent to 4th quarter of 2021>
	 * Expected Outputs: ArrayList R where R[i][2] = {1248,1446,205,482} |R| = 3
	 */
	@Test
	void generateQuarterlyReport_Akko_Income_Dec21() {
		int[] expectedTotalIncome = {4848,4476,4714};
		ReportType inputReport = ReportType.income;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2021, 4, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		for(int i = 0;i < pdfSpy.processedIncome.size();i++)
			assertEquals(expectedTotalIncome[i],pdfSpy.processedIncome.get(i)[2]);
		assertEquals(3,pdfSpy.processedIncome.size());
	}

	/**
	 * Test Name: generateQuarterlyReport_NULL_Income_Dec21
	 * Description: Generating Quarterly income report for a NULL branch
	 * Inputs: <ReportType: income>,<branch: NULL> ,<LocalDate: '01/04/2021' equivalent to 4th quarter of 2021>
	 * Expected Outputs: No data processed size of arraylist is zero
	 */
	@Test
	void generateQuarterlyReport_NULL_Income_Dec21() {
		ReportType inputReport = ReportType.income;
		String inputBranch = null;
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(3,pdfSpy.processedIncome.size());
		for(Integer[] set : pdfSpy.processedIncome)
			assertEquals(0,set[2]);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Akko_Income_Dec24
	 * Description: Generating quarterly income report for a future quarter
	 * Inputs: <ReportType: income>,<branch: Akko> ,<LocalDate: '01/01/2024' equivalent to 1st quarter>
	 * Expected Outputs: ArrayList R = {0,0,0} |R| = 3
	 */
	@Test
	void generateQuarterlyReport_Akko_Income_Dec24() {
		ReportType inputReport = ReportType.income;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2024, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(3,pdfSpy.processedIncome.size());
		for(Integer[] set : pdfSpy.processedIncome)
			assertEquals(0,set[2]);
	}
	
	
	/**
	 * Test Name: generateQuarterlyReport_NULL_Income_Dec21
	 * Description: Generating quarterly income report for a NULL date
	 * Inputs: <ReportType: income>,<branch: Akko> ,<LocalDate: NULL>
	 * Expected Outputs: No data processed size of arraylist 3 but all content is 0
	 */
	@Test
	void generateQuarterlyReport_Akko_Income_NULL() {
		ReportType inputReport = ReportType.income;
		String inputBranch = "Akko";
		LocalDate inputDate = null;
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		try
		{
			ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
			assertEquals(pdfSpy.processedIncome.size(),3);
			for(Integer[] set : pdfSpy.processedIncome)
				assertEquals(0,set[2]);
		}
		catch (NullPointerException e)
		{
			fail("Null date case is not handled");
		}
	}
	
	/* ----------Order Reports---------- */
	
	
	/**
	 * Test Name: generateMonthlyReport_Haifa_Orders_March21
	 * Description: Generating monthly order report for haifa in march 21 where there are no reports
	 * Inputs: <ReportType: order>,<branch: Haifa> ,<LocalDate: '01/03/2021'>
	 * Expected Outputs: HashMap: for every ItemType -> 0 orders
	 * 
	 */
	@Test
	void generateMonthlyReport_Haifa_Orders_March21() {
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
			expected.put(t, 0);
		ReportType inputReport = ReportType.order;
		String inputBranch = "Haifa";
		LocalDate inputDate = LocalDate.of(2021, 3, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(expected,pdfSpy.processedOrders);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Lod_Orders_March21
	 * Description: Generating monthly orders report for a branch that does not exist
	 * Inputs: <ReportType: orders>,<branch: Lod> ,<LocalDate: '01/03/2021'>
	 * Expected Outputs: HashMap: for every ItemType -> 0 orders
	 */
	@Test
	void generateMonthlyReport_Lod_Orders_March21() {
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
			expected.put(t, 0);
		ReportType inputReport = ReportType.order;
		String inputBranch = "Lod";
		LocalDate inputDate = LocalDate.of(2021, 3, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(expected,pdfSpy.processedOrders);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Akko_Orders_Dec21
	 * Description: Generating monthly orders report for a branch with data
	 * Inputs: <ReportType: orders>,<branch: Akko> ,<LocalDate: '01/12/2021'>
	 * Expected Outputs: HashMap itemType[i] -> {5,2,7,20,4,1,7}[i]
	 */
	@Test
	void generateMonthlyReport_Akko_Orders_Dec21() {
		Integer[] expectedOfEach = {5,2,7,20,4,1,7};
		int i = 0;
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
		{
			expected.put(t, expectedOfEach[i++]);
		}
		ReportType inputReport = ReportType.order;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2021, 12, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(expected,pdfSpy.processedOrders);
	}

	/**
	 * Test Name: generateMonthlyReport_Akko_order_Dec24
	 * Description: Generating monthly order report for a future month
	 * Inputs: <ReportType: order>,<branch: Akko> ,<LocalDate: '01/12/2024'>
	 * Expected Outputs: HashMap: for every ItemType -> 0 orders
	 */
	@Test
	void generateMonthlyReport_Akko_Order_Dec24() {
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
			expected.put(t, 0);
		ReportType inputReport = ReportType.order;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2024, 12, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(expected,pdfSpy.processedOrders);
	}
	
	/**
	 * Test Name: generateMonthlyReport_NULL_Order_Dec21
	 * Description: Generating monthly order report for a NULL branch
	 * Inputs: <ReportType: order>,<branch: NULL> ,<LocalDate: '01/12/2021'>
	 * Expected Outputs: HashMap: every ItemType -> 0
	 */
	@Test
	void generateMonthlyReport_NULL_Order_Dec21() {
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
			expected.put(t, 0);
		ReportType inputReport = ReportType.order;
		String inputBranch = null;
		LocalDate inputDate = LocalDate.of(2021, 12, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(expected,pdfSpy.processedOrders);
	}
	
	/**
	 * Test Name: generateMonthlyReport_NULL_Order_Dec21
	 * Description: Generating monthly order report for a NULL date
	 * Inputs: <ReportType: order>,<branch: Akko> ,<LocalDate: '01/12/2021'>
	 * Expected Outputs: HashMap: every ItemType -> 0
	 */
	@Test
	void generateMonthlyReport_Akko_Order_NULL() {
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
			expected.put(t, 0);
		ReportType inputReport = ReportType.order;
		String inputBranch = "Akko";
		LocalDate inputDate = null;
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		try
		{
			ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
			assertEquals(expected,pdfSpy.processedOrders);
		}
		catch (NullPointerException e)
		{
			fail("Null date case is not handled");
		}
	}
	
	//quarterly
	/**
	 * Test Name: generateQuarterlyReport_Haifa_Order_March21
	 * Description: Generating quarterly order report for haifa in 1st quarter where there are no reports
	 * Inputs: <ReportType: order>,<branch: Haifa> ,<LocalDate: '01/01/2021' equivalent to 1st quarter of 2021>
	 * Expected Outputs: HashMap: every ItemType -> 0
	 */
	@Test
	void generateQuarterlyReport_Haifa_Order_March21() {
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
			expected.put(t, 0);
		ReportType inputReport = ReportType.order;
		String inputBranch = "Haifa";
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(expected,pdfSpy.processedOrders);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Lod_Order_March21
	 * Description: Generating quarterly order report for a branch that does not exist
	 * Inputs: <ReportType: order>,<branch: Lod> ,<LocalDate: '01/01/2021' equivalent to 1st quarter of 2021>
	 * Expected Outputs: HashMap: every ItemType -> 0
	 */
	@Test
	void generateQuarterlyReport_Lod_Order_March21() {
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
			expected.put(t, 0);
		ReportType inputReport = ReportType.order;
		String inputBranch = "Lod";
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(expected,pdfSpy.processedOrders);
	}
	
	/**
	 * Test Name: generateQuarterlyReport_Akko_Order_Dec21
	 * Description: Generating quarterly income report for a branch with data
	 * Inputs: <ReportType: order>,<branch: Akko> ,<LocalDate: '01/04/2021' equivalent to 4th quarter of 2021>
	 * Expected Outputs: HashMap itemType[i] -> {5,2,7,20,4,1,7}[i]
	 */
	@Test
	void generateQuarterlyReport_Akko_Order_Dec21() {
		Integer[] expectedOfEach = {12,7,21,42,13,5,18};
		int i = 0;
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
		{
			expected.put(t, expectedOfEach[i++]);
		}
		ReportType inputReport = ReportType.order;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2021, 4, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(expected,pdfSpy.processedOrders);
	}

	/**
	 * Test Name: generateQuarterlyReport_NULL_Order_Dec21
	 * Description: Generating Quarterly order report for a NULL branch
	 * Inputs: <ReportType: order>,<branch: NULL> ,<LocalDate: '01/04/2021' equivalent to 4th quarter of 2021>
	 * Expected Outputs: HashMap: every ItemType -> 0
	 */
	@Test
	void generateQuarterlyReport_NULL_Order_Dec21() {
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
			expected.put(t, 0);
		ReportType inputReport = ReportType.order;
		String inputBranch = null;
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(expected,pdfSpy.processedOrders);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Order_Income_Dec24
	 * Description: Generating quarterly order report for a future quarter
	 * Inputs: <ReportType: order>,<branch: Akko> ,<LocalDate: '01/01/2024' equivalent to 1st quarter>
	 * Outputs: HashMap: every ItemType -> 0
	 */
	@Test
	void generateQuarterlyReport_Akko_Order_Dec24() {
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
			expected.put(t, 0);
		ReportType inputReport = ReportType.order;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2024, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(expected,pdfSpy.processedOrders);
	}
	
	
	/**
	 * Test Name: generateQuarterlyReport_NULL_Order_Dec21
	 * Description: Generating quarterly order report for a NULL date
	 * Inputs: <ReportType: order>,<branch: Akko> ,<LocalDate: NULL>
	 * Outputs: HashMap: every ItemType -> 0
	 */
	@Test
	void generateQuarterlyReport_Akko_Order_NULL() {
		HashMap<ItemType,Integer> expected = new HashMap<>();
		for(ItemType t : ItemType.values())
			expected.put(t, 0);
		ReportType inputReport = ReportType.order;
		String inputBranch = "Akko";
		LocalDate inputDate = null;
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		try
		{
			ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
			assertEquals(expected,pdfSpy.processedOrders);
		}
		catch (NullPointerException e)
		{
			fail("Null date case is not handled");
		}
	}
	
	/*** Service Report ***/
	
	
	/**
	 * Test Name: generateMonthlyReport_Haifa_Service_March21
	 * Description: Generating monthly service report for haifa in march 21 where there are no reports
	 * Inputs: <ReportType: service>,<branch: Haifa> ,<LocalDate: '01/03/2021'>
	 * Expected Outputs: (X,Y) where X is dayOfYear [2021,12,1]~[2021,12,31], Y is all 0's
	 * 
	 */
	@Test
	void generateMonthlyReport_Haifa_Service_March21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();;
		for(int y = LocalDate.of(2021, 3, 1).getDayOfYear();y <= LocalDate.of(2021, 3, 31).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.service;
		String inputBranch = "Haifa";
		LocalDate inputDate = LocalDate.of(2021, 3, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedComplaintsX);
		assertEquals(YVals,pdfSpy.processedComplaintsY);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Lod_Service_March21
	 * Description: Generating monthly service report for a branch that does not exist
	 * Inputs: <ReportType: service>,<branch: Lod> ,<LocalDate: '01/03/2021'>
	 * Expected Outputs:(X,Y) where X is dayOfYear [2021,3,1]~[2021,3,31], Y is all 0's
	 */
	@Test
	void generateMonthlyReport_Lod_Service_March21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();;
		for(int y = LocalDate.of(2021, 3, 1).getDayOfYear();y <= LocalDate.of(2021, 3, 31).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.service;
		String inputBranch = "Lod";
		LocalDate inputDate = LocalDate.of(2021, 3, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedComplaintsX);
		assertEquals(YVals,pdfSpy.processedComplaintsY);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Akko_Service_Dec21
	 * Description: Generating monthly service report for a branch with data
	 * Inputs: <ReportType: service>,<branch: Akko> ,<LocalDate: '01/12/2021'>
	 * Expected Outputs: (X,Y) where X is dayOfYear [2021,12,1]~[2021,12,31], Y is {3, 0, 2, 0, 0, 1, 1, 2, 0, 3, 0, 2, 1, 0, 2,
	 * 																			 0, 0, 1, 0, 1, 0, 1, 1, 1, 4, 0, 0, 1, 2, 0, 0}
	 */
	@Test
	void generateMonthlyReport_Akko_Service_Dec21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		 YVals.addAll(Arrays.asList(new Integer[]{3, 0, 2, 0, 0, 1, 1, 2, 0, 3, 0, 2, 1, 0, 2, 0, 0, 1, 0, 1, 0, 1, 1, 1, 4, 0, 0, 1, 2, 0, 0}));
		for(int y = LocalDate.of(2021, 12, 1).getDayOfYear();y <= LocalDate.of(2021, 12, 31).getDayOfYear();y++)
			XVals.add(y);
		ReportType inputReport = ReportType.service;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2021, 12, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedComplaintsX);
		assertEquals(YVals,pdfSpy.processedComplaintsY);
	}

	/**
	 * Test Name: generateMonthlyReport_Akko_Service_Dec24
	 * Description: Generating monthly service report for a future month
	 * Inputs: <ReportType: service>,<branch: Akko> ,<LocalDate: '01/12/2024'>
	 * Expected Outputs: (X,Y) where X is dayOfYear [2021,12,1]~[2021,12,31], Y is all 0's
	 */
	@Test
	void generateMonthlyReport_Akko_Service_Dec24() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();;
		for(int y = LocalDate.of(2024, 12, 1).getDayOfYear();y <= LocalDate.of(2024, 12, 31).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.service;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2024, 12, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedComplaintsX);
		assertEquals(YVals,pdfSpy.processedComplaintsY);
	}
	
	/**
	 * Test Name: generateMonthlyReport_NULL_Service_Dec21
	 * Description: Generating monthly service report for a NULL branch
	 * Inputs: <ReportType: service>,<branch: NULL> ,<LocalDate: '01/12/2021'>
	 * Expected Outputs: (X,Y) where X is dayOfYear [2021,12,1]~[2021,12,31], Y is all 0's
	 */
	@Test
	void generateMonthlyReport_NULL_Service_Dec21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();;
		for(int y = LocalDate.of(2021, 12, 1).getDayOfYear();y <= LocalDate.of(2021, 12, 31).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.service;
		String inputBranch = null;
		LocalDate inputDate = LocalDate.of(2021, 12, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedComplaintsX);
		assertEquals(YVals,pdfSpy.processedComplaintsY);
	}
	
	/**
	 * Test Name: generateMonthlyReport_NULL_Service_Dec21
	 * Description: Generating monthly service report for a NULL date
	 * Inputs: <ReportType: service>,<branch: Akko> ,<LocalDate: '01/12/2021'>
	 * Expected Outputs: (X,Y) where X is dayOfYear [2021,12,1]~[2021,12,30], Y is all 0's
	 */
	@Test
	void generateMonthlyReport_Akko_Service_NULL() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();;
		for(int y = LocalDate.of(2021, 12, 1).getDayOfYear();y <= LocalDate.of(2021, 12, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.service;
		String inputBranch = "Akko";
		LocalDate inputDate = null;
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		try
		{
			ReportGenerator.GenerateMonthlyReport(inputReport, inputBranch, inputDate);
			assertEquals(XVals,pdfSpy.processedComplaintsX);
			assertEquals(YVals,pdfSpy.processedComplaintsY);
		}
		catch (NullPointerException e)
		{
			fail("Null date case is not handled");
		}
	}
	
	//quarterly
	/**
	 * Test Name: generateQuarterlyReport_Haifa_Service_March21
	 * Description: Generating quarterly service report for haifa in 1st quarter where there are no reports
	 * Inputs: <ReportType: service>,<branch: Haifa> ,<LocalDate: '01/01/2021' equivalent to 1st quarter of 2021>
	 * Expected Outputs:
	 */
	@Test
	void generateQuarterlyReport_Haifa_Service_March21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		for(int y = LocalDate.of(2021, 1, 1).getDayOfYear();y <= LocalDate.of(2021, 3, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.service;
		String inputBranch = "Haifa";
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedComplaintsX);
		assertEquals(YVals,pdfSpy.processedComplaintsY);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Lod_Service_March21
	 * Description: Generating quarterly service report for a branch that does not exist
	 * Inputs: <ReportType: service>,<branch: Lod> ,<LocalDate: '01/01/2021' equivalent to 1st quarter of 2021>
	 * Expected Outputs: (X,Y) where X is dayOfYear [2021,1,1]~[2021,3,30], Y is all 0's
	 */
	@Test
	void generateQuarterlyReport_Lod_Service_March21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		for(int y = LocalDate.of(2021, 1, 1).getDayOfYear();y <= LocalDate.of(2021, 3, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.service;
		String inputBranch = "Lod";
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedComplaintsX);
		assertEquals(YVals,pdfSpy.processedComplaintsY);
	}
	
	/**
	 * Test Name: generateQuarterlyReport_Akko_Service_Dec21
	 * Description: Generating quarterly service report for a branch with data
	 * Inputs: <ReportType: service>,<branch: Akko> ,<LocalDate: '01/04/2021' equivalent to 4th quarter of 2021>
	 * Outputs: (X,Y) where X is dayOfYear [2021,10,1]~[2021,12,30], Y is
 * 												  1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1,
												  0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1,
												  0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0,
												  0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0,
												  0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0,
												  0, 0, 3, 0, 2, 0, 0, 1, 1, 2, 0, 3,
												  0, 2, 1, 0, 2, 0, 0, 1, 0, 1, 0, 1,
												  1, 1, 4, 0, 0, 1, 2, 0}));
	 */
	@Test
	void generateQuarterlyReport_Akko_Service_Dec21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		YVals.addAll(Arrays.asList(new Integer[] {1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1,
												  0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1,
												  0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0,
												  0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0,
												  0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0,
												  0, 0, 3, 0, 2, 0, 0, 1, 1, 2, 0, 3,
												  0, 2, 1, 0, 2, 0, 0, 1, 0, 1, 0, 1,
												  1, 1, 4, 0, 0, 1, 2, 0}));
		for(int y = LocalDate.of(2021, 10, 1).getDayOfYear();y <= LocalDate.of(2021, 12, 30).getDayOfYear();y++)
			XVals.add(y);
		ReportType inputReport = ReportType.service;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2021, 4, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedComplaintsX);
		assertEquals(YVals,pdfSpy.processedComplaintsY);
	}

	/**
	 * Test Name: generateQuarterlyReport_NULL_Service_Dec21
	 * Description: Generating Quarterly service report for a NULL branch
	 * Inputs: <ReportType: service>,<branch: NULL> ,<LocalDate: '01/04/2021' equivalent to 4th quarter of 2021>
	 * Outputs: (X,Y) where X is dayOfYear [2021,1,1]~[2021,3,30], Y is all 0's
	 */
	@Test
	void generateQuarterlyReport_NULL_Service_Dec21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		for(int y = LocalDate.of(2021, 1, 1).getDayOfYear();y <= LocalDate.of(2021, 3, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.service;
		String inputBranch = null;
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedComplaintsX);
		assertEquals(YVals,pdfSpy.processedComplaintsY);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Service_Income_Dec24
	 * Description: Generating quarterly service report for a future quarter
	 * Inputs: <ReportType: service>,<branch: Akko> ,<LocalDate: '01/01/2024' equivalent to 1st quarter>
	 * Outputs: (X,Y) where X is dayOfYear [2024,1,1]~[2024,3,30], Y is all 0's
	 */
	@Test
	void generateQuarterlyReport_Akko_Service_Dec24() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		for(int y = LocalDate.of(2024, 1, 1).getDayOfYear();y <= LocalDate.of(2024, 3, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.service;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2024, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedComplaintsX);
		assertEquals(YVals,pdfSpy.processedComplaintsY);
	}
	
	
	/**
	 * Test Name: generateQuarterlyReport_NULL_Service_Dec21
	 * Description: Generating quarterly service report for a NULL date
	 * Inputs: <ReportType: service>,<branch: Akko> ,<LocalDate: NULL>
	 * Outputs: (X,Y) where X is dayOfYear [2021,1,1]~[2021,3,30], Y is all 0's
	 */
	@Test
	void generateQuarterlyReport_Akko_Service_NULL() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		for(int y = LocalDate.of(2021, 1, 1).getDayOfYear();y <= LocalDate.of(2021, 3, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.service;
		String inputBranch = "Akko";
		LocalDate inputDate = null;
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		try
		{
			ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
			assertEquals(XVals,pdfSpy.processedComplaintsX);
			assertEquals(YVals,pdfSpy.processedComplaintsY);
		}
		catch (NullPointerException e)
		{
			fail("Null date case is not handled");
		}
	}
	
	/*** CEO Reports ***/
	
	/**
	 * Test Name: generateQuarterlyReport_Haifa_CEO_March21
	 * Description: Generating quarterly ceo report for haifa in 1st quarter where there are no reports
	 * Inputs: <ReportType: ceo>,<branch: Haifa> ,<LocalDate: '01/01/2021' equivalent to 1st quarter of 2021>
	 * Expected Outputs:
	 */
	@Test
	void generateQuarterlyReport_Haifa_CEO_March21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		for(int y = LocalDate.of(2021, 1, 1).getDayOfYear();y <= LocalDate.of(2021, 3, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.ceo;
		String inputBranch = "Haifa";
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedCEOX);
		assertEquals(YVals,pdfSpy.processedCEOY);
	}
	
	/**
	 * Test Name: generateMonthlyReport_Lod_ceo_March21
	 * Description: Generating quarterly ceo report for a branch that does not exist
	 * Inputs: <ReportType: ceo>,<branch: Lod> ,<LocalDate: '01/01/2021' equivalent to 1st quarter of 2021>
	 * Expected Outputs: (X,Y) where X is dayOfYear [2021,1,1]~[2021,3,30], Y is all 0's
	 */
	@Test
	void generateQuarterlyReport_Lod_ceo_March21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		for(int y = LocalDate.of(2021, 1, 1).getDayOfYear();y <= LocalDate.of(2021, 3, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.ceo;
		String inputBranch = "Lod";
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedCEOX);
		assertEquals(YVals,pdfSpy.processedCEOY);
	}
	
	/**
	 * Test Name: generateQuarterlyReport_Akko_CEO_Dec21
	 * Description: Generating quarterly ceo report for a branch with data
	 * Inputs: <ReportType: ceo>,<branch: Akko> ,<LocalDate: '01/04/2021' equivalent to 4th quarter of 2021>
	 * Outputs: (X,Y) where X is dayOfYear [2021,10,1]~[2021,12,30], Y is
				{0, 484, 0, 0, 0, 869, 0, 0, 0,
				0, 0, 0, 358, 0, 824,
				0, 0, 0, 740, 0, 0, 0,
				607, 0, 648, 0, 0, 0,
				0, 318, 0, 0, 620, 0,
				0, 0, 151, 0, 0, 752,
				0, 0, 0, 0, 244, 0, 0,
				0, 0, 481, 0, 0, 0, 0,
				1074, 0, 0, 176, 0, 0,
				978, 1248, 0, 0, 1446,
				0, 0, 205, 0, 0, 0, 0,
				0, 482, 0, 0, 0, 0, 369,
				0, 0, 0, 237, 0, 0, 0,
				0, 301, 0, 0, 0}));
	 */
	@Test
	void generateQuarterlyReport_Akko_CEO_Dec21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		YVals.addAll(Arrays.asList(new Integer[] {0, 484, 0, 0, 0, 869, 0, 0, 0,
				0, 0, 0, 358, 0, 824,
				0, 0, 0, 740, 0, 0, 0,
				607, 0, 648, 0, 0, 0,
				0, 318, 0, 0, 620, 0,
				0, 0, 151, 0, 0, 752,
				0, 0, 0, 0, 244, 0, 0,
				0, 0, 481, 0, 0, 0, 0,
				1074, 0, 0, 176, 0, 0,
				978, 1248, 0, 0, 1446,
				0, 0, 205, 0, 0, 0, 0,
				0, 482, 0, 0, 0, 0, 369,
				0, 0, 0, 237, 0, 0, 0,
				0, 301, 0, 0, 0}));
		for(int y = LocalDate.of(2021, 10, 1).getDayOfYear();y <= LocalDate.of(2021, 12, 30).getDayOfYear();y++)
			XVals.add(y);
		ReportType inputReport = ReportType.ceo;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2021, 4, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedCEOX);
		assertEquals(YVals,pdfSpy.processedCEOY);
	}

	/**
	 * Test Name: generateQuarterlyReport_NULL_CEO_Dec21
	 * Description: Generating Quarterly ceo report for a NULL branch
	 * Inputs: <ReportType: ceo>,<branch: NULL> ,<LocalDate: '01/04/2021' equivalent to 4th quarter of 2021>
	 * Outputs: (X,Y) where X is dayOfYear [2021,1,1]~[2021,3,30], Y null
	 */
	@Test
	void generateQuarterlyReport_NULL_CEO_Dec21() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		for(int y = LocalDate.of(2021, 1, 1).getDayOfYear();y <= LocalDate.of(2021, 3, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.ceo;
		String inputBranch = null;
		LocalDate inputDate = LocalDate.of(2021, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedCEOX);
		assertEquals(YVals,pdfSpy.processedCEOY);
	}
	
	/**
	 * Test Name: generateMonthlyReport_CEO_Income_Dec24
	 * Description: Generating quarterly ceo report for a future quarter
	 * Inputs: <ReportType: ceo>,<branch: Akko> ,<LocalDate: '01/01/2024' equivalent to 1st quarter>
	 * Outputs: (X,Y) where X is dayOfYear [2024,1,1]~[2024,3,30], Y is all 0's
	 */
	@Test
	void generateQuarterlyReport_Akko_CEO_Dec24() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		for(int y = LocalDate.of(2024, 1, 1).getDayOfYear();y <= LocalDate.of(2024, 3, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.ceo;
		String inputBranch = "Akko";
		LocalDate inputDate = LocalDate.of(2024, 1, 1);
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
		assertEquals(XVals,pdfSpy.processedCEOX);
		assertEquals(YVals,pdfSpy.processedCEOY);
	}
	
	
	/**
	 * Test Name: generateQuarterlyReport_NULL_CEO_Dec21
	 * Description: Generating quarterly ceo report for a NULL date
	 * Inputs: <ReportType: ceo>,<branch: Akko> ,<LocalDate: NULL>
	 * Outputs: (X,Y) where X is dayOfYear [2021,1,1]~[2021,3,30], Y is all 0's
	 */
	@Test
	void generateQuarterlyReport_Akko_CEO_NULL() {
		ArrayList<Integer> XVals = new ArrayList<>();
		ArrayList<Integer> YVals = new ArrayList<>();
		for(int y = LocalDate.of(2021, 1, 1).getDayOfYear();y <= LocalDate.of(2021, 3, 30).getDayOfYear();y++)
		{
			YVals.add(0);
			XVals.add(y);
		}
		ReportType inputReport = ReportType.ceo;
		String inputBranch = "Akko";
		LocalDate inputDate = null;
		ReportGenerator.SetPdfGenerationService(pdfSpy);
		ReportGenerator.SetLoggingService(logger);
		try
		{
			ReportGenerator.GenerateQuarterlyReport(inputReport, inputBranch, inputDate);
			assertEquals(XVals,pdfSpy.processedCEOX);
			assertEquals(YVals,pdfSpy.processedCEOY);
		}
		catch (NullPointerException e)
		{
			fail("Null date case is not handled");
		}
	}
}
