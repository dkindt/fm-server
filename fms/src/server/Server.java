package server;

import com.sun.net.httpserver.HttpServer;
import server.database.Database;
import server.exceptions.DatabaseException;
import server.handlers.*;
import server.handlers.FileHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.*;

public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;
    private static Logger log;

    static {
        try {
            initLog();
        }
        catch (IOException e) {
            System.out.println(String.format("Could not initialize log: %s", e.getMessage()));
            e.printStackTrace();
        }
    }

    private static void initLog() throws IOException {

        Level logLevel = Level.FINEST;

        log = Logger.getLogger("family-map-server");
        log.setLevel(logLevel);
        log.setUseParentHandlers(false);

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(logLevel);
        consoleHandler.setFormatter(new SimpleFormatter());
        log.addHandler(consoleHandler);

        java.util.logging.FileHandler fileHandler = new java.util.logging.FileHandler("fms.log", false);
        fileHandler.setLevel(logLevel);
        fileHandler.setFormatter(new SimpleFormatter());
        log.addHandler(fileHandler);
    }

    private HttpServer server;

    /**
     * Initializes and runs the Server on a specified port number.
     * @param portNumber specifies the port number to listen on.
     */
    private void run(String portNumber) {

        // Since the server has no "user interface", it should display "log"
        // messages containing information about its internal activities.
        // This allows a system administrator (or you) to know what is happening
        // inside the server, which can be useful for diagnosing problems
        // that may occur.
        log.info("Initializing HTTP Server");

        try {
            // Create a new HttpServer object.
            // Rather than calling "new" directly, we instead insert
            // the object by calling the HttpServer.insert static factory method.
            // Just like "new", this method returns a reference to the new object.
            String hostname = "localhost";
            server = HttpServer.create(
                new InetSocketAddress(hostname, Integer.parseInt(portNumber)),
                MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        // This line is necessary, but its function is unimportant for our purposes.
        server.setExecutor(null);

        // The HttpServer class listens for incoming HTTP requests.  When one
        // is received, it looks at the URL path inside the HTTP request, and
        // forwards the request to the handler for that URL path.
        // TL;DR this sets up our server's `routes`
        log.info("Creating contexts");
        server.createContext("/", new FileHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/clear/", new ClearHandler());
        server.createContext("/load/", new ClearHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/person", new PersonHandler());

        // Tells the HttpServer to start accepting incoming client connections.
        // This method call will return immediately, and the "main" method
        // for the program will also complete.
        // Even though the "main" method has completed, the program will continue
        // running because the HttpServer object we created is still running
        // in the background.
        server.start();

        log.info(String.format("Server started at: %s", server.getAddress()));
    }

    // "main" method for the server program
    // "args" should contain one command-line argument, which is the port number
    // on which the server should accept incoming client connections.
    public static void main(String[] args) throws DatabaseException, IOException {
        String portNumber = args[0];
        new Server().run(portNumber);
        new Database().init("sql/db_init.sql");
    }

}