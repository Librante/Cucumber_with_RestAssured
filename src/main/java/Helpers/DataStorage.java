package Helpers;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class DataStorage {

    private static String deck_id;
    private static Response response;
    private static String pileName;
    private static String baseURL;
    private static HashMap <String, Response> responseAsMap;


    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }


    public void setDeck_id (String newDeck_id){
        this.deck_id = newDeck_id;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setPileName(String pileName) {
        this.pileName = pileName;
    }

    public void setResponseAsMap(HashMap<String, Response> responseAsMap) {
        this.responseAsMap = responseAsMap;
    }

    public String getDeck_id (){
        return deck_id;
    }

    public Response getResponse(){
        return response;
    }

    public String getPileName(){
        return pileName;
    }

    public String getBaseURL() {return baseURL;}

    public HashMap <String, Response> getResponseAsMap (){
        return responseAsMap;
    }


}
