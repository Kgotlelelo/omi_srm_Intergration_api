package omi_ri.utilities;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.naming.AuthenticationException;
import javax.ws.rs.client.Entity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("ALL")
public class JTricksRESTClient extends baseTest {
	private static String cycleFilePath, TestDescription, cycleID, projectKey, projectID, testID, testKey,
			executionID, requiredProjectKey, BASE_URL, versionID, auth, cycleName;
	private static Map<String, String> CycleDateMap;

	public JTricksRESTClient() {

		//Get JIRA connection details
		initialiseVariables();

		//Initialise the project details
		initialiseProjectId();

		/*if (cycleID == null || cycleID.trim().isEmpty()) {
			if (FileUtility.fileExists(cycleFilePath)) {
				try {
					cycleID = ((FileUtility.fileAsString(cycleFilePath).replaceAll("\n", ""))
							.replaceAll("\r", "")).trim();
					if(cycleID==null || cycleID.trim().isEmpty()){
						createJIRATestCycle();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				createJIRATestCycle();
			}

		}*/
	}

	private void initialiseVariables() {
		try {
			BASE_URL = jiraURL;
			auth = new String(Base64.encode(jiraLoginUserName + ":" + jiraLoginPassword));
			versionID = jiraReleaseVersion;
			requiredProjectKey = jiraProjectKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getCycleID() {
		return cycleID;
	}

	public void addTestToCycle() {
		addTestToJIRATestCycle();
	}

	public void addAttachmentToJira(String attachmentPath, String TestName) {
		try {

			String testIDnum = getJIRATestID(TestName);
			if (testIDnum == "") {
				System.out.println("Test Named '" + TestName + "' was not found on the cycle. The new test will be created.");
				createTestCase(TestName);
				addTestToCycle();
				testIDnum = testID;
			}

			int expirationInSec = 360;
			File file = new File(attachmentPath);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(BASE_URL + "/rest/api/latest/issue/" + testIDnum + "/attachments");
//			HttpPost httppost = new HttpPost(BASE_URL+"/rest/api/latest/attachment?entityId=" + testStepID  + "=&entityType=stepResult");

			httppost.setHeader("X-Atlassian-Token", "no-check");
			httppost.setHeader("Authorization", "Basic " + auth);
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			FileBody fileBody = new FileBody(file, "application/octet-stream");
			entity.addPart("file", fileBody);

			httppost.setEntity(entity);
			HttpResponse response = httpclient.execute(httppost);
			System.out.println("Attachment '" + attachmentPath.toUpperCase() + "' added to the test with JIRA ID number : " + testIDnum);
			int statusCode = response.getStatusLine().getStatusCode();


		} catch (FileNotFoundException e) {
			System.out.println("Attachment named: " + attachmentPath + " was not found hence it could not be uploaded to JIRA..");
		} catch (ClientProtocolException cpe) {
			System.out.println("Attachment named: " + attachmentPath + " could not be uploaded to JIRA. Exception displayed is " + cpe);
		} catch (IOException ioe) {
			System.out.println("Attachment named: " + attachmentPath + " could not be uploaded to JIRA. Exception received is " + ioe);
		}
	}

	public String getJIRATestID(String TestName) {
		String stestID = "";
		try {

			//Cycle URS
			String cycleUrl = BASE_URL + "/rest/zapi/latest/execution?projectId=" + projectID + "&versionId=" + versionID + "&cycleId=" + cycleID;
			String result = invokeGetMethod(auth, cycleUrl);
			JsonNode node;
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(result);
			node = mapper.readTree(parser);
			ArrayNode executionNode;
			executionNode = (ArrayNode) node.get("executions");
			String[] issuesArray;
			ArrayList issuesList = new ArrayList();
			int count = 0;
			int iSize = executionNode.size();
			for (final JsonNode objNode : executionNode) {
				String TestSummary = String.valueOf(objNode.findValue("summary")).replaceAll("\"", "");
				//System.out.println(TestSummary);
				if (TestSummary.trim().equals(TestName)) {
					stestID = String.valueOf(objNode.findValue("issueId")).replaceAll("\"", "");
					break;
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return stestID;
	}

	public boolean JIRATestExistsInCycle(String Key) {
		String stestID = "";
		try {

			//Cycle URS
			String cycleUrl = BASE_URL + "/rest/zapi/latest/execution?projectId=" + projectID + "&versionId=" + versionID + "&cycleId=" + cycleID;
			String result = invokeGetMethod(auth, cycleUrl);
			JsonNode node;
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(result);
			node = mapper.readTree(parser);
			ArrayNode executionNode;
			executionNode = (ArrayNode) node.get("executions");
			String[] issuesArray;
			ArrayList issuesList = new ArrayList();
			int count = 0;
			int iSize = executionNode.size();
			for (final JsonNode objNode : executionNode) {
				String TestKey = String.valueOf(objNode.findValue("issueKey")).replaceAll("\"", "");
				String TestName = String.valueOf(objNode.findValue("summary")).replaceAll("\"", "");
				if (TestKey.trim().equals(Key)) {
					//System.out.println("The test named : "+TestName+" exists on the current cycle hence it wont be added.");
					return true;
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void createTestCase(String TestSummary) {
		//Create a Test issue
		TestDescription = "Scenario added using automated script.";
		createAJiraTestIssue(TestSummary, TestDescription);
	}

	public int getNumberOfTestsOnACycle() {
		String stestID = "";
		try {

			//Cycle URS
			String cycleUrl = BASE_URL + "/rest/zapi/latest/execution?projectId=" + projectID + "&versionId=" + versionID + "&cycleId=" + cycleID;
			String result = invokeGetMethod(auth, cycleUrl);
			JsonNode node;
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(result);
			node = mapper.readTree(parser);
			ArrayNode executionNode;
			executionNode = (ArrayNode) node.get("executions");
			String[] issuesArray;
			ArrayList issuesList = new ArrayList();
			int count = 0;
			return executionNode.size();


		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	public void copyTestCasesFromOneCycleToAnother(String cycleID, String newCycleName) {
		try {
			//Cycle URS
			String cycleUrl = BASE_URL + "/rest/zapi/latest/execution?projectId=" + projectID + "&versionId=" + versionID + "&cycleId=" + cycleID;
			String result = invokeGetMethod(auth, cycleUrl);
			JsonNode node;
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(result);
			node = mapper.readTree(parser);
			ArrayNode executionNode;
			executionNode = (ArrayNode) node.get("executions");
			String[] issuesArray;
			ArrayList issuesList = new ArrayList();
			int count = 0;
			for (final JsonNode objNode : executionNode) {
				String issueKey = String.valueOf(objNode.findValue("issueKey"));
				issuesList.add(issueKey.replaceAll("\"", ""));
			}

			issuesArray = new String[issuesList.size()];
			issuesList.toArray(issuesArray);
			createJIRATestCycle(newCycleName, true);
			addTestToJIRATestCycle(issuesArray);

			int num = getNumberOfTestsOnACycle();

			//Get the expected number of test cases that must be cloned from another cycle
			int counterIndex = Integer.parseInt(data.ReadTextFile(sDefaultPath + "\\JiraFiles\\Counter.txt"));

			while (num < counterIndex) {
				num = getNumberOfTestsOnACycle();
			}

			//Update the text file after cloning the tests
			String testsCloned = "true";
			data.WriteTextFile(sDefaultPath + "\\JiraFiles\\JiraTestsCloned.txt", testsCloned);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTestExecutionStatus(String TestName, String status) {
		try {

			String testIDnum = getJIRATestID(TestName);
			if (testIDnum == "") {
				System.out.println("Test Named '" + TestName + "'was not found on the cycle. The new test will be created.");
				createTestCase(TestName);
				addTestToCycle();
				testIDnum = testID;
			}
			String getIssueURL = "/rest/zapi/latest/execution?issueId=" + testIDnum;
			String issueUrl = BASE_URL + getIssueURL;
			String result = invokeGetMethod(auth, issueUrl);
			JsonNode node;
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(result);
			node = mapper.readTree(parser);
			ArrayNode executionNode;
			executionNode = (ArrayNode) node.get("executions");
			executionID = String.valueOf(executionNode.get(0).findValue("id"));


			String url = "/rest/zapi/latest/execution/" + executionID + "/execute";
			String statusInReq = "";
			if (status.equalsIgnoreCase("pass")) {
				statusInReq = "1";
			} else {
				statusInReq = "2";
			}
			String createIssueData = "{\"status\": \"" + statusInReq + "\",\"comment\": \"Updated Through JAVA\"}";
			invokePutMethod(auth, BASE_URL + url, createIssueData);
			System.out.println("Execution Status Updated for test ID : " + testIDnum);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createJIRATestCycle(String cycleName, boolean saveCycleID) {
		try {

			if (cycleExists(cycleName)) {
				return;
			}
			//Generate the Start and End dates
			getTodayDate();
			String startDate = CycleDateMap.get("Start Date");
			String endDate = CycleDateMap.get("End Date");

			//Populate the task details
			//
			//cycleName = "M2 Automated Regression dated : " + startDate + " to " + endDate;
			String createIssueData = "{\"name\": \" " + cycleName + "\",  \"build\": \"\",  \"environment\": \"SAGE\",  \"description\": \"Create cycle with automated script\",  \"startDate\": \"" + startDate + "\",  \"endDate\": \"" + endDate + "\",  \"projectId\": \"" + projectID + "\",  \"versionId\": \"" + versionID + "\"}";
			//Create a cycle
			String cycle = invokePostMethod(auth, BASE_URL + "/rest/zapi/latest/cycle", createIssueData);

			//String projects = invokeGetMethod(auth, BASE_URL+"/rest/zapi/latest/cycle?projectId=16201");
			//Verify the created cycle results
			JsonNode node;
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(cycle);
			node = mapper.readTree(parser);
			ArrayNode arrNode;
			cycleID = String.valueOf(node.get("id"));
			//Replace double quotes on the cycleID
			cycleID = cycleID.replaceAll("\"", "");
			if (saveCycleID) {
				//Update the text file after creating a cycle
				String testsCycleID = cycleID;
				data.WriteTextFile(cycleIDFilePath, testsCycleID);
			}
			System.out.println("New execution cycle created. The id of the cycle is " + cycleID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialiseProjectId() {
		try {
			if (projectID == null) {
				String projects = invokeGetMethod(auth, BASE_URL + "/rest/api/2/project");
				JsonNode node;
				ObjectMapper mapper = new ObjectMapper();
				JsonFactory factory = new JsonFactory();
				List<String> res = null;
				JsonParser parser = factory.createJsonParser(projects);
				node = mapper.readTree(parser);
				JSONArray projectsArray = new JSONArray(projects);
				for (int i = 0; i < projectsArray.length(); i++) {
					JSONObject proj = projectsArray.getJSONObject(i);
					projectKey = proj.getString("key");
					if (projectKey.trim().equalsIgnoreCase(requiredProjectKey)) {
						System.out.println("Project Key:" + proj.getString("key") + ", Project Name:" + proj.getString("name"));
						projectID = proj.getString("id");
						//Replace double quotes on the projectID
						projectID = projectID.replaceAll("\"", "");
						break;
					}

				}
				System.out.println("Project Loaded...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createAJiraTestIssue(String TestSummary, String TestDescription) {
		try {

			String createIssueData = "{\"fields\": {\"project\": {\"key\": \""+requiredProjectKey+"\"},\"summary\": \"" + TestSummary + "\",\"description\": \"" + TestDescription + "\", \"issuetype\": {\"name\": \"Test\"}}}";
			String response = invokePostMethod(auth, BASE_URL + "/rest/api/2/issue/", createIssueData);
			JsonNode node;
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(response);
			node = mapper.readTree(parser);
			ArrayNode arrNode;
			testID = String.valueOf(node.get("id")).replaceAll("\"", "");
			testKey = String.valueOf(node.get("key")).replaceAll("\"", "");
			System.out.println("Test Issue Created! New Key is: " + testKey);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addTestToJIRATestCycle() {
		try {

			String issues = "[\"" + testKey + "\"]";
			//\"folderId\" : \""+folderID+"\",
			String createIssueData = "{\"issues\": " + issues + ", \"cycleId\": \"" + cycleID + "\",  \"method\": \"1\",  \"projectId\": \"" + projectID + "\",  \"versionId\": \"" + versionID + "\"}";
			Entity payload = Entity.json("{  \"components\": \"\",  \"cycleId\": \"-1\",  \"fromCycleId\": \"3\",  \"fromVersionId\": \"10003\",  \"hasDefects\": false,  \"labels\": \"\",  \"method\": \"3\",  \"priorities\": \"\",  \"projectId\": \"10000\",  \"statuses\": \"\",  \"versionId\": \"10003\",  \"folderId\": 12313}");
			String result = invokePostMethod(auth, BASE_URL + "/rest/zapi/latest/execution/addTestsToCycle", createIssueData);
			JsonNode node;
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(result);
			node = mapper.readTree(parser);
			ArrayNode arrNode;
			arrNode = (ArrayNode) node.get("IssueStatusesOptionsList");
			//System.out.println(result);
			//System.out.println("Test ID: " + testID + " was added to the cycleID : "+cycleID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addTestToJIRATestCycle(String[] issues) {
		try {

			JSONArray jsArray = new JSONArray();
			for (int i = 0; i < issues.length; i++) {
				boolean testAlreadyExists = JIRATestExistsInCycle(issues[i]);
				if(!testAlreadyExists){
					jsArray.put(issues[i]);
				}
			}
			if(jsArray.length()==0)return;

			String issuesData = jsArray.toString();
			//\"folderId\" : \""+folderID+"\",
			String createIssueData = "{\"issues\": " + jsArray + ", \"cycleId\": \"" + cycleID + "\",  \"method\": \"1\",  \"projectId\": \"" + projectID + "\",  \"versionId\": \"" + versionID + "\"}";
			Entity payload = Entity.json("{  \"components\": \"\",  \"cycleId\": \"-1\",  \"fromCycleId\": \"3\",  \"fromVersionId\": \"10003\",  \"hasDefects\": false,  \"labels\": \"\",  \"method\": \"3\",  \"priorities\": \"\",  \"projectId\": \"10000\",  \"statuses\": \"\",  \"versionId\": \"10003\",  \"folderId\": 12313}");
			String result = invokePostMethod(auth, BASE_URL + "/rest/zapi/latest/execution/addTestsToCycle", createIssueData);
			JsonNode node;
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(result);
			node = mapper.readTree(parser);
			ArrayNode arrNode;
			arrNode = (ArrayNode) node.get("IssueStatusesOptionsList");
			String projectID = "";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean cycleExists(String cycleName) {
		try {
			//Replace double quotes on the projectID
			projectID = projectID.replaceAll("\"", "");
			String projects = invokeGetMethod(auth, BASE_URL + "/rest/zapi/latest/cycle?projectId=" + projectID);
			JsonNode node, node2;
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(projects);
			node = mapper.readTree(parser);

			ObjectNode objectNode = (ObjectNode) node;
			Iterator<Map.Entry<String, JsonNode>> iter = objectNode.getFields();


			while (iter.hasNext()) {
				Map.Entry<String, JsonNode> entry = iter.next();
				//Read the version ID
				String currentReleaseVersion = entry.getKey();
				//if the currently read version is the same as the currently used version then search for the cycle
				if (versionID.trim().equalsIgnoreCase(currentReleaseVersion.trim())) {
					ArrayNode cycleNodes = (ArrayNode) entry.getValue();
					//Iterate through the cycles to search for the required cycle
					for (JsonNode objNode : cycleNodes) {
						Iterator<Map.Entry<String, JsonNode>> cycleFields = objNode.getFields();
						String currentCycleName = "";
						while (cycleFields.hasNext()) {
							Map.Entry<String, JsonNode> cycleFieldMap = cycleFields.next();
							try{
								currentCycleName = cycleFieldMap.getValue().findValue("name").toString().replaceAll("\"", "");//get the cycle name
							}catch (NullPointerException e){
								e.getMessage();
							}

							if (currentCycleName.trim().equals(cycleName.trim())) {
								cycleID = cycleFieldMap.getKey().replaceAll("\"", "");
								System.out.println("Execution Cycle named " + cycleName + " already exists, the cycle ID is " + cycleID + ". The existing cycle will be reused.");
								return true;
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String invokeGetMethod(String auth, String url) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").get(ClientResponse.class);
		int statusCode = response.getStatus();
		if(statusCode == 401){
			System.out.println("Invalid Username or Password " +statusCode);
			throw new AuthenticationException("Invalid Username or Password");
		}
		return response.getEntity(String.class);
	}

	private static String invokePostMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").post(ClientResponse.class, data);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
		return response.getEntity(String.class);
	}

	private static void invokePutMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").put(ClientResponse.class, data);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
	}

	private void getTodayDate() {
		if (CycleDateMap == null) {
			CycleDateMap = new HashMap<>();
			//Setting the date to the given date
			try {

				Calendar cal = Calendar.getInstance();
				SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
				//System.out.println(dateOnly.format(cal.getTime()));
				String currentDate = dateOnly.format(cal.getTime());
				//Setting the date to the given date
				cal.setTime(dateOnly.parse(currentDate));

				//Number of Days to add
				cal.add(Calendar.DAY_OF_MONTH, 7);
				//Date after adding the days to the given date
				String endDate = dateOnly.format(cal.getTime());
				//Format the date details
				String[] startDateArray = currentDate.split("/");
				DateFormatSymbols dfs = new DateFormatSymbols();
				String[] shortMonths = dfs.getShortMonths();
				String startMonthName = shortMonths[Integer.parseInt(startDateArray[0]) - 1];

				String[] endDateArray = endDate.split("/");
				String endMonthName = "";

				//check if moths must be different
				if (Integer.parseInt(startDateArray[1]) > Integer.parseInt(endDateArray[1])) {
					endMonthName = shortMonths[Integer.parseInt(startDateArray[0])];
				} else {
					endMonthName = shortMonths[Integer.parseInt(startDateArray[0]) - 1];
				}
				endDate = endDateArray[1] + "/" + endMonthName
						+ "/" + StringUtils.right(endDateArray[2], 2);
				currentDate = startDateArray[1] + "/" + startMonthName
						+ "/" + StringUtils.right(startDateArray[2], 2);
				CycleDateMap.put("Start Date", currentDate);
				CycleDateMap.put("End Date", endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}
}
