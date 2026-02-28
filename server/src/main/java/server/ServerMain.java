package server;

/**
 * Our program's main. Handles starting our javalin http server.
 */
public class ServerMain {

    public static void main(String[] args) {

        Server server = new Server();
        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}