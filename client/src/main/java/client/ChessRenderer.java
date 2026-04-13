package client;

import chess.*;

import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class ChessRenderer {

    public ChessRenderer(ChessBoard board, ChessGame.TeamColor perspective) {
        this.board = board;
        this.asWhite = perspective != ChessGame.TeamColor.BLACK;
    }

    public void display(ArrayList<ChessPosition> highlightedPositions) {

        String rowLabels = "12345678";

        if(asWhite){
            rowLabels = new StringBuilder(rowLabels).reverse().toString();
        }

        System.out.print(topPadding());

        System.out.print(columnLabels());

        // Board rows
        for(int row = 1; row <= 8; row++){

            // Padding
            System.out.print(leftPadding());

            // Row label
            System.out.print(LABEL_COLOR);
            System.out.printf(" %c ", rowLabels.charAt(row - 1));

            // Row squares
            for(int col = 1; col <= 8; col++){

                ChessPosition position = new ChessPosition(asWhite ? 9 - row : row, asWhite ? col : 9 - col);

                ChessPiece piece = board.getPiece(position);

                // Determine if this square is highlighted as a valid move
                boolean highlighted = highlightedPositions.contains(position);

                System.out.print(getBackgroundColorForSquare(row, col, highlighted));

                if(piece == null){

                    // Empty square
                    System.out.print("   ");

                }else{

                    // Piece
                    System.out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_COLOR : BLACK_COLOR);
                    System.out.print(piece);
                }
            }

            // Row label
            System.out.print(RESET_BG_COLOR);
            System.out.print(LABEL_COLOR);
            System.out.printf(" %c \n", rowLabels.charAt(row - 1));
        }

        System.out.print(columnLabels());

        System.out.print(bottomPadding());

        System.out.flush();
    }

    private String leftPadding(){
        String padding = "";
        for(int i = 0; i < LEFT_PADDING; i++){
            padding = padding + " ";
        }
        return padding;
    }

    private String topPadding(){
        return topBottomPadding(TOP_PADDING);
    }

    private String bottomPadding(){
        return topBottomPadding(BOTTOM_PADDING);
    }

    private String topBottomPadding(int lines){
        String padding = "";
        for(int i = 0; i < lines; i++){
            padding = padding + "\n";
        }
        return padding;
    }

    private String columnLabels(){

        String colLabels = "abcdefgh";

        if(!asWhite){
            colLabels = new StringBuilder(colLabels).reverse().toString();
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(leftPadding());
        buffer.append("   ");
        buffer.append(LABEL_COLOR);

        for(int i = 0; i < 8; i++){
            buffer.append(String.format(" %c ", colLabels.charAt(i)));
        }

        buffer.append("\n");

        return buffer.toString();
    }

    private String getBackgroundColorForSquare(int row, int col, boolean highlighted){

        if (highlighted) {
            return (row + col) % 2 == 0 ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_GREEN;
        }

        return (row + col) % 2 == 0 ? SET_BG_COLOR_BLACK : SET_BG_COLOR_WHITE;
    }

    private final ChessBoard board;
    private final boolean asWhite;

    private static final String LABEL_COLOR = SET_TEXT_COLOR_LIGHT_GREY;
    private static final String WHITE_COLOR = SET_TEXT_COLOR_RED;
    private static final String BLACK_COLOR = SET_TEXT_COLOR_BLUE;
    private static final int TOP_PADDING = 2;
    private static final int BOTTOM_PADDING = 1;
    private static final int LEFT_PADDING = 1;
}
