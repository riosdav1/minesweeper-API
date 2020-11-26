# Minesweeper-API

## REQUIREMENTS

See: https://github.com/deviget/minesweeper-API/blob/master/README.md

## ASUMPTIONS

1. DB-based, single role, user registration.
2. Token based user authentication.
3. For new games, the API will initialize each cell of the game board with one of: mined / covered / number of adjacent mines.
4. New games will be associated to the authenticated user.
5. Timer and game board will be updated on client side, possibly allowing the user to pause the game.
6. A game will have one of three possible statuses: in game, game lost, game won.
7. The API will allow to update the following properties of a game: timer, mines left, status, game board.
8. The API will return the list of games associated to the current user.
9. The API will support removal of an individual game, or all of them.

## GAMES RULES

See: https://en.wikipedia.org/wiki/Microsoft_Minesweeper#Gameplay

## API SPECS

| Method | Path                    | Parameters                                 | Description
|:------:|-------------------------|--------------------------------------------|---------------
| POST   | minesweeper/user/signup | username, email, password                  | User registration.
| POST   | minesweeper/user/signin | username, password                         | User login. Returns auth token.
| POST   | minesweeper/game        | cols, rows, mines                          | Create new game.
| PUT    | minesweeper/game/{id}   | timer, mines left, game board, game status | Update game.
| GET    | minesweeper/game        | --                                         | Retrieve all games associated to current user.
| GET    | minesweeper/game/{id}   | game id                                    | Retrieve an individual game associated to current user.
| DELETE | minesweeper/game        | --                                         | Delete all games associated to current user.
| DELETE | minesweeper/game/{id}   | game id                                    | Delete an individual game associated to current user.

## PERSISTENCE

1. Database

MySQL

2. Entities & Models

User (Entity):
* Long id
* String email
* String username
* String password
* String role

GameStatus (Enum):
* IN_GAME
* GAME_WON
* GAME_LOST

Game (Entity):
* Long id
* String username
* Integer numCols
* Integer numRows
* Integer numMines
* Integer minesLeft
* List<Integer> board
* Long timer
* GameStatus status
* LocalDateTime dateCreated
* LocalDateTime lastUpdated
