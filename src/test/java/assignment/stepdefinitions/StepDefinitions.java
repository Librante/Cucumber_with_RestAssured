package assignment.stepdefinitions;

import assignment.helpers.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static assignment.helpers.SetEnv.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

import java.util.*;

public class StepDefinitions {
    private static final Logger LOGGER = LoggerFactory.getLogger(StepDefinitions.class);

    final DataStorage dataStorage = new DataStorage();

    /**
     * Initial step of all scenario.
     * Sets base URL
     */
    @Given("the user starts card game")
    public void theUserStartsCardGame() {
        defineBaseURLFromPropertiesFile(dataStorage);
    }


    /**
     * Steps creates new shuffled deck of card. Sets deck_id, which should be used in most following steps
     */
    @When("the user shuffles the new deck of cards")
    public void theUserShufflesTheNewDeckOfCards() {
        Response response = RequestsImpl.GetRequest(dataStorage.getBaseURL() + Constants.NEW_DECK);
        String desk_id = (String) JsonParsers.receiveJsonValueFromResponse(response, Constants.DECK_ID);
        LOGGER.info("desk_id is: {}", desk_id);
        dataStorage.setDeck_id(desk_id);
    }

    /**
     * @param numberOfCardsToDraw - set particular number of card to deal from the deck
     * Received Response is saved in two ways:
     *                            One is sets to the hashMap of responses and can be called then by request shorten name (see Constants,
     *                            i.e key "DRAW_CARDS" will return response for DRAW_CARD request)
     *                            Simply saved response will be override by next request response
     */
    @When("the user deals {string} cards")
    public void theUserDrawsCards(String numberOfCardsToDraw) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(Constants.DECK_ID, dataStorage.getDeck_id());
        queryParams.put(Constants.COUNT, numberOfCardsToDraw);
        Response response = RequestsImpl.GetRequestWithQueryParam(dataStorage.getBaseURL() + Constants.DRAW_CARDS_FROM_DECK, queryParams);
        dataStorage.addResponseToMap("DRAW_CARDS_FROM_DECK", response);
        dataStorage.setResponse(response);
    }

    /**
     *
     * @param remainingCards number of card to verify
     *       Steps verify number of card in <>deck</>
     */
    @Then("the deck contains {string} cards")
    public void theRemainingDeskContainsCards(String remainingCards) {
        Integer cardsRemains = (Integer) JsonParsers.receiveJsonValueFromResponse(dataStorage.getResponse(), Constants.JSON_VALUE_REMAINING);
        assertThat(cardsRemains, is(Integer.valueOf(remainingCards)));
    }

    /**
     * @param dealingParam - cards codes to deal in new deck
     *  We can define some particular set of card and call it using <>switch</>
     *  or we can just define some codes (i.e random) as "dealing params".
     */
    @Given("the user creates the new deck of cards with only {string}")
    public void theUserCreatesTheNewPileOfCardsWithOnly(String dealingParam) {
        String cards = "";
        switch (dealingParam) {
            case ("Aces"):
                cards = Constants.ALL_ACES;
                break;
            default:
                cards = dealingParam;
                LOGGER.info("User defines random card codes: {}", dealingParam);
        }
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(Constants.CARDS, cards);
        Response response = RequestsImpl.GetRequestWithQueryParam(dataStorage.getBaseURL() + Constants.DRAW_SPECIFIC_CARDS, queryParams);
        dataStorage.setDeck_id((String) JsonParsers.receiveJsonValueFromResponse(response, Constants.DECK_ID));
        dataStorage.setResponse(response);
        dataStorage.addResponseToMap("DRAW_SPECIFIC_CARDS", response);
    }

    /**
     *
     * @param pileName - pileName to be used in following steps
     *        User should create a new pile before any other manipulation with specific cards
     *        If user wants to draw any specific card, he needs to create a pile with this cards first
     */
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
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(Constants.CARDS, cardsInPile);
        queryParams.put(Constants.DECK_ID, dataStorage.getDeck_id());
        queryParams.put(Constants.PILE_NAME, pileName);
        Response response = RequestsImpl.GetRequestWithQueryParam(dataStorage.getBaseURL() + Constants.CREATE_SPECIFIC_PILE, queryParams);
        dataStorage.setResponse(response);
        dataStorage.addResponseToMap("CREATE_SPECIFIC_PILE", response);
    }

    /**
     * @param pileName - pileName should be set in step with creating new pile
     */
    @When("the player reviews his cards in {string} pile")
    public void thePlayerReviewsHisCards(String pileName) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(Constants.PILE_NAME, dataStorage.getPileName());
        queryParams.put(Constants.DECK_ID, dataStorage.getDeck_id());
        Response response = RequestsImpl.GetRequestWithQueryParam(dataStorage.getBaseURL() + Constants.LIST_SPECIFIC_PILE, queryParams);
        dataStorage.setResponse(response);
        dataStorage.addResponseToMap("LIST_SPECIFIC_PILE", response);
    }

    /**
     *
     * @param expectedCards - card user expect to see in the deal
     * @param pileName - specific pile listed
     *      Verify response for listing of specific pile
     *       Getting and verifying expecting card codes
     */

    @Then("the player sees only {string} in dealing of {string} pile")
    public void thePlayerSeesOnlyInDealing(String expectedCards, String pileName) {
        String cardsInPile = "";
        switch (expectedCards) {
            case ("Aces"):
                cardsInPile = Constants.ALL_ACES;
                break;
            default:
              cardsInPile = pileName;
        }
        String[] strParts = cardsInPile.split(",");
        ArrayList<String> cardsReceivedInDeal = new ArrayList<>(Arrays.asList(strParts));
        List<Object> codes = JsonParsers.receiveJsonValuesAsListFromResponse(dataStorage.getResponseAsMap().get("LIST_SPECIFIC_PILE"), "piles." + dataStorage.getPileName() + ".cards.code");
        assertThat(cardsReceivedInDeal, is(codes));
    }

    /**
     *
     * @param cardsToDraw - card codes to draw
     * @param fromWhereToDrawCards - bottom or top. If you want to draw from the top just leave this parameter empty
     * @param pileName - saved pile name
     */
    @When("the user draws {string} cards from the {string} of {string} pile")
    public void theUserDrawsCardsFromTheOfPile(String cardsToDraw, String fromWhereToDrawCards, String pileName) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(Constants.CARDS, cardsToDraw);
        queryParams.put(Constants.DECK_ID, dataStorage.getDeck_id());
        queryParams.put("from_where_draw", fromWhereToDrawCards);
        queryParams.put(Constants.PILE_NAME, pileName);
        Response response = RequestsImpl.GetRequestWithQueryParam(dataStorage.getBaseURL() + Constants.DRAW_FROM_THE_PILE, queryParams);
        dataStorage.addResponseToMap("DRAW_FROM_THE_PILE", response);
    }

    /**
     *
     * @param pileName - saved pile name
     * @param countOfCards -
     *       Check number of card in specified in "LIST_SPECIFIC_PILE" request
     */
    @Then("the pile {string} contains <{int}> cards")
    public void thePileContainsCards(String pileName, int countOfCards) {
        int cardsRemains = (Integer) JsonParsers.receiveJsonValueFromResponse(dataStorage.getResponseAsMap().get("LIST_SPECIFIC_PILE"), "piles." + pileName + ".remaining");
        assertThat(cardsRemains, is(countOfCards));
    }

    /**
     *
     * @param pileName - saved pile name
     * @param cardsToBeExcluded - card codes shoudn't appear in card listing specified in "LIST_SPECIFIC_PILE" request
     */
    @And("the pile {string} do not contains {string} cards in it")
    public void thePileAllCardsDoNotContainsCardsInIt(String pileName, String cardsToBeExcluded) {
        List<Object> codes = JsonParsers.receiveJsonValuesAsListFromResponse(dataStorage.getResponseAsMap().get("LIST_SPECIFIC_PILE"), "piles." + pileName + ".cards.code");
        String[] strParts = cardsToBeExcluded.split(",");
        ArrayList<String> cardsReceivedInDeal = new ArrayList<>(Arrays.asList(strParts));
        for (String cardExclude : cardsReceivedInDeal) {
            assertTrue("Card " + cardExclude + "shoudn't be in the pile, but it is",
                    codes.stream().noneMatch(v -> v.equals(cardExclude)));
        }
    }


}

