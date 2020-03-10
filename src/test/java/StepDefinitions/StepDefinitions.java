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

    @Given("the user shuffles the new deck of cards")
    public void theUserShufflesTheNewDeckOfCards() {
        Response response = request.GetRequest(Constants.BASE_URL+ Constants.NEW_DECK);
        String desk_id = (String) request.receiveJsonValueFromResponse(response,"deck_id");
        dataStorage.setDeck_id(desk_id);
        System.out.println(dataStorage.getDeck_id());
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
        String cardsInPile;
        switch (pileName) {
            case ("Aces"):
                cardsInPile = Constants.ALL_ACES;
        break;
            case ("allCards"):
                cardsInPile = Constants.ALL_CARDS;
                break;
            default:
                cardsInPile = pileName;
                pileName = "randomPile";
        }
        Map<String, String> queryParams = new HashMap();
        queryParams.put("cards", cardsInPile);
        queryParams.put("deck_id", dataStorage.getDeck_id());
        queryParams.put("pile_name",pileName);

        Response response = request.GetRequestWithQueryParam(Constants.BASE_URL+ Constants.CREATE_SPECIFIC_PILE, queryParams);
        dataStorage.setResponse(response);
        dataStorage.setPileName(pileName);
        System.out.println(response.asString());

    }

    @Then("the player sees only {string} in dealing of {string} pile")
    public void thePlayerSeesOnlyInDealing(String expectedCards, String pileName) {
        String cardsInPile = "";
        switch (expectedCards) {
            case ("Aces"):
                cardsInPile = Constants.ALL_ACES;
        }
        String[] strParts = cardsInPile.split(",");
        ArrayList<String> cardsReceivedInDeal = new ArrayList<>(Arrays.asList(strParts));

        List<Object> values = request.receiveJsonValuesAsListFromResponse(dataStorage.getResponse(),"piles."+pileName+".cards.value");
        List<Object> codes = request.receiveJsonValuesAsListFromResponse(dataStorage.getResponse(),"piles."+pileName+".cards.code");
        assertThat(cardsReceivedInDeal, is(codes));
        assertTrue (values.stream().allMatch(v -> v.equals("ACE")));
        System.out.println(values);
        System.out.println(codes);

    }


    @When("the user draws {string} cards from the {string} of {string} pile")
    public void theUserDrawsCardsFromTheOfPile(String cardsToDraw, String fromWhereToDrawCards, String pileName) {
        Map<String, String> queryParams = new HashMap();
        queryParams.put("cards", cardsToDraw);
        queryParams.put("deck_id", dataStorage.getDeck_id());
        queryParams.put("whereToDraw",fromWhereToDrawCards);
        queryParams.put("pileName", pileName);
        Response response = request.GetRequestWithQueryParam(Constants.BASE_URL+ Constants.DRAW_FROM_THE_DECK_BOTTOM, queryParams);

        System.out.println(response.asString());
    }

    @Then("the pile {string} contains <{int}> cards")
    public void thePileContainsCards(String pileName, int countOfCards) {

        int cardsRemains = (Integer) request.receiveJsonValueFromResponse(dataStorage.getResponse(),"piles."+pileName+".remaining");
        assertThat(cardsRemains, is(countOfCards));
    }

    @And("the pile {string} do not contains {string} cards in it")
    public void thePileAllCardsDoNotContainsCardsInIt(String pileName, String cardsToBeExcluded) {
        List<Object> codes = request.receiveJsonValuesAsListFromResponse(dataStorage.getResponse(),"piles."+pileName+".cards.code");
        System.out.println(codes);
        String[] strParts = cardsToBeExcluded.split(",");
        ArrayList<String> cardsReceivedInDeal = new ArrayList<>(Arrays.asList(strParts));
        System.out.println(cardsReceivedInDeal);
        for (String cardEx: cardsReceivedInDeal){
        assertTrue("For " +cardEx + "something get wrong",
                codes.stream().noneMatch(v -> v.equals(cardEx)));
 }
    }
}

