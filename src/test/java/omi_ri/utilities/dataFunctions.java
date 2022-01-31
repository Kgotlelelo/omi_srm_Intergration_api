package omi_ri.utilities;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dataFunctions {

    public FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-ZA"), new RandomService());
    public Faker faker = new Faker(new Locale("en-ZA"));

    private String sWebURL;
    private String sPassword;
    private String sUserID;
    private String sAppLoc;
    private String sAppUsername;
    private String sAppPassword;
    private String sApi;
    private String sDataLocation;
    private String sDataType;
    private String sDBPassword;
    private String sDBUsername;
    private String sEmail;
    private String sEmailFrom;
    private String sEmailTo;
    private String sDBHost;
    private String sBrowser;
    private String sTestManagement;
    private String sTestMURL;
    private String sTestMUsername;
    private String sTestMPassword;
    private String sDomain;
    private String sProject;
    private String sReportName;
    private String sAppendReport;
    private String sbrowserDrivers;
    public Connection connect;
    public java.sql.Connection conn = null;

    ArrayList<String> lines = new ArrayList<String>();

    //Excel variables
    public Sheet sheet;
    public Workbook workbook;
    int fillonum = 1;
    int count = 1;

    public String getWebURL() {
        return sWebURL;
    }

    public String getWebPassword() {
        return sPassword;
    }

    public String getWebUserName() {
        return sUserID;
    }

    public String getWindowsAppLocation() {
        return sAppLoc;
    }

    public String getWindowsAppUsername() {
        return sAppUsername;
    }

    public String getWindowsAppPassword() {
        return sAppPassword;
    }

    public String getAPI() {
        return sApi;
    }

    public String getDataLocation() {
        return sDataLocation;
    }

    public String getDBHost() {
        return sDBHost;
    }

    public String getDBUsername() {
        return sDBUsername;
    }

    public String getDBPassword() {
        return sDBPassword;
    }

    public String getDataType() {
        return sDataType;
    }

    public String getSendEmail() {
        return sEmail;
    }

    public String getEmailFrom() {
        return sEmailFrom;
    }

    public String getEmailTo() {
        return sEmailTo;
    }

    public String getBrowser() {
        return sBrowser;
    }

    public String getTestManagement() {
        return sTestManagement;
    }

    public String getTestMURL() {
        return sTestMURL;
    }

    public String getTestMUsername() {
        return sTestMUsername;
    }

    public String getTestMPassword() {
        return sTestMPassword;
    }

    public String getDomain() {
        return sDomain;
    }

    public String getProject() {
        return sProject;
    }

    public String getReportName() {
        return sReportName;
    }

    public String getAppendReport() {
        return sAppendReport;
    }

    public String getBrowserDrivers() {
        return sbrowserDrivers;
    }

    public void GetEnvironmentVariables() throws IOException, ParseException {
        File f1 = null;
        FileReader fr = null;

        JSONParser parser = new JSONParser();
        try {
            f1 = new File("conf/Environment.json");
            fr = new FileReader(f1);
            Object obj = parser.parse(fr);
            JSONObject jsonObject = (JSONObject) obj;
            //System.out.print(jsonObject);
            //String[] env=new String[10];

            sWebURL = (String) jsonObject.get("weburl");
            sUserID = (String) jsonObject.get("webusername");
            sPassword = (String) jsonObject.get("webpassword");
            sAppLoc = (String) jsonObject.get("windowsapplocation");
            sAppUsername = (String) jsonObject.get("windowsappusername");
            sAppPassword = (String) jsonObject.get("windowsapppassword");
            sApi = (String) jsonObject.get("API");
            sDataLocation = (String) jsonObject.get("datalocation");
            sDataType = (String) jsonObject.get("datatype");
            sDBHost = (String) jsonObject.get("dbhost");
            sDBPassword = (String) jsonObject.get("dbpass");
            sDBUsername = (String) jsonObject.get("dbusername");
            sEmail = (String) jsonObject.get("sendemail");
            sEmailFrom = (String) jsonObject.get("emailfrom");
            sEmailTo = (String) jsonObject.get("emailto");
            sBrowser = (String) jsonObject.get("Browser");
            sTestManagement = (String) jsonObject.get("testmanagement");
            sTestMURL = (String) jsonObject.get("testurl");
            sTestMUsername = (String) jsonObject.get("testmanagememntusername");
            sTestMPassword = (String) jsonObject.get("testmanagmentpassword");
            sDomain = (String) jsonObject.get("domain");
            sProject = (String) jsonObject.get("project");
            sReportName = (String) jsonObject.get("reportname");
            sAppendReport = (String) jsonObject.get("appendreport");
            sbrowserDrivers = (String) jsonObject.get("browserDrivers");

        } finally {
            try {
                fr.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    // Read and Write data from/To a TextFile
    public String ReadTextFile(String filePath) throws IOException {

        BufferedReader input = null;
        FileReader file = new FileReader(filePath);
        input = new BufferedReader(file);
        String value = input.readLine();
        input.close();

        return value;

    }

    public void WriteTextFile(String filePath, String outputData) throws IOException {
        Writer output = null;
        File file = new File(filePath);
        output = new BufferedWriter(new FileWriter(file));

        output.write(outputData);
        // output.write("\r\n");
        output.close();

    }

    public void GetData(String Location, String sheetName) throws IOException {

        String DefaultPath = System.getProperty("user.dir");
        sheet = ReadExcel(DefaultPath+Location,sheetName);
    }

    public Sheet ReadExcel(String FILE_NAME, String strSheetName) throws IOException {

        FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        // sheet = workbook.getSheetAt(0);
        sheet = workbook.getSheet(strSheetName);
        return sheet;
    }

    public void ReadExcelWorkbook(String FILE_NAME) throws IOException {

        FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
        workbook = new XSSFWorkbook(excelFile);
    }


    public String getCellData(String strColumn, int iRow, Sheet sheet) throws Exception {
        String Value = null;
        Row row = sheet.getRow(0);
        for (int i = 0; i < columnCount(sheet); i++) {
            if (row.getCell(i).getStringCellValue().trim().equals(strColumn)) {
                Row raw = sheet.getRow(iRow);
                Cell cell = raw.getCell(i);
                DataFormatter formatter = new DataFormatter();
                Value = formatter.formatCellValue(cell);
                break;
            }
        }
        return Value;
    }

    public String getCellData(String strColumn, int iRow, Sheet sheet, Recordset record, ResultSet resultset, String Type) throws Exception {
        String sValue = null;
        switch (Type.toUpperCase()) {

            case "EXCEL":

                Row row = sheet.getRow(0);
                for (int i = 0; i < columnCount(sheet); i++) {
                    if (row.getCell(i).getStringCellValue().trim().equals(strColumn)) {
                        Row raw = sheet.getRow(iRow);
                        Cell cell = raw.getCell(i);
                        DataFormatter formatter = new DataFormatter();
                        sValue = formatter.formatCellValue(cell);
                        break;
                    }
                }
                break;

            case "FILLO":

                if (iRow == fillonum) {
                    if (record.next()) {
                        fillonum = iRow + 1;
                        sValue = record.getField(strColumn);
                    }
                } else {
                    sValue = record.getField(strColumn);
                }
                break;

            case "SQLSERVER":

                if (iRow == fillonum) {
                    if (resultset.next()) {
                        fillonum = iRow + 1;
                        sValue = resultset.getString(strColumn);
                    }
                } else {
                    sValue = resultset.getString(strColumn);
                }
                break;
        }
        return sValue;
    }


    public int rowCount(Sheet sheet, Recordset record, ResultSet resultset, String Type) throws Exception {
        int count = 0;
        switch (Type.toUpperCase()) {
            case "EXCEL":
                count = sheet.getPhysicalNumberOfRows();
                break;
            case "FILLO":

                count = record.getCount();
                break;
            case "SQLSERVER":
                int i = 0;
                while (resultset.next()) {
                    i++;
                }
                count = i;
        }
        return count;
    }

    public int columnCount(Sheet sheet) throws Exception {
        return sheet.getRow(0).getLastCellNum();
    }

    public Recordset ConnectFillo(String path, String Query) throws FilloException {

        Fillo fillo = new Fillo();
        Recordset record;
        connect = fillo.getConnection(path);
        record = connect.executeQuery(Query);
        return record;
    }

    public ResultSet ConnectAndQuerySQLServer(String sDBURL, String sUserName, String sPassword, String sQuery) {

        ResultSet rs = null;

        try {

            String dbURL = sDBURL;
            String user = sUserName;
            String pass = sPassword;
            conn = DriverManager.getConnection(dbURL, user, pass);
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sQuery);

        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return rs;
    }

    public String GenerateSAIdNumber() {
        String saIdNumber = fakeValuesService.regexify(
                // with spaces and dashes
                // "(((\\d{2}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229))((
                // |-)(\\d{4})( |-)(\\d{3})|(\\d{7}))");
                "(((\\d{2}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229))((\\d{4})(\\d{3})|(\\d{7}))");
        return saIdNumber;
    }

    public String GenerateSACellNumber(Boolean valid) {
        String saCellNumber = null;
        if (valid) {
            while (true) {
                saCellNumber = fakeValuesService.regexify("[+](27)[156789][0-9]{8}");
                Pattern pattern = Pattern.compile("[+](27)[156789][0-9]{8}");
                Matcher matcher = pattern.matcher(saCellNumber);
                if (matcher.matches()) {
                    break;
                }
            }
            return saCellNumber;
        } else {
            while (true) {
                saCellNumber = fakeValuesService.bothify("+27#########");
                Pattern pattern = Pattern.compile("[+](27)[156789][0-9]{8}");
                Matcher matcher = pattern.matcher(saCellNumber);
                if (!matcher.matches()) {
                    break;
                }
            }
            return saCellNumber;
        }
    }

    public String GenerateRandomBirthdayWithFormat(int minAge, int maxAge, String format) {

        Date date = faker.date().birthday(minAge, maxAge);
        String formattedBirthday = new SimpleDateFormat("dd/MM/yyyy").format(date);
        return formattedBirthday;
    }

    public static Object[][] ReadTestData(String SheetName,String Location){
        Object Data[][] = null;
        try{

            String file_location = String.valueOf(Paths.get(Location));
            FileInputStream fileInputStream= new FileInputStream(file_location); //Excel sheet file location get mentioned here
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream); //get my workbook
            XSSFSheet worksheet=workbook.getSheet(SheetName);// get my sheet from workbook
            XSSFRow Row=worksheet.getRow(0);   //get my Row which start from 0

            int RowNum = worksheet.getPhysicalNumberOfRows();// count my number of Rows
            int ColNum= Row.getLastCellNum(); // get last ColNum

            Data = new Object[RowNum][ColNum]; // pass my  count data in array

            for(int i=0; i<RowNum; i++) //Loop work for Rows
            {
                XSSFRow row= worksheet.getRow(i);

                for (int j=0; j<ColNum; j++) //Loop work for colNum
                {
                    if(row==null)
                        Data[i][j]= "";
                    else
                    {
                        XSSFCell cell= row.getCell(j);
                        if(cell==null)
                            Data[i][j]= ""; //if it get Null value it pass no data
                        else
                        {
                            DataFormatter formatter = new DataFormatter();
                            String value=formatter.formatCellValue(cell);
                            Data[i][j]=value; //This formatter get my all values as string i.e integer, float all type data value
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Data;
    }
}
