package omi_ri.utilities;

import com.codoid.products.fillo.Recordset;
import org.apache.poi.ss.usermodel.Sheet;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class constants {

	public static String sDefaultPath = System.getProperty("user.dir");
	//JIRA variables

	//Link to jira
	public static final String jiraURL = "";
	public static final String jiraLoginUserName = "";
	public static final String jiraLoginPassword = "";
	public static final String jiraProjectKey = "";
	public static final String cycleIDFilePath = String.valueOf(Paths.get(sDefaultPath,"/JiraFiles/JIRACycle.txt"));
	public static final String defaultCycleName= "";
	public static String defaultCycleID;
	public static boolean jiraTestsCopied;

	public static final String jiraReleaseVersion = ""; //Sprint
	public static final String newCycleName= "Sprint5";//Sprint
	public static final String defaultCycleIDFilePath = String.valueOf(Paths.get(sDefaultPath,"/JiraFiles/DefaultCycleSprint.txt"));

	//public static final String jiraReleaseVersion = "48157"; //WeeklyRun
	//public static final String newCycleName= "Weekly Executions";//Weekly
	//public static final String defaultCycleIDFilePath = String.valueOf(Paths.get(sDefaultPath,"/JiraFiles/DefaultCycleRegression.txt"));

	//Test variables
	public static final String strWebTestEnvURL = "";
	public static final String strGmail = "https://mail.google.com/mail/u/0/?tab=km#inbox";
	public static final String strOutLookl = "";
	public static final String APIWebData = sDefaultPath+"/testData/webData/API.xlsx";

	public static String testResult;
	public static String reportPath;
	public static String finalResult;
	public static boolean testHasFailed;


	public static HashMap<String,String> testHeaderMap = new HashMap<>();
	public static HashMap<String,String> testDataMap = new HashMap<>();

	public static int count = 1;
	protected int iRow;
	protected Sheet sheet;
	protected Recordset record;
	protected ResultSet resultset;
	protected String sDataType;
	public static String JiraTestName;
	public static Map<String, List<String>> scenariosMap;
	public static Map<String, String> testResults;
	public static Map<String, String> testReportMap;
	public static final String passedState = "pass";

	public static String serviceUrl;
}
