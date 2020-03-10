package Helpers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RequestsImpl {


    public static Response GetRequest(String url){
        RestAssured.defaultParser = Parser.JSON;
        return given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when().get(url)
                .then().extract().response();
    }

    public static String receiveJsonValueFromResponse(Response response, String value){
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get(value);
    }
}
