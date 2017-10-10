package ru.mail.polis.kuryakin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

public final class Service implements KVService {

    private static final String PREFIX = "id=";
    private static final String VALUE_BY_ID = "Value by id=";

    @NotNull
    private final HttpServer httpServer;
    @NotNull
    private final DAO dao;

    public Service(int port, @NotNull DAO dao) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        this.dao = dao;

        this.httpServer.createContext("/v0/status", httpExchange -> sendResponse(httpExchange, "Server running", 200));


        this.httpServer.createContext("/v0/entity", httpExchange -> {
            final String query = httpExchange.getRequestURI().getQuery();
            if (!query.startsWith(PREFIX)) {
                sendResponse(httpExchange, "Shitty query", 400);
                return;
            }
            final String id = query.substring(PREFIX.length());
            final String method = httpExchange.getRequestMethod();
            switch (method) {
                case "GET":
                    final byte[] getValue;
                    try {
                        getValue = dao.get(id);
                    } catch (NoSuchElementException e) {
                        sendResponse(httpExchange, e.getMessage(), 404);
                        return;
                    } catch (IllegalArgumentException e) {
                        sendResponse(httpExchange, e.getMessage(), 400);
                        return;
                    }
                    sendResponse(httpExchange, getValue, 200);
                    break;
                case "PUT":
                    final int contentLength = Integer.valueOf(httpExchange.getRequestHeaders().getFirst("Content-Length"));
                    final byte[] putValue = new byte[contentLength];
                    if (contentLength != 0 && httpExchange.getRequestBody().read(putValue) != putValue.length) {
                        throw new IOException("Can't read at once");
                    }
                    try {
                        dao.upsert(id, putValue);
                    } catch (IllegalArgumentException e) {
                        sendResponse(httpExchange, e.getMessage(), 400);
                        return;
                    }
                    sendResponse(httpExchange, VALUE_BY_ID + id + "have been updated", 201);
                    break;
                case "DELETE":
                    try {
                        dao.delete(id);
                    } catch (IllegalArgumentException e) {
                        sendResponse(httpExchange, e.getMessage(), 400);
                        return;
                    }
                    sendResponse(httpExchange, VALUE_BY_ID + id + "might have been deleted", 202);
                    break;
                default:
                    sendResponse(httpExchange, method + "not allowed", 405);
            }
        });
    }

    private void sendResponse(@NotNull HttpExchange http, @NotNull byte[] message, int code) throws IOException {
        http.sendResponseHeaders(code, message.length);
        http.getResponseBody().write(message);
        http.close();
    }

    private void sendResponse(@NotNull HttpExchange http, @NotNull String message, int code) throws IOException {
        sendResponse(http, message.getBytes(), code);
    }

    @Override
    public void start() {
        httpServer.start();
    }

    @Override
    public void stop() {
        httpServer.stop(0);
    }

}
