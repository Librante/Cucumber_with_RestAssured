package assignment.helpers;

public class Constants {
    //URL's
    public static final String NEW_DECK = "new/shuffle/?deck_count=1";
    public static final String DRAW_CARDS_FROM_DECK = "{deck_id}/draw/?count={count}";
    public static final String DRAW_SPECIFIC_CARDS = "new/shuffle/?cards={cards}";
    public static final String DRAW_FROM_THE_PILE = "{deck_id}/pile/{pile_name}/draw/{from_where_draw}/?cards={cards}";
    public static final String CREATE_SPECIFIC_PILE = "{deck_id}/pile/{pile_name}/add/?cards={cards}";
    public static final String LIST_SPECIFIC_PILE = "{deck_id}/pile/{pile_name}/list/";

    //Field and Json values names
    public static final String DECK_ID = "deck_id";
    public static final String COUNT = "count";
    public static final String CARDS = "cards";
    public static final String PILE_NAME = "pile_name";

    public static final String JSON_VALUE_REMAINING = "remaining";


    //Cards
    public static final String ALL_ACES = "AH,AC,AD,AS";
    public static final String ALL_CARDS = "AH,AS,AC,AD,2C,2H,2D,2S,3C,3H,3D,3S,4C,4H,4D,4S,5C,5H,5D,5S,6C,6H,6D,6S,7C,7H,7D,7S,8C,8H,8D,8S,9C,9H,9D,9S,0C,0H,0D,0S,JC,JH,JD,JS,QC,QH,QD,QS,KC,KH,KD,KS";
}
