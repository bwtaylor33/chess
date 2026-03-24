package client;

import server.ServerFacade;
import service.ResponseException;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.WHITE_QUEEN;

abstract public class BaseClient {

    public BaseClient(ServerFacade serverFacade) throws ResponseException {
        server = serverFacade;
    }

    public void run() {
        System.out.println(WHITE_QUEEN + " Welcome to the chess client. Sign in to start.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    abstract public void printPrompt();

    abstract public String help();

    abstract public String eval(String line);

    protected final ServerFacade server;
}
