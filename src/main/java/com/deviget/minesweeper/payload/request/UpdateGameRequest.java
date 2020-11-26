package com.deviget.minesweeper.payload.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import com.deviget.minesweeper.model.GameStatus;

/**
 * Payload class used for update game requests.
 * 
 * @author david.rios
 */
public class UpdateGameRequest {

    @NotNull
    private Long timer;
    
    @NotNull
    private Integer minesLeft;
    
    @NotNull
    private List<Integer> board;

    @NotNull
    private GameStatus status;
    
    public Long getTimer() {
        return timer;
    }

    public void setTimer(Long timer) {
        this.timer = timer;
    }
    
    public Integer getMinesLeft() {
        return minesLeft;
    }

    public void setMinesLeft(Integer minesLeft) {
        this.minesLeft = minesLeft;
    }
    
    public List<Integer> getBoard() {
        return board;
    }
    
    public void setBoard(List<Integer> board) {
        this.board = board;
    }
    
    public GameStatus getStatus() {
        return status;
    }
    
    public void setStatus(GameStatus status) {
        this.status = status;
    }
}
