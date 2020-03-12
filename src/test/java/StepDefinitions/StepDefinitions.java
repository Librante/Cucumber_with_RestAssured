package StepDefinitions;

import Helpers.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

import java.util.*;

public class StepDefinitions {
    private static final Logger LOGGER = LoggerFactory.getLogger(StepDefinitions.class);
    private static HashMap<String, Response> responseAsMap = new HashMap<String, Response>();

    RequestsImpl request = new RequestsImpl();
    DataStorage dataStorage = new DataStorage();
    SetEnv setEnv = new SetEnv();
    JsonParsers jsonParsers = new JsonParsers();


    @Given("the user starts card game")
    public void theUserStartsCardGame() {
        setEnv.defineBaseURLFromPropertiesFile();
    }

    @Given("the user shuffles the new deck of cards")
    public void theUserShufflesTheNewDeckOfCards() {
        Response response = request.GetRequest(dataStorage.getBaseURL() + Constants.NEW_DECK);
        String desk_id = (String) jsonParsers.receiveJsonValueFromResponse(response, "deck_id");
        dataStorage.setDeck_id(desk_id);
        LOGGER.info("desk_id is: ", desk_id);
    }

    /**
     * @param numberOfCardsToDraw Received Response is saved in to ways:
     *                            One is sets to the hashMap of responses and can be called then by request shorten name (see Constants,
     *                            i.e key "DRAW_CARDS" will return response for DRAW_CARD request)
     *                            Simply saved response will be override by next request response
     */
    @When("the user deals {string} cards")
    public void theUserDrawsCards(String numberOfCardsToDraw) {
        Map<String, String> queryParams = new HashMap();
        queryParams.put("deck_id", dataStorage.getDeck_id());
        queryParams.put("count", numberOfCardsToDraw);
        Response response = request.GetRequestWithQueryParam(dataStorage.getBaseURL() + Constants.DRAW_CARDS, queryParams);
        responseAsMap.put("DRAW_CARDS", response);
        dataStorage.setResponseAsMap(responseAsMap);
        dataStorage.setResponse(response);
    }

    @Then("the deck contains {string} cards")
    public void theRemainingDeskContainsCards(String remainingCards) {
        Integer cardsRemains = (Integer) jsonParsers.receiveJsonValueFromResponse(dataStorage.getResponseAsMap().get("DRAW_CARDS"), "remaining");
        assertThat(cardsRemains, is(Integer.valueOf(remainingCards)));
    }


    @Given("the user creates the new deck of cards with only {string}")
    public void theUserCreatesTheNewPileOfCardsWithOnly(String dealingParam) {
        String cards = "";
        switch (dealingParam) {
            case ("Aces"):
                cards = Constants.ALL_ACES;
                break;
            default:
                LOGGER.info("User do not defines params for card dealing");
        }
        Map<String, String> queryParams = new HashMap();
        queryParams.put("cards", cards);
        Response response = request.GetRequestWithQueryParam(dataStorage.getBaseURL() + Constants.DRAW_SPECIFIC_CARDS, queryParams);
        dataStorage.setDeck_id((String) jsonParsers.receiveJsonValueFromResponse(response, "deck_id"));
        dataStorage.setResponse(response);
        responseAsMap.put("DRAW_SPECIFIC_CARDS", response);
    }

    @When("the player reviews his cards in {string} pile")
    public void thePlayerReviewsHisCards(String pileName) {
        Map<String, String> queryParams = new HashMap();
        queryParams.put("pile_name", pileName);
        queryParams.put("deck_id", dataStorage.getDeck_id());
        Response response = request.GetRequestWithQueryParam(dataStorage.getBaseURL() + Constants.LIST_SPECIFIC_PILE, queryParams);
        dataStorage.setResponse(response);
        responseAsMap.put("LIST_SPECIFIC_PILE", response);
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
        dataStorage.setPileName(pileName);
        Map<String, String> queryParams = new HashMap();
        queryParams.put("cards", cardsInPile);
        queryParams.put("deck_id", dataStorage.getDeck_id());
        queryParams.put("pile_name", pileName);
        Response response = request.GetRequestWithQueryParam(dataStorage.getBaseURL() + Constants.CREATE_SPECIFIC_PILE, queryParams);
        dataStorage.setResponse(response);
        responseAsMap.put("CREATE_SPECIFIC_PILE", response);
    }

    @Then("the player sees only {string} in dealing of {string} pile")
    public void thePlayerSeesOnlyInDealing(String expectedCards, String pileName) {
        String cardsInPile = "";
        String cardValue = "";
        switch (expectedCards) {
            case ("Aces"):
                cardsInPile = Constants.ALL_ACES;
                cardValue = "ACE";
                break;
        }
        String[] strParts = cardsInPile.split(",");
        ArrayList<String> cardsReceivedInDeal = new ArrayList<>(Arrays.asList(strParts));
        List<Object> values = jsonParsers.receiveJsonValuesAsListFromResponse(dataStorage.getResponse(), "piles." + pileName + ".cards.value");
        List<Object> codes = jsonParsers.receiveJsonValuesAsListFromResponse(dataStorage.getResponse(), "piles." + pileName + ".cards.code");
        assertThat(cardsReceivedInDeal, is(codes));
        String finalCardValue = cardValue;
        assertTrue("I'm expecting to see " + cardValue + " in pile, but receiving " + values,
                values.stream().allMatch(v -> v.equals(finalCardValue)));
    }


    @When("the user draws {string} cards from the {string} of {string} pile")
    public void theUserDrawsCardsFromTheOfPile(String cardsToDraw, String fromWhereToDrawCards, String pileName) {
        Map<String, String> queryParams = new HashMap();
        queryParams.put("cards", cardsToDraw);
        queryParams.put("deck_id", dataStorage.getDeck_id());
        queryParams.put("whereToDraw", fromWhereToDrawCards);
        queryParams.put("pileName", pileName);
        Response response = request.GetRequestWithQueryParam(dataStorage.getBaseURL() + Constants.DRAW_FROM_THE_DECK_BOTTOM, queryParams);
        responseAsMap.put("DRAW_FROM_THE_DECK_BOTTOM", response);
    }

    @Then("the pile {string} contains <{int}> cards")
    public void thePileContainsCards(String pileName, int countOfCards) {
        int cardsRemains = (Integer) jsonParsers.receiveJsonValueFromResponse(dataStorage.getResponse(), "piles." + pileName + ".remaining");
        assertThat(cardsRemains, is(countOfCards));
    }

    @And("the pile {string} do not contains {string} cards in it")
    public void thePileAllCardsDoNotContainsCardsInIt(String pileName, String cardsToBeExcluded) {
        List<Object> codes = jsonParsers.receiveJsonValuesAsListFromResponse(dataStorage.getResponse(), "piles." + pileName + ".cards.code");
        String[] strParts = cardsToBeExcluded.split(",");
        ArrayList<String> cardsReceivedInDeal = new ArrayList<>(Arrays.asList(strParts));
        for (String cardEx : cardsReceivedInDeal) {
            assertTrue("For " + cardEx + "something went wrong",
                    codes.stream().noneMatch(v -> v.equals(cardEx)));
        }
    }


}

