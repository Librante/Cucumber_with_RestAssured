package StepDefinitions;

import Helpers.Constants;
import Helpers.RequestsImpl;
import Helpers.DataStorage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

public class StepDefinitions {

RequestsImpl request = new RequestsImpl();
DataStorage dataStorage = new DataStorage();

    @Given("the user shuffles the new pile of cards")
    public void theUserShufflesTheNewPileOfCards() {
        Response response = request.GetRequest(Constants.BASE_URL+ Constants.NEW_DECK);
        String desk_id = (String) request.receiveJsonValueFromResponse(response,"deck_id");
        dataStorage.setDeck_id(desk_id);
    }

    @When("the user deals {string} cards")
    public void theUserDrawsCards(String numberOfCardsToDraw) {
        Map<String, String> queryParams = new HashMap();
        queryParams.put("deck_id", dataStorage.getDeck_id());
        queryParams.put("count", numberOfCardsToDraw);
        Response response = request.GetRequestWithQueryParam(Constants.BASE_URL+ Constants.DRAW_CARDS, queryParams);
        dataStorage.setResponse(response);
    }

    @Then("the deck contains <{int}> cards")
    public void theRemainingDeskContainsCards(int remainingCards) {
        int cardsRemains = (Integer) request.receiveJsonValueFromResponse(dataStorage.getResponse(),"remaining");
        assertThat(cardsRemains, is (remainingCards));
    }

    @Given("the user creates the new pile of cards with only {string}")
    public void theUserCreatesTheNewPileOfCardsWithOnly(String dealingParam) {
       String cards = "";
        switch (dealingParam){
            case("Aces"):
            cards = "AC,AH,AD,AS";
        }
        Map<String, String> queryParams = new HashMap();
        queryParams.put("cards", cards);
        Response response = request.GetRequestWithQueryParam(Constants.BASE_URL+ Constants.DRAW_SPECIFIC_CARDS, queryParams);
        dataStorage.setDeck_id((String) request.receiveJsonValueFromResponse(response,"deck_id"));
        dataStorage.setResponse(response);
    }


}
