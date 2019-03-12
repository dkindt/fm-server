package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

import static shared.util.FileHelper.inputStreamToString;
import static shared.util.RegexHelper.search;

abstract class BaseHandler {

    String supportedMethod;
    static Logger log = Logger.getLogger("family-map-server");

    BaseHandler() {}

    abstract String getURLPattern();

    void sendJSONResponse(Object result, HttpExchange exchange, int status) throws IOException {

        sendStringResponse(exchange, serialize(result), status);
    }

    void sendStringResponse(HttpExchange exchange, String data, int status) throws IOException {

        sendResponse(
            exchange,
            new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)),
            status
            );
    }

    void sendResponse(HttpExchange exchange, InputStream input, int status) throws IOException {

        Headers headers = exchange.getResponseHeaders();
        headers.add("Server", "CS240 Family Map");
        exchange.sendResponseHeaders(status, 0);

        final int offset = 0;
        byte[] buffer = new byte[1024];
        int bytesRead;

        OutputStream body = exchange.getResponseBody();
        while((bytesRead = input.read(buffer)) > offset) {
            body.write(buffer, offset, bytesRead);
        }
        exchange.close();
    }

    Object deserialize(InputStream input, Class klass) throws IOException {
        return new Gson().fromJson(
            inputStreamToString(input).replace("userName", "username"),
            klass
        );
    }

    private String serialize(Object o) {
        return new Gson().toJson(o, o.getClass());
    }

    boolean isValidRequestMethod(HttpExchange exchange) {
        return exchange.getRequestMethod().equalsIgnoreCase(supportedMethod);
    }

    Map<String, String> getURLParams(HttpExchange exchange) {

        log.entering("Handler", "getURLParams");
        Map<String, String> params = parseURL(exchange);
        return params;
    }

    private Map<String, String> parseURL(HttpExchange exchange) {
        return search(getURLPattern(), exchange.getRequestURI().getPath());
    }

    protected String getAuthorization(HttpExchange exchange) {
        return exchange.getRequestHeaders().getFirst("Authorization");
    }
}
