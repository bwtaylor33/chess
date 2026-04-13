package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    public ChessPosition(int row, int col) {

        this.row = row;
        this.column = col;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessPosition that = (ChessPosition) o;

        return getRow() == that.getRow() && getColumn() == that.getColumn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d]", row, column);
    }

    public String toRowColumnString() {
        return String.format("%c%c", 'a' + row - 1, '1' + column - 1);
    }

    public static ChessPosition fromRowColumnString(String rowColumnString) {

        if(rowColumnString == null || rowColumnString.length() != 2){
            throw new IllegalArgumentException("Invalid format: expect row-column string as 2-character, letter-digit combo");
        }

        rowColumnString = rowColumnString.toLowerCase();

        int col = rowColumnString.charAt(0) - 'a' + 1;
        int row = rowColumnString.charAt(1) - '1' + 1;

        if(row < 1 || row > 8 || col < 1 || col > 8){
            throw new IllegalArgumentException("Error: Expected row in range a-h and column in range 1-8");
        }

        return new ChessPosition(row, col);
    }

    private int row;
    private int column;
}