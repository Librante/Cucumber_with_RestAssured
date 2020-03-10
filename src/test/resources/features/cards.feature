Feature: Cards

  Scenario Outline: User draws a card from the desk and checks remaining cards
    Given the user shuffles the new pile of cards
   # When the user draws "<number>" cards
   # Then the remaining desk contains "<remainingNumber>" cards
    Examples:
      | number | remainingNumber |
    |   3     |    49             |
    #|   7      |          45         |
    #|42        |10                   |