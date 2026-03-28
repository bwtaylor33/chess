package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

abstract public class BaseClient {

    public BaseClient(ServerFacade serverFacade, String greetingMessage) {
        server = serverFacade;
        this.greetingMessage = greetingMessage;
    }

    public void run() {
        System.out.println(greetingMessage);
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_WHITE + result);
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
    protected static final String WHITE_QUEEN = " ♕ ";

    private final String greetingMessage;
}
