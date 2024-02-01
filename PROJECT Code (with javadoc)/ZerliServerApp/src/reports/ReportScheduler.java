package reports;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Entities.ReportType;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import server.Server;

// TODO: Auto-generated Javadoc
/**
 * The Class ReportScheduler is the automated report generator task
 * used to generate reports when enough data was gathered.
 */
public class ReportScheduler {
	
	/** The zerli debug. */
	private final boolean ZERLI_DEBUG = false;
	
	/**  The dawn of time is used to determine from when to check for missing reports. */
	private final LocalDate dawnOfTime = LocalDate.of(2021, 1, 1);
	
	/**  The quarter marks are used for designating the beginning of each quarter. */
	private final List<Integer> QUARTER_MARKS = Arrays.asList(new Integer[] {1,4,7,10});
	
	/**  The branches of the Zerli franchise. */
	ArrayList<String> branches;
	
	/**
	 *  This method will compensate missing reports due to server being down 
	 *  this is done by searching for Inconsistencies in the database
	 *  when the compensator locates an inconsistency it will generate report to fill the gap*.
	 */
	public void Compensate()
	{
		Server.Log("R-Scheduler", "Getting Branches");
		branches = Server.SqlServerManager.GetBranches();
		Server.Log("R-Scheduler", "Compensation begun");
		for(int year = dawnOfTime.getYear();year <= LocalDate.now().getYear();year++)
		{
			for(int month = 1;month <= 12;month++)
			{
				
				if(LocalDate.of(year, month, 1).isEqual(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(),1))) break;
				/*Check Report exists both q && m for Income/Orders/Service [2 * 3 * duration_between(dawn,now).inMonths * #branches]*/
				for(String branch : branches)
				{
					Server.Log("R-Scheduler", "Iterating now: " + month + "/" + year + ", Branch: " + branch);	
					for(ReportType t : ReportType.values())
					{
						boolean rM = Server.SqlServerManager.ReportExists(t,true,branch,java.sql.Date.valueOf(LocalDate.of(year, month, 1)));
						if(!rM)
						{
							Server.Log("R-Scheduler", "Monthly Inconsistency detected: " + month + "/" + year + ", Branch: " + branch + " Report: " + t.toString());
							ReportGenerator.GenerateMonthlyReport(t, branch, LocalDate.of(year, month, 1));
						}
					}
				}
			}
		}
		
		for(int year = dawnOfTime.getYear();year <= LocalDate.now().getYear();year++)
		{
			for(int quarter = 1;quarter<=4;quarter++)
			{
				for(String branch : branches)
				{
					for(ReportType t : ReportType.values())
					{
						if(year == LocalDate.now().getYear())
							if((((LocalDate.now().getMonthValue()-1) / 3) + 1) <= quarter) break;
						boolean rQ = Server.SqlServerManager.ReportExists(t,false,branch,java.sql.Date.valueOf(LocalDate.of(year, quarter, 1)));
						if(!rQ)
						{
							Server.Log("R-Scheduler", "Quarterly Inconsistency detected: " + quarter + "/" + year + ", Branch: " + branch + " Report: " + t.toString());
							ReportGenerator.GenerateQuarterlyReport(t, branch, LocalDate.of(year, quarter, 1));
						}
					}
				}
			}
		}
//		for(int year = dawnOfTime.getYear();year <= LocalDate.now().getYear();year++)
//		{
//			for(int quarter = 1;quarter<=4;quarter++)
//			{
//
//				if(year == LocalDate.now().getYear())
//					if((LocalDate.now().getMonthValue() / 3) + 1 < quarter) break;
//				boolean rQ = Server.SqlServerManager.ReportExists(ReportType.ceo,false,"*",java.sql.Date.valueOf(LocalDate.of(year, quarter, 1)));
//				if(!rQ)
//				{
//					Server.Log("R-Scheduler", "CEO Report Inconsistency detected: " + quarter + "/" + year);
//					ReportGenerator.GenerateQuarterlyReport(ReportType.ceo, "*", LocalDate.of(year, quarter, 1));
//				}
//			}
//		}	
	}
	
	/**
	 *  This method will periodically (monthly basis) generate reports
	 *  this method also generates quarterly reports if the date starts a new quarter.
	 */
	public void Interrupt()
	{
		Compensate();
		/*Server.Log("R-Scheduler", "Generation Progress Begun");
		branches = Server.SqlServerManager.GetBranches();
		for(String branch : branches) //branches * 6 or 3 [q/m]
		{
			for(ReportType t : ReportType.values())
			{
				Server.Log("R-Scheduler", "Generating Monthly Report for " + branch + " Type: " + t.toString());
				ReportGenerator.GenerateMonthlyReport(t, branch, LocalDate.now());
				
				if(QUARTER_MARKS.contains(LocalDate.now().getMonthValue()) || ZERLI_DEBUG)
				{
					Server.Log("R-Scheduler", "Generating Quarterly Report for " + branch + " Type: " + t.toString());
					ReportGenerator.GenerateQuarterlyReport(t, branch, LocalDate.now());
				}
			}
		}*/
	}
	
	/**
	 * Runs the report scheduler automated task
	 * This will run the compensator and when the compensator is done
	 * the Interrupt scheduler will step into action.
	 */
	public void Run()
	{
		Compensate();
		boolean x = false;
		while(x)
		{
			/** wait for generation period **/
			while(LocalDate.now().getDayOfMonth() != 1)
			{
				Server.Log("R-Scheduler", "Polling Reports");
				if(!ZERLI_DEBUG)
					try {
						Thread.sleep((1000 * 3600) * 12);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
					try {
						Thread.sleep(10 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //10 seconds
			}
			Compensate();
		}
	}
}
