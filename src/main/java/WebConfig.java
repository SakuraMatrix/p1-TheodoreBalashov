
import com.datastax.oss.driver.api.core.CqlSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.scheduling.annotation.EnableAsync;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
@EnableAsync
@EnableMBeanExport
@Configuration
@ComponentScan
public class WebConfig {
    @Autowired
    @Primary
    @Bean
    public CacheManager jdkCacheManager() {
        return new ConcurrentMapCacheManager("cache");
    }
    @Bean
    public InstrumentationLoadTimeWeaver loadTimeWeaver()  throws Throwable {
        InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
        return loadTimeWeaver;
    }
    @Bean
    public CqlSession session() {
        return CqlSession.builder().build();
    }

    @Bean

    public DisposableServer disposableServer() {
        DatabaseTalker db = new DatabaseTalker(session());
        return HttpServer.create()
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
                                .post("/users/{param}", (request, response) ->
                                        response.sendObject(toByteBuf(db.createUser(parseUser(request.param("param"))))))
                                .post("/userActivity/{param}", (request, response) ->
                                        response.sendObject(toByteBuf(db.createUserAction(parseUserAction(request.param("param"))))))
                                .post("/paintingActivity/{param}", (request, response) ->
                                        response.sendObject(toByteBuf(db.createPaintingAction(parsePaintingAction(request.param("param"))))))

                )
                .bindNow();

    }




    static Painting parsePainting(String in){
        Painting painting = null;

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

        return painting;

    }
    static User parseUser(String in){
        User user = null;

            String[] params = in.split(",");
            int user_id = Integer.parseInt(params[0].split("=")[1]);
            String name = params[1].split("=")[1];
            String password  = params[2].split("=")[1];
            double balance = Double.parseDouble(params[3].split("=")[1]);
            user = new User(user_id, name, password, balance);

        return user;
    }
    static PaintingAction parsePaintingAction(String in){
        PaintingAction paintingAction = null;

            String[] params = in.split(",");
            int action_id = Integer.parseInt(params[0].split("=")[1]);
            int user_id = Integer.parseInt(params[1].split("=")[1]);
            int painting_id = Integer.parseInt(params[2].split("=")[1]);
            String action = params[3].split("=")[1];
            double amount = Integer.parseInt(params[4].split("=")[1]);
            paintingAction = new PaintingAction(action_id, user_id, painting_id, action, amount);

        return paintingAction;
    }
    static UserAction parseUserAction(String in){
        UserAction userAction = null;

            String[] params = in.split(",");
            int action_id = Integer.parseInt(params[0].split("=")[1]);
            int user_id = Integer.parseInt(params[1].split("=")[1]);
            String action = params[2].split("=")[1];
            double amount = Integer.parseInt(params[3].split("=")[1]);
            userAction = new UserAction(action_id, user_id, action, amount);

        return userAction;
    }
    static final ObjectMapper MAPPER = new ObjectMapper();
    private ByteBuf toByteBuf(Object o) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            MAPPER.writeValue(out, o);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ByteBufAllocator.DEFAULT.buffer().writeBytes(out.toByteArray());
    }
}