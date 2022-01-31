package omi_ri.utilities.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static io.restassured.RestAssured.given;

public class SOAPInteractionPoint {
    public String send(String wsURL, String body, String soapAction) throws IOException {
        URL url = new URL(wsURL);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[body.length()];
        buffer = body.getBytes();
        byteArrayOutputStream.write(buffer);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(byteArray.length));
        httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpURLConnection.setRequestProperty("SOAPAction", soapAction);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(byteArray);
        outputStream.close();

        InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
        BufferedReader in = new BufferedReader(inputStreamReader);

        String responseString = "";
        String response = "";
        while ((responseString = in.readLine()) != null) {
            response += responseString;
        }
        return response;
    }

    private String path(String response, String node, String tagName) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(response)));

        NodeList errNodes = doc.getElementsByTagName(node);
        if (errNodes.getLength() > 0) {
            Element err = (Element) errNodes.item(0);
            return err.getElementsByTagName(tagName)
                    .item(0)
                    .getTextContent();
        } else {
            throw new Exception(tagName + " NOT FOUND.");
        }
    }

    public Response send(String wsURL, String body) throws Exception {
        return given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.XML)
                .request().body(body).post(wsURL);
    }

    public Response sendWithContentType(String wsURL, String body, String proxy, int port, String contentType) throws Exception {
        return given()
                .relaxedHTTPSValidation().proxy(proxy,port)
                .contentType(contentType)
                .request().body(body).post(wsURL);
    }
}