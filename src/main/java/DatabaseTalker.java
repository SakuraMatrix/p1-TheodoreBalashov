import ch.qos.logback.classic.Logger;
import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class DatabaseTalker {
    public static final Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("main");

    private CqlSession session;
    public DatabaseTalker(CqlSession session) {
        this.session = session;
    }
    public User createUser(User user){
        log.info("Creating a user: "+user);
        SimpleStatement simp = SimpleStatement.builder("INSERT INTO paintingSeller.user"+
                "(user_id, username, password, balance) " +
                "values (?, ?, ?, ?)")
                .addPositionalValues(user.getUser_id(), user.getName(), user.getPassword(), user.getBalance())
                .build();
        Flux.from(session.executeReactive(simp)).subscribe();
        System.out.println(user);
        return user;
    }
    public Painting createPainting(Painting painting){
        log.info("Creating a painting: "+painting);
        SimpleStatement simp = SimpleStatement.builder("INSERT INTO paintingSeller.painting"+
                "(painting_id, owner, title, url, description, author, isForSale, price) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)")
                .addPositionalValues(painting.getId(), painting.getOwner(), painting.getTitle(), painting.getUrl(), painting.getDesc(), painting.getAuthor(),
                        painting.getIsForSale(), painting.getPrice())
                .build();
        Flux.from(session.executeReactive(simp)).subscribe();
        return painting;
    }

    public Flux<Painting> getPainting(int id){
        log.info("Getting painting id: "+id);
        return returnAPainting("SELECT * FROM paintingSeller.painting where painting_id = "+id);
    }
    public Flux<Painting> getAllPaintings(){
        log.info("Getting all paintings");
        return returnAPainting("SELECT * FROM paintingSeller.painting");
    }
    public Flux<User> getAllUsers(){
        log.info("Getting all users");
        return returnAUser("SELECT * FROM paintingSeller.user");
    }
    public Flux<User> getUser(int id){
        log.info("Getting user id: "+ id);
        return returnAUser("SELECT * FROM paintingSeller.user where user_id = "+id);
    }
    public Flux<PaintingAction> getPaintingAction(int id){
        log.info("Getting painting action: "+ id);
        return returnAPaintingAction("SELECT * FROM paintingSeller.paintingActivity where action_id = "+id);
    }
    public Flux<PaintingAction> getAllPaintingAction(){
        log.info("Getting all painting actions.");
        return returnAPaintingAction("SELECT * FROM paintingSeller.paintingActivity");
    }
    public Flux<UserAction> getUserAction(int id){
        log.info("Getting user action: " +id);
        return returnAUserAction("SELECT * FROM paintingSeller.userAction where action_id = "+id);
    }
    public Flux<UserAction> getAllUserAction(){
        log.info("Getting all user actions.");
        return returnAUserAction("SELECT * FROM paintingSeller.userAction");
    }
    public PaintingAction createPaintingActionHelper(PaintingAction paintingAction){
        log.info("Creating a painting action: " + paintingAction);
        SimpleStatement simp = SimpleStatement.builder("INSERT INTO paintingSeller.paintingAction"+
                "(action_id, user_id, painting_id, action, amount) " +
                "values (?, ?, ?, ?, ?)")
                .addPositionalValues(paintingAction.getAction_id(), paintingAction.getUser_id(), paintingAction.getPainting_id(), paintingAction.getAction(), paintingAction.getAmount())
                .build();
        Flux.from(session.executeReactive(simp)).subscribe();
        return paintingAction;
    }
    public PaintingAction putPaintingOnSale(PaintingAction paintingAction){
        log.info("Putting painting on sale: "+paintingAction);
        Flux.from(session.executeReactive("UPDATE paintingSeller.painting set isForSale = TRUE where painting_id = "+paintingAction.getPainting_id())).subscribe();
        return paintingAction;
    }
    public PaintingAction setPaintingCost(PaintingAction paintingAction){
        log.info("Setting painting cost: "+paintingAction);
        Flux.from(session.executeReactive("UPDATE paintingSeller.painting set price = "+paintingAction.getAmount()+" where painting_id = "+paintingAction.getPainting_id())).subscribe();
        return paintingAction;
    }
    public PaintingAction takePaintingOffSale(PaintingAction paintingAction){
        log.info("Taking painting off sale: "+paintingAction);
        Flux.from(session.executeReactive("UPDATE paintingSeller.painting set isForSale = FALSE where painting_id = "+paintingAction.getPainting_id())).subscribe();
        return paintingAction;
    }
    public PaintingAction transferPaintingOwnership(PaintingAction paintingAction){
        log.info("Transfering ownership of painting to user: "+paintingAction);
        Flux.from(session.executeReactive("UPDATE paintingSeller.painting set owner = " + paintingAction.getUser_id() + " where painting_id = " + paintingAction.getPainting_id())).subscribe();
        return paintingAction;
    }
    public boolean createPaintingAction(PaintingAction paintingAction){
        log.info("Attempting to process paintingAction.");
        if(paintingAction.getAction().equals("buy")){
            if(hasAmount(paintingAction.getUser_id(), getCost(paintingAction.getPainting_id()))&&isForSale(paintingAction.getPainting_id())){
                transferPaintingOwnership(paintingAction);
                takePaintingOffSale(paintingAction);
                createPaintingActionHelper(paintingAction);
                return true;
            }
        }
        if(paintingAction.getAction().equals("putOnSale")){
            System.out.println(paintingAction);
            putPaintingOnSale(paintingAction);
            setPaintingCost(paintingAction);
            createPaintingActionHelper(paintingAction);

            return true;
        }
        if(paintingAction.getAction().equals("takeOffSale")) {
            takePaintingOffSale(paintingAction);
            createPaintingActionHelper(paintingAction);
            return true;
        }
        log.warn("Painting action not processed.");
        return false;
    }
    public UserAction createUserActionHelper(UserAction userAction){
        log.info("Creating user action: "+userAction);
        SimpleStatement simp = SimpleStatement.builder("INSERT INTO paintingSeller.userAction"+
                "(action_id, user_id, action, amount) " +
                "values (?, ?, ?, ?)")
                .addPositionalValues(userAction.getAction_id(), userAction.getUser_id(), userAction.getAction(), userAction.getAmount())
                .build();
        Flux.from(session.executeReactive(simp)).subscribe();
        return userAction;
    }
    public boolean createUserAction(UserAction userAction){
        log.info("Attempting to process user action.");
        if(userAction.getAction().equals("deposit")){
            deposit(userAction);
            createUserActionHelper(userAction);
            return true;
        }
        if(userAction.getAction().equals("withdraw")){
            if(hasAmount(userAction.getUser_id(), userAction.getAmount())) {
                withdraw(userAction);
                createUserActionHelper(userAction);
            }
            return true;
        }
        log.warn("User action not processed.");
        return false;
    }

    public Flux<PaintingAction> returnAPaintingAction(String query){
        return Flux.from(session.executeReactive(query))
                .map(row -> new PaintingAction(row.getInt("action_id"), row.getInt("user_id"),row.getInt("painting_id"),
                        row.getString("action"), row.getDouble("amount")));
    }
    public Flux<UserAction> returnAUserAction(String query){
        return Flux.from(session.executeReactive(query))
                .map(row -> new UserAction(row.getInt("action_id"), row.getInt("user_id"),
                        row.getString("action"), row.getDouble("amount")));
    }
    public Flux<User> returnAUser(String query){
        return Flux.from(session.executeReactive(query))
                .map(row -> new User(row.getInt("user_id"), row.getString("username"),
                        row.getString("password"), row.getDouble("balance")));
    }
    public Flux<Painting> returnAPainting(String query){
        return Flux.from(session.executeReactive(query))
                .map(row -> new Painting(row.getInt("painting_id"), row.getInt("owner"),
                        row.getString("title"), row.getString("url"),
                        row.getString("description"), row.getString("author"),
                        row.getBoolean("isForSale"), row.getDouble("price") ));
    }
    public boolean hasAmount(int user_id, double amt) {
        log.info("Checking if user " + user_id + " has " + amt);
        return true;
    }
    public double getBalance(int UserID){
        log.info("Checking balance of " + UserID);
        return 0;
    }
    public boolean deposit(UserAction userAction){
        log.info("Depositing: " + userAction);
        return true;
    }
    public boolean isForSale(int PaintingId){
        log.info("Checking if painting id on sale: "+PaintingId);
        return true;
    }
    public boolean withdraw(UserAction userAction){
        log.info("Withdrawing: " + userAction);
        return true;
    }
    public double getCost(int paintingId){
        log.info("Getting cost of painting id: " + paintingId);
        return 0;
    }
}