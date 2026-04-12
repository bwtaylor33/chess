package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class ChessRenderer {

    public ChessRenderer(ChessBoard board, ChessGame.TeamColor perspective) {
        this.board = board;
        this.asWhite = perspective == ChessGame.TeamColor.WHITE;
    }

    public void display() {

        String rowLabels = "12345678";

        if(!asWhite){
            rowLabels = new StringBuilder(rowLabels).reverse().toString();
        }

        System.out.print(topPadding());

        System.out.print(columnLabels());

        // Board rows
        for(int row = 1; row <= 8; row++){

            // Padding
            System.out.print(leftPadding());

            // Row label
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.printf(" %c ", rowLabels.charAt(row - 1));

            // Row squares
            for(int col = 1; col <= 8; col++){

                ChessPiece piece = board.getPiece(new ChessPosition(asWhite ? 9 - row : row, col));
                System.out.print(getBackgroundColorForSquare(row, col));

                if(piece == null){

                    // Empty square
                    System.out.print("   ");

                }else{

                    // Piece
                    System.out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK);
                    System.out.print(piece);
                }
            }

            // Row label
            System.out.print(RESET_BG_COLOR);
            System.out.print(SET_TEXT_COLOR_RED);
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
        buffer.append(SET_TEXT_COLOR_RED);

        for(int i = 0; i < 8; i++){
            buffer.append(String.format(" %c ", colLabels.charAt(i)));
        }

        buffer.append("\n");

        return buffer.toString();
    }

    private String getBackgroundColorForSquare(int row, int col){
        return (row + col) % 2 == 0 ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_GREY;
    }

    private final ChessBoard board;
    private final boolean asWhite;

    private static final int TOP_PADDING = 2;
    private static final int BOTTOM_PADDING = 1;
    private static final int LEFT_PADDING = 1;
}
