package eu.sverdiyev.application;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class MainClass {
    public static void main(String[] args) throws IOException {
        var server = HttpServer.create();

        HttpHandler handler = exchange -> {
            String response = "This is the response from verds website";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        };

        HttpHandler handlerVertification = exchange -> {
            try {

                String fileName = "D477EBFE2CAC324679FFF3EB7944AB07.txt";

                InputStream myFile = MainClass.class.getResourceAsStream("/" + fileName);

                byte[] allBytes = myFile.readAllBytes();

                System.out.println(Arrays.toString(allBytes));
                // Set response headers
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=" + fileName);

                // Set response status
                exchange.sendResponseHeaders(200, allBytes.length);


                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(allBytes);
                } finally {
                    // Close the InputStream
                    myFile.close();
                }

                // Close the exchange to indicate that the response is complete
                exchange.close();
            } catch (Exception e) {
                System.err.println("ERROR HAPPENED");
                System.err.println(e);

            }
        };


        server.createContext("/", handler);
        server.createContext("/elo", handlerVertification);

        server.bind(new InetSocketAddress(8080), 10);
        server.start();
    }

}
