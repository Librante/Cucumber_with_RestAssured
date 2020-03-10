package StepDefinitions;

import Helpers.RequestsImpl;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;

import static io.restassured.RestAssured.get;

public class StepDefinitions {

RequestsImpl request = new RequestsImpl();

    @Given("the user shuffles the new pile of cards")
    public void theUserShufflesTheNewPileOfCards() {
        Response response = request.GetRequest("https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1");
        String desk_id = request.receiveJsonValueFromResponse(response,"deck_id");
        System.out.println(desk_id);
    }
}
