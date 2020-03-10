package Helpers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RequestsImpl {


    public static Response GetRequest(String url){
        RestAssured.defaultParser = Parser.JSON;
        return given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when().get(url)
                .then()
                .statusCode(200)
                .extract().response();
    }

    public static Response GetRequestWithQueryParam(String url, Map queryParams){
        RestAssured.defaultParser = Parser.JSON;
        return given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .when().get(url, queryParams)
                .then()
                .statusCode(200)
                .extract().response();
    }

    public static Object receiveJsonValueFromResponse(Response response, String value){
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get(value);
    }

    public static List<Object> receiveJsonValuesAsListFromResponse(Response response, String value) {

        return response.jsonPath().getList(value);
    }
}
