import java.io.*;
import com.sun.net.httpserver.*;

public class Main {
  public static void main(String[] args) throws Exception {
    HttpServer server = HttpServer.create(new java.net.InetSocketAddress(3002), 0);
    server.createContext("/", exchange -> {
      String response = "Hola desde Java";
      exchange.sendResponseHeaders(200, response.length());
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(response.getBytes());
      }
    });
    server.start();
  }
}
