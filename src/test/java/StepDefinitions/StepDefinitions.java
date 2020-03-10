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
import static org.junit.Assert.assertTrue;

import java.util.*;

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


    @Given("the user creates the new deck of cards with only {string}")
    public void theUserCreatesTheNewPileOfCardsWithOnly(String dealingParam) {
       String cards = "";
        switch (dealingParam){
            case("Aces"):
            cards = Constants.ALL_ACES;
        }
        Map<String, String> queryParams = new HashMap();
        queryParams.put("cards", cards);
        Response response = request.GetRequestWithQueryParam(Constants.BASE_URL+ Constants.DRAW_SPECIFIC_CARDS, queryParams);
        dataStorage.setDeck_id((String) request.receiveJsonValueFromResponse(response,"deck_id"));
        dataStorage.setResponse(response);
        System.out.println(response.asString());
    }

    @Then("the player receives only {string} in dealing")
    public void thePlayerReceivesOnlyInDealing(String expectingCardsInDeals) {
        System.out.println(dataStorage.getResponse().asString());
    }

    @When("the player reviews his cards in {string} pile")
    public void thePlayerReviewsHisCards(String pileName) {
        Map<String, String> queryParams = new HashMap();
        queryParams.put("pile_name", pileName);
        queryParams.put("deck_id", dataStorage.getDeck_id());
        Response response = request.GetRequestWithQueryParam(Constants.BASE_URL+ Constants.LIST_SPECIFIC_PILE, queryParams);
        dataStorage.setResponse(response);
        System.out.println(response.asString());
    }

    @Then("the player have a new pile with {string}")
    public void thePlayerHaveANewPileWithOnly(String pileName) {
        String cardsInPile = "";
        switch (pileName){
            case("Aces"):
                cardsInPile = Constants.ALL_ACES;
        }
        Map<String, String> queryParams = new HashMap();
        queryParams.put("cards", cardsInPile);
        queryParams.put("deck_id", dataStorage.getDeck_id());
        queryParams.put("pile_name",pileName);

        Response response = request.GetRequestWithQueryParam(Constants.BASE_URL+ Constants.CREATE_SPECIFIC_PILE, queryParams);
        dataStorage.setResponse(response);
        System.out.println(response.asString());

    }

    @Then("the player sees only {string} in dealing")
    public void thePlayerSeesOnlyInDealing(String expectedCards) {
        String cardsInPile = "";
        switch (expectedCards) {
            case ("Aces"):
                cardsInPile = Constants.ALL_ACES;
        }
        String[] strParts = cardsInPile.split(",");
        ArrayList<String> cardsReceivedInDeal = new ArrayList<>(Arrays.asList(strParts));

        List<Object> values = request.receiveJsonValuesAsListFromResponse(dataStorage.getResponse(),"piles.Aces.cards.value");
        List<Object> codes = request.receiveJsonValuesAsListFromResponse(dataStorage.getResponse(),"piles.Aces.cards.code");
        assertThat(cardsReceivedInDeal, is(codes));
        assertTrue (values.stream().allMatch(v -> v.equals("ACE")));
        System.out.println(values);
        System.out.println(codes);

    }
    }

