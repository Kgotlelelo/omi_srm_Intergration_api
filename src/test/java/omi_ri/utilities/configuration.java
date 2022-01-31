package omi_ri.utilities;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class configuration extends baseTest {

	private static JsonNode node;
	private static ArrayNode arrNode;
	private static String sFilePath;
	private static File sFile;
	private static String testEnvironment;
	private static String testDataPath;
	private static String testName;
	private static String testURL;
	private static String testResultFolder;
	private static String testStatus;
	private static String executionSheet;
	private static String fileLocation;
	private static Map<String, String> mapEnvironment;
	private static int testCount;


	public configuration(String FileName) {
		sFilePath = FileName;
	}

	public Map<String,String> getConfiguration(String className,String sheetName) {
		try {
			//sFile = new File(sFilePath);
			String content = Files.toString(new File(sFilePath), Charsets.UTF_8);
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = new JsonFactory();
			List<String> res = null;
			JsonParser parser = factory.createJsonParser(content);
			node = mapper.readTree(parser);
			testCount = 0;

	

			arrNode = (ArrayNode) node.get("tests");
			mapEnvironment = new HashMap<String, String>();

			if (arrNode.isArray()) {
				for (final JsonNode objNode : arrNode) {

					testName = objNode.findValue("TestName").toString().replace("\"", "");
					testDataPath = objNode.findValue("TestDataPath").toString().replace("\"", "");
					testEnvironment = objNode.findValue("TestEnvironment").toString().replace("\"", "");
					testStatus = objNode.findValue("Status").toString().replace("\"", "");
					testURL = objNode.findValue("URL").toString().replace("\"", "");
					testResultFolder = objNode.findValue("TestResults").toString().replace("\"", "");
					executionSheet = objNode.findValue("ExecutionSheet").toString().replace("\"", "");
					fileLocation = objNode.findValue("FileLocation").toString().replace("\"", "");
					if ((testName.equals(className) && (testStatus.equals("Enabled")))) {
						mapEnvironment.put("testName", testName);
						mapEnvironment.put("testDataPath", testDataPath);
						mapEnvironment.put("testEnvironment", testEnvironment);
						mapEnvironment.put("testStatus", testStatus);
						mapEnvironment.put("testURL", testURL);
						mapEnvironment.put("testResults", testResultFolder);
						mapEnvironment.put("executionSheet", executionSheet);
						mapEnvironment.put("fileLocation", fileLocation);
						testCount++;
						break;
					}
				}

				if (testCount > 1) {
					System.out.println("Only one environment can be executed at a time for test '"+className.trim().toUpperCase()+"'. Please update the config.json file accordingly");
					System.out.println("The test was terminated");
					//System.exit(1);
				} else if (testCount == 0) {
					System.out.println("Either the configuration is Disabled or there is no entry in the configuration file for '"+className.trim().toUpperCase()+"' test");
					System.out.println("The test was terminated");
					//System.exit(1);
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("The configuration file is not found. Make sure the path is correct");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mapEnvironment;
	}

	/*public static void main(String[] args) {
		Configuration conf = new Configuration("src/main/java/config/config.json");
		Map<String, String> test = new HashMap<>();

		test = conf.getConfiguration("Configuration","");

		System.out.println(test.get("testDataPath"));
	}*/
}



