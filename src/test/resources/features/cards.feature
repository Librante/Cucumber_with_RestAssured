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
  Given the user creates the new deck of cards with only "Aces"
      And the user deals "4" cards
      And the player have a new pile with "Aces"
      When the player reviews his cards in "Aces" pile
      Then the player sees only "Aces" in dealing