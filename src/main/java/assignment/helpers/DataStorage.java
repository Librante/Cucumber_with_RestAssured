package assignment.helpers;

import io.restassured.response.Response;

import java.util.HashMap;

public class DataStorage {

    private String deck_id;
    private Response response;
    private String pileName;
    private String baseURL;
    private HashMap <String, Response> responseAsMap = new HashMap<>();

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

    public void addResponseToMap(String key, Response response) {
        responseAsMap.put(key, response);
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
