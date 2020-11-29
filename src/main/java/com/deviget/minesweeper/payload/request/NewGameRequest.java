package com.deviget.minesweeper.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public NewGameRequest() {
    }

    public NewGameRequest(Integer numRows, Integer numCols, Integer numMines) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.numMines = numMines;
    }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("numRows", numRows)
                .append("numCols", numCols)
                .append("numMines", numMines)
                .toString();
    }
}
