/*import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@ComponentScan
public class WebConfig {

    @Autowired
    DatabaseTalker db;

    @Bean
    public CqlSession session() {
        return CqlSession.builder().build();
    }

    @Bean
    public DisposableServer server() throws URISyntaxException {
        Path errorHTML = Paths.get(WebTalker.class.getResource("/error.html").toURI());

        return HttpServer.create()
                .port(8080)
                .route(routes ->
                        routes.get("/paintings", (request, response) ->
                                response.send(db.getAllPaintings().map(WebTalker::toByteBuf)
                                        .log("http-server")))
                                .get("/paintings/{param}", (request, response) ->
                                        response.send(db.getPainting(request.param("param")).map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/users", (request, response) ->
                                        response.send(db.getAllUsers().map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/users/{param}", (request, response) ->
                                        response.send(db.getUser(request.param("param")).map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/paintingActivity", (request, response) ->
                                        response.send(db.getAllPaintingActivity().map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/paintingActivity/{param}", (request, response) ->
                                        response.send(db.getPaintingActivity(request.param("param")).map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/userActivity", (request, response) ->
                                        response.send(db.getAllUserActivity().map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/userActivity/{param}", (request, response) ->
                                        response.send(db.getUserActivity(request.param("param")).map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .post("/paintings", (request, response) ->
                                        response.send(request.receive().asString()
                                                .map(WebTalker::parsePainting)
                                                .map(db::createPainting)
                                                .map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .post("/users", (request, response) ->
                                        response.send(request.receive().asString()
                                                .map(WebTalker::parseUser)
                                                .map(db::createUser)
                                                .map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .post("/paintingActivity", (request, response) ->
                                        response.send(request.receive().asString()
                                                .map(WebTalker::parsePaintingAction)
                                                .map(db::createPaintingAction)
                                                .map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .post("/userActivity", (request, response) ->
                                        response.send(request.receive().asString()
                                                .map(WebTalker::parseUserAction)
                                                .map(db::createUserAction)
                                                .map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/error", (request, response) ->
                                        response.status(404).addHeader("Message", "Goofed")
                                                .sendFile(errorHTML))
                )
                .bindNow();
    }
}*/