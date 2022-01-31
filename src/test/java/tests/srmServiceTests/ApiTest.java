package tests.srmServiceTests;


import io.restassured.response.Response;
import omi_ri.utilities.api.BuildJsonFile;
import omi_ri.utilities.api.RestInteractionPoint;
import omi_ri.utilities.api.StatusCode;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ApiTest {


    @Test
    public void iGetARequest() throws Exception {

        BuildJsonFile buildJsonFile =new BuildJsonFile();
        RestInteractionPoint restInteractionPoint = new RestInteractionPoint();
        Response response;


        String endPoint ="https://api.funtranslations.com/translate/yoda";
        String BodyContent ="{\n" +
                "    \"text\": \"Hello, I am learning how to test APIs with Postman!\"\n" +
                "}";

        System.out.println(endPoint);
        System.out.println(BodyContent);

        response =restInteractionPoint.post(endPoint,BodyContent);
        response.prettyPrint();
        System.out.println(response.getStatusCode());

        System.out.println(response.getBody());

        System.out.println(response.asString());

        System.out.println(StatusCode.CODE_200.code);

        assertStatusCode(response.getStatusCode(), StatusCode.CODE_200);
  //    System.setProperty("PetId",response.body().path("id").toString());
    }
/*
    public void iAddANewPetAndStatusAvailable(String expectedResults) {
        String result = response.getBody().path("name").toString();
        Assert.assertEquals(result,expectedResults);
    }*/

    public void assertStatusCode(int actualStatusCode, StatusCode statusCode){

        assertThat(actualStatusCode, equalTo(statusCode.code));

    }

   /* public void assertError(Error responseErr, StatusCode statusCode){

        assertThat(responseErr.getError().getStatus(), equalTo(statusCode.code))
        assertThat(responseErr.getError().getMessage() , equalTo(statusCode.msg))


    }*/

}
