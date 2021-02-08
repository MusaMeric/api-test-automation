import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class HarryPotter {

    @Test
    public void test() {

        Response searchResponse = get("http://www.omdbapi.com/?apikey=bcd33f7a&s='harry potter'");
        System.out.println(searchResponse.getBody().asString());
        System.out.println(searchResponse.statusLine());

        int statusCode = searchResponse.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        String imbdID ="";

        if(statusCode == 200){
            int sizeOfList = searchResponse.body().path("Search.size()");

            List<Map<String, String>> searchResult = searchResponse.jsonPath().getList("Search");
            for (int i=0; i<sizeOfList; i++)
            {
              if(searchResult.get(i).get("Title").equals("Harry Potter and the Sorcerer's Stone"))
              {
                  imbdID = searchResult.get(i).get("imdbID");
                  System.out.println(imbdID);
                  break;
              }
            }
        }
        else{
            System.out.println("Response code is not 200");
        }

        Response searchByIdResponse = get("http://www.omdbapi.com/?apikey=b0e04e8&i={imbdID}", imbdID);
        System.out.println(searchByIdResponse.getBody().asString());
        System.out.println(searchByIdResponse.statusLine());
        searchByIdResponse.then()
                .assertThat()
                .statusCode(200)
                .body("Title", notNullValue())
                .body("Year", notNullValue())
                .body("Released", notNullValue());
    }

}
