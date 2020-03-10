Feature: Cards

  Scenario Outline: User draws a card from the deck and checks remaining cards
    Given the user shuffles the new deck of cards
    When the user deals "<number>" cards
    Then the deck contains <remainingNumber> cards
    Examples:
      | number | remainingNumber |
      | 3      | 49              |
      | 27     | 25              |
      | 42     | 10              |

  Scenario: User creates deck containing only aces and player can receive only aces from this deck
    Given the user creates the new deck of cards with only "Aces"
    And the user deals "4" cards
    And the player have a new pile with "Aces"
    When the player reviews his cards in "Aces" pile
    Then the player sees only "Aces" in dealing of "Aces" pile

    #You can draw specific cards only from a pile, not from the deck as was described in task
  @wip
  Scenario Outline: User draws 5 specific cards from a bottom of checks remaining cards in the deck
    Given the user shuffles the new deck of cards
    And the user deals "52" cards
    And the player have a new pile with "allCards"
    When the user draws "<drawCards>" cards from the "bottom" of "allCards" pile
    When the player reviews his cards in "allCards" pile
    Then the pile "allCards" contains <47> cards
    And the pile "allCards" do not contains "<drawCards>" cards in it
    Examples:
      | drawCards      |
      | 0C,6S,AD,KS,QS |

