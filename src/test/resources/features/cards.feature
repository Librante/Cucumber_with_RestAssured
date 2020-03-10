Feature: Cards

  Scenario Outline: User draws a card from the deck and checks remaining cards
    Given the user shuffles the new pile of cards
    When the user deals "<number>" cards
    Then the deck contains <remainingNumber> cards
    Examples:
      | number | remainingNumber |
      | 3      | 49              |
      | 27     | 25              |
      | 42     | 10              |
@wip
    Scenario: User creates deck containing only aces and player can receive only aces from this deck
      Given the user creates the new pile of cards with only "Aces"
      And the deck contains <4> cards
#      When the user shuffles the existing pile
#      And the user deals "<4>" cards
#      Then the player receives only "Aces" in dealing