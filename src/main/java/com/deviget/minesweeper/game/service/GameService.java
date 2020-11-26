package com.deviget.minesweeper.game.service;

import static com.deviget.minesweeper.model.CellStatus.COVERED_CELL;
import static com.deviget.minesweeper.model.CellStatus.COVERED_MINE;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.deviget.minesweeper.model.Game;
import com.deviget.minesweeper.payload.request.NewGameRequest;
import com.deviget.minesweeper.payload.request.UpdateGameRequest;
import com.deviget.minesweeper.payload.response.MessageResponse;
import com.deviget.minesweeper.repository.GameRepository;

/**
 * Service class implementing all operations for {@link Game} management.
 * 
 * @author david.rios
 */
@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    /**
     * Interacts with {@link GameRepository} to create a new game associated to the current user.
     * 
     * @param request - a {@link NewGameRequest} instance with the game details.
     * @return a {@link Game} instance if the creation was successful, error otherwise.
     */
    public Game createGame(NewGameRequest request) {
        int numCols = request.getNumCols();
        int numRows = request.getNumRows();
        int numMines = request.getNumMines();
        int numCells = numRows * numCols;

        List<Integer> board = new ArrayList<>(Collections.nCopies(numCells, COVERED_CELL.value()));

        for (int i = 0; i < numMines; i++) {
            int minePosition = ThreadLocalRandom.current()
                    .nextInt(0, numCells);

            if (newCoveredMineCell(board.get(minePosition))) {
                int currentCol = minePosition % numCols;
                if (currentCol > 0) {
                    // Top left adjacent cell
                    updateIfAdjacentToMine(board, minePosition - numCols - 1);

                    // Left adjacent cell
                    updateIfAdjacentToMine(board, minePosition - 1);

                    // Bottom left adjacent cell
                    updateIfAdjacentToMine(board, minePosition + numCols - 1);
                }

                if (currentCol < (numCols - 1)) {
                    // Top right adjacent cell
                    updateIfAdjacentToMine(board, minePosition - numCols + 1);

                    // Bottom right adjacent cell
                    updateIfAdjacentToMine(board, minePosition + numCols + 1);

                    // Right adjacent cell
                    updateIfAdjacentToMine(board, minePosition + 1);
                }

                // Top adjacent cell
                updateIfAdjacentToMine(board, minePosition - numCols);

                // Bottom adjacent cell
                updateIfAdjacentToMine(board, minePosition + numCols);
            }
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        Game game = new Game(numCols, numRows, numMines, board, username);
        gameRepository.save(game);
        return game;
    }

    /**
     * Interacts with {@link GameRepository} to update an existing game.
     * 
     * @param id - the game id.
     * @param request - a {@link UpdateGameRequest} instance with the allowed update properties.
     * @return the updated {@link Game} instance.
     */
    public Game updateGame(Long id, UpdateGameRequest request) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game Not Found with id: " + id));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        if (!username.equals(game.getUsername())) {
            throw new GameNotAvailableException("Game Not Available for current user: " + username);
        }
        game.setTimer(request.getTimer());
        game.setMinesLeft(request.getMinesLeft());
        game.setBoard(request.getBoard());
        game.setStatus(request.getStatus());
        game.setLastUpdated(LocalDateTime.now());
        gameRepository.save(game);
        return game;
    }

    /**
     * Interacts with {@link GameRepository} to get all games for current user.
     * 
     * @return a {@link List} of {@link Game} instances associated to the authenticated user.
     */
    public List<Game> getAllGamesForCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        List<Game> games = gameRepository.findAllByUsername(username);
        return games;
    }

    /**
     * Interacts with {@link GameRepository} to get an individual game.
     * 
     * @param id - the game id.
     * @return a {@link Game} instance if the game is associated to the authenticated user, an error otherwise.
     */
    public Game getGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game Not Found with id: " + id));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        if (!username.equals(game.getUsername())) {
            throw new GameNotAvailableException("Game Not Available for current user: " + username);
        }
        return game;
    }

    /**
     * Interacts with {@link GameRepository} to delete an individual game associated to the current user.
     * 
     * @param id - the game id.
     * @return a {@link MessageResponse} indicating if the deletion was successful or not.
     */
    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Game Not Found with id: " + id));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        if (!username.equals(game.getUsername())) {
            throw new GameNotAvailableException("Game Not Available for user: " + username);
        }
        gameRepository.delete(game);
    }

    /**
     * Interacts with {@link GameRepository} to delete all games associated to the current user.
     * 
     * @return a {@link MessageResponse} indicating if the deletion was successful or not.
     */
    public void deleteAllGamesForCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        List<Game> games = gameRepository.findAllByUsername(username);
        for (Game game : games) {
            gameRepository.delete(game);
        }
    }

    /**
     * Checks if the given cell does not contain a mine. If so, plant one.
     * 
     * @param cell - the board cell.
     * @return true if a mine was planted, false otherwise.
     */
    private boolean newCoveredMineCell(Integer cell) {
        if (cell != COVERED_MINE.value()) {
            cell = COVERED_MINE.value();
            return true;
        }
        return false;
    }

    /**
     * Updates the mines counter if the given cell is adjacent to a mine.
     * 
     * @param board - the game board.
     * @param cellIndex - the index of the cell to check for adjacent mines.
     */
    private void updateIfAdjacentToMine(List<Integer> board, int cellIndex) {
        if (cellIndex >= 0) {
            Integer cell = board.get(cellIndex);
            if (cell != COVERED_MINE.value()) {
                cell += 1;
            }
        }
    }
}
