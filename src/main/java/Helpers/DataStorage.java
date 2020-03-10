package Helpers;

import io.restassured.response.Response;

public class DataStorage {

    private static String deck_id;
    private static Response response;



    public void setDeck_id (String newDeck_id){
        this.deck_id = newDeck_id;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getDeck_id (){
        return deck_id;
    }

    public Response getResponse(){
        return response;
    }
}
