package gpacalculator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class AppServer {
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        SemesterCalculator calculator = new SemesterCalculator(new GradeScale());
        PageRenderer renderer = new PageRenderer();

        server.createContext("/", exchange -> handleHome(exchange, calculator, renderer));
        server.setExecutor(null);
        server.start();

        System.out.println("Semester GPA Calculator running at http://localhost:" + port + "/");
        Thread.currentThread().join();
    }

    private static void handleHome(
            HttpExchange exchange,
            SemesterCalculator calculator,
            PageRenderer renderer
    ) throws IOException {
        try {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> form = FormParser.parse(body);
                int subjectCount = FormParser.intValue(form, "subjectCount", 5, 1, 12);
                List<Subject> subjects = Subject.fromForm(form, subjectCount);
                SemesterResult result = calculator.calculate(subjects);
                send(exchange, renderer.render(subjectCount, subjects, result));
                return;
            }

            int subjectCount = FormParser.intValue(
                    FormParser.parse(exchange.getRequestURI().getRawQuery()),
                    "subjectCount",
                    5,
                    1,
                    12
            );
            send(exchange, renderer.render(subjectCount, Subject.samples(subjectCount), null));
        } catch (RuntimeException exception) {
            send(exchange, renderer.renderError(exception.getMessage()), 400);
        }
    }

    private static void send(HttpExchange exchange, String html) throws IOException {
        send(exchange, html, 200);
    }

    private static void send(HttpExchange exchange, String html, int status) throws IOException {
        byte[] response = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(status, response.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(response);
        }
    }
}
