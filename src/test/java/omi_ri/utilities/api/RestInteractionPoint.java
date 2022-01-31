package omi_ri.utilities.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static io.restassured.RestAssured.given;

public class RestInteractionPoint {
    public Response post(String baseURI, String body) {
        return given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .request().body(body).post(baseURI);
    }

    public Response put(String baseURI, String body) {
        return given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .request().body(body)
                .put(baseURI);
    }

    public Response get(String baseURI) {
        return given()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
                .request()
                .get(baseURI);
    }

    public Response get(String baseURI, String body) {
        return given().relaxedHTTPSValidation().contentType(ContentType.JSON).request().body(body).get(baseURI);
    }

    public Response getAndDownloadFile(String baseURI, String body, String locationToStoreFile, String fileName)throws Exception{
        File outputFile = new File(locationToStoreFile, fileName);
        Response response = given()
                .relaxedHTTPSValidation()
                .contentType("application/json")
                .body(body)
                .get(baseURI);
        response.getHeaders();

        byte[] fileContents = response.getBody().asByteArray();
        OutputStream outStream=null;
        try {
            outStream = new FileOutputStream(outputFile);
            outStream.write(fileContents);
        }catch(Exception e){
            System.out.println("Error writing file " + outputFile.getAbsolutePath());
        }finally {
            if(outStream!=null){
                outStream.close();
            }
        }
        return response;
    }
}
