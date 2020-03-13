@cards
Feature: Cards

  Scenario Outline: User draws a card from the deck and checks remaining cards
    Given the user starts card game
    When the user shuffles the new deck of cards
    And the user deals "<number>" cards
    Then the deck contains "<remainingNumber>" cards
    Examples:
      | number | remainingNumber |
      | 3      | 49              |
      | 27     | 25              |
      | 42     | 10              |

  Scenario Outline: User creates pile containing only aces and player can receive only aces from a deal
    Given the user starts card game
    When the user creates the new deck of cards with only "<pileName>"
    And the user deals "4" cards
    Then the player have a new pile with "<pileName>"
    When the player reviews his cards in "<pileName>" pile
    Then the player sees only "<pileName>" in dealing of "<pileName>" pile
    Examples:
      | pileName |
      | Aces     |

    #User can draw specific cards only from a pile, not from the deck as was described in the task
  Scenario Outline: User draws 5 specific cards from a bottom of pile and checks remaining cards in the deck
    Given the user starts card game
    When the user shuffles the new deck of cards
    And the user deals "52" cards
    Then the player have a new pile with "allCards"
    When the user draws "<drawCards>" cards from the "bottom" of "allCards" pile
    And the player reviews his cards in "allCards" pile
    Then the pile "allCards" contains <47> cards
    And the pile "allCards" do not contains "<drawCards>" cards in it
    Examples:
      | drawCards      |
      | 0C,6S,AD,KS,QS |

