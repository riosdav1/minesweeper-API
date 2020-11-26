package com.deviget.minesweeper.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Payload class used for new games requests.
 * 
 * @author david.rios
 */
public class NewGameRequest {

    @NotNull
    @Max(1000)
    private Integer numRows;
    
    @NotNull
    @Max(1000)
    private Integer numCols;
    
    @NotNull
    @Max(10000)
    private Integer numMines;

    public Integer getNumRows() {
        return numRows;
    }
    
    public void setNumRows(Integer numRows) {
        this.numRows = numRows;
    }

    public int getNumCols() {
        return numCols;
    }
    
    public void setNumCols(Integer numCols) {
        this.numCols = numCols;
    }
    
    public Integer getNumMines() {
        return numMines;
    }

    public void setNumMines(Integer numMines) {
        this.numMines = numMines;
    }
}
