import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DatabaseTalker {
    private CqlSession session;
    public DatabaseTalker(CqlSession session) {

        this.session = session;
        System.out.println("TEST: "+session.executeReactive("SELECT balance from paintingSeller.user WHERE user_id = "+100));
    }
    public User createUser(User user){
        SimpleStatement simp = SimpleStatement.builder("INSERT INTO paintingSeller.user"+
                "(user_id, username, password, balance) " +
                "values (?, ?, ?, ?)")
                .addPositionalValues(user.getUser_id(), user.getName(), user.getPassword(), user.getBalance())
                .build();
        Flux.from(session.executeReactive(simp)).subscribe();
        return user;
    }
    public Painting createPainting(Painting painting){

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
        return returnAPainting("SELECT * FROM paintingSeller.painting where painting_id = "+id);
    }
    public Flux<Painting> getAllPaintings(){
        return returnAPainting("SELECT * FROM paintingSeller.painting");
    }
    public Flux<User> getAllUsers(){
        return returnAUser("SELECT * FROM paintingSeller.user");
    }
    public Flux<User> getUser(int id){
        return returnAUser("SELECT * FROM paintingSeller.user where user_id = "+id);
    }
    public Flux<User> getPaintingAction(int id){
        return returnAUser("SELECT * FROM paintingSeller.paintingActivity where action_id = "+id);
    }
    public Flux<User> getAllPaintingAction(){
        return returnAUser("SELECT * FROM paintingSeller.paintingActivity");
    }
    public Flux<User> getUserAction(int id){
        return returnAUser("SELECT * FROM paintingSeller.userAction where action_id = "+id);
    }
    public Flux<User> getAllUserAction(){
        return returnAUser("SELECT * FROM paintingSeller.userAction");
    }
    public PaintingAction createPaintingActionHelper(PaintingAction paintingAction){
        SimpleStatement simp = SimpleStatement.builder("INSERT INTO paintingSeller.paintingAction"+
                "(action_id, painting_id, owner, title, url, description, author, isForSale, price) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)")
                .addPositionalValues(paintingAction.getAction_id(), paintingAction.getUser_id(), paintingAction.getPainting_id(), paintingAction.getAmount())
                .build();
        Flux.from(session.executeReactive(simp)).subscribe();
        return paintingAction;
    }
    public PaintingAction putPaintingOnSale(PaintingAction paintingAction){
        session.executeReactive("UPDATE paintingSeller.painting set isForSale = TRUE where painting_id = "+paintingAction.getPainting_id());
        return paintingAction;
    }
    public PaintingAction takePaintingOffSale(PaintingAction paintingAction){
        session.executeReactive("UPDATE paintingSeller.painting set isForSale = FALSE where painting_id = "+paintingAction.getPainting_id());
        return paintingAction;
    }
    public PaintingAction transferPaintingOwnership(PaintingAction paintingAction){
        session.executeReactive("UPDATE paintingSeller.painting set owner = " + paintingAction.getUser_id() + " where painting_id = " + paintingAction.getPainting_id());
        return paintingAction;
    }
    public boolean createPaintingAction(PaintingAction paintingAction){
        if(paintingAction.getAction().equals("buy")){
            if(hasAmount(paintingAction.getUser_id(), getCost(paintingAction.getPainting_id()))&&isForSale(paintingAction.getPainting_id())){
                transferPaintingOwnership(paintingAction);
                takePaintingOffSale(paintingAction);
                return true;
            }
        }
        if(paintingAction.getAction().equals("putOnSale")){
            putPaintingOnSale(paintingAction);
            return true;
        }
        if(paintingAction.getAction().equals("takeOffSale")) {
            takePaintingOffSale(paintingAction);
            return true;
        }
        return false;
    }
    public UserAction createUserActionHelper(UserAction userAction){
        SimpleStatement simp = SimpleStatement.builder("INSERT INTO paintingSeller.userAction"+
                "(action_id, user_id, action, amount) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)")
                .addPositionalValues(userAction.getAction_id(), userAction.getUser_id(), userAction.getAction(), userAction.getAmount())
                .build();
        Flux.from(session.executeReactive(simp)).subscribe();
        return userAction;
    }
    public boolean createUserAction(UserAction userAction){
        if(userAction.getAction().equals("deposit")){
            deposit(userAction);
            createUserActionHelper(userAction);
        }
        if(userAction.getAction().equals("withdraw")){
            if(hasAmount(userAction.getUser_id(), userAction.getAmount())) {
                withdraw(userAction);
                createUserActionHelper(userAction);
            }
        }

        return true;
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
        return true;
    }
    public double getBalance(int UserID){
        return 0;
    }
    public boolean deposit(UserAction userAction){
        return true;
    }
    public boolean isForSale(int PaintingId){
        return true;
    }
    public boolean withdraw(UserAction userAction){
        return true;
    }
    public double getCost(int paintingId){
        return 0;
    }
}