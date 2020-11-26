package com.deviget.minesweeper.model;

/**
 * Enumerated class containing the possible statuses for a {@link Game} board cell.
 * 
 * @author david.rios
 */
public enum CellStatus {

    EMPTY_CELL(0),
    MINED_CELL(9),
    COVERED_CELL(10),
    MARKED_CELL(10),
    COVERED_MINE(19),
    MARKED_MINE(29);

    private final int value;

    private CellStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
