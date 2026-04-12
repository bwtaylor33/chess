package websocket.commands;

public class ResignGameCommand extends UserGameCommand {

    public ResignGameCommand(String authToken, int gameID) {
        super(CommandType.RESIGN, authToken, gameID);
    }
}
