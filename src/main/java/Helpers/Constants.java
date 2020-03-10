package Helpers;

public class Constants {
    //URL's
    public static String BASE_URL = "https://deckofcardsapi.com/api/deck/";

    public static String NEW_DECK = "new/shuffle/?deck_count=1";
    public static String DRAW_CARDS = "{deck_id}/draw/?count={count}";
    public static String DRAW_SPECIFIC_CARDS = "new/shuffle/?cards={cards}";
    public static String CREATE_SPECIFIC_PILE = "{deck_id}/pile/{pile_name}/add/?cards={cards}";

    public static String LIST_SPECIFIC_PILE = "{deck_id}/pile/{pile_name}/list/";


    //Cards
    public static String ALL_ACES = "AH,AC,AD,AS";
}
