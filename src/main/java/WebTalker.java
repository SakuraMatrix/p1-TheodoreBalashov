
import com.datastax.oss.driver.api.core.CqlSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebTalker {

    public WebTalker() throws URISyntaxException {
        Path indexHTML = Paths.get(WebTalker.class.getResource("/index.html").toURI());
        Path errorHTML = Paths.get(WebTalker.class.getResource("/error.html").toURI());

        CqlSession session = CqlSession.builder().build();
        //CreateKeyspace myKeyspace = SchemaBuilder.createKeyspace("paintingSeller");
        DatabaseTalker db = new DatabaseTalker(session);

        HttpServer.create()
                .port(8080)
                .route(routes ->
                        routes.get("/paintings", (request, response) ->
                                response.send(db.getAllPaintings().map(WebTalker::toByteBuf)
                                        .log("http-server")))
                                .get("/paintings/{param}", (request, response) ->
                                        response.send(db.getPainting(Integer.parseInt(request.param("param"))).map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/users", (request, response) ->
                                        response.send(db.getAllUsers().map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/users/{param}", (request, response) ->
                                        response.send(db.getUser(Integer.parseInt(request.param("param"))).map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/paintingActivity", (request, response) ->
                                        response.send(db.getAllPaintingAction().map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/paintingActivity/{param}", (request, response) ->
                                        response.send(db.getPaintingAction(Integer.parseInt(request.param("param"))).map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/userActivity", (request, response) ->
                                        response.send(db.getAllUserAction().map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .get("/userActivity/{param}", (request, response) ->
                                        response.send(db.getUserAction(Integer.parseInt(request.param("param"))).map(WebTalker::toByteBuf)
                                                .log("http-server")))
                                .post("/paintings/{param}", (request, response) ->
                                        response.sendObject(toByteBuf(db.createPainting(parsePainting(request.param("param"))))))
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
                .bindNow()
                .onDispose()
                .block();
    }

    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static ByteBuf toByteBuf(Object o) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            OBJECT_MAPPER.writeValue(out, o);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ByteBufAllocator.DEFAULT.buffer().writeBytes(out.toByteArray());
    }/*
    static Item parseItem(String str) {
        Item item = null;
        try {
            item = OBJECT_MAPPER.readValue(str, Item.class);
        } catch (JsonProcessingException ex) {
            String[] params = str.split("&");
            int id = Integer.parseInt(params[0].split("=")[1]);
            String name = params[1].split("=")[1];
            double price = Double.parseDouble(params[2].split("=")[1]);
            item = new Item(id, name, price);
        }
        return item;
    }*/
    static Painting parsePainting(String in){
        Painting painting = null;
        try{
            painting=OBJECT_MAPPER.readValue(in, Painting.class);
        }catch (JsonProcessingException ex) {
            String[] params = in.split(",");
            int painting_id = Integer.parseInt(params[0].split("=")[1]);
            int owner = Integer.parseInt(params[1].split("=")[1]);
            String title = params[2].split("=")[1];
            String url = params[3].split("=")[1];
            String description = params[4].split("=")[1];
            String author = params[5].split("=")[1];
            boolean isForSale = Boolean.parseBoolean(params[6].split("=")[1]);
            double price = Double.parseDouble(params[7].split("=")[1]);
            painting = new Painting(painting_id, owner, title, url, description, author, isForSale, price);
        }
        return painting;

    }
    static User parseUser(String in){
        User user = null;
        try{
            user=OBJECT_MAPPER.readValue(in, User.class);
        }catch (JsonProcessingException ex) {
            String[] params = in.split(",");
            int user_id = Integer.parseInt(params[0].split("=")[1]);
            String name = Integer.parseInt(params[1].split("=")[1]);
            String password  = params[2].split("=")[1];
            String url = params[3].split("=")[1];
            String description = params[4].split("=")[1];
            String author = params[5].split("=")[1];
            boolean isForSale = Boolean.parseBoolean(params[6].split("=")[1]);
            double price = Double.parseDouble(params[7].split("=")[1]);
            user = new User(user_id, password, ;
        }
        return user;
    }
    static PaintingAction parsePaintingAction(String in){
        return null;
    }
    static UserAction parseUserAction(String in){
        return null;
    }
}
/*.post("/paintings", (request, response) ->
                                        response.send(request.receive().asString()
                                                .map(WebTalker::parsePainting)
                                                .map(db::createPainting)
                                                .map(WebTalker::toByteBuf)
                                                .log("http-server")))*/