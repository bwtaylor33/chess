package client;

import java.util.Scanner;

import static ui.EscapeSequences.*;

abstract public class BaseClient {

    public BaseClient(ServerFacade serverFacade, String greetingMessage) {
        server = serverFacade;
        this.greetingMessage = greetingMessage;
    }

    abstract public void printPrompt();

    abstract public String help();

    abstract public String eval(String line);

    public void run() {

        System.out.println(greetingMessage);
        System.out.println(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {

            printPrompt();

            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(SET_TEXT_COLOR_WHITE + result);

            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }

        System.out.println();
    }

    protected final ServerFacade server;
    protected static final String WHITE_QUEEN = " ♕ ";

    private final String greetingMessage;
}