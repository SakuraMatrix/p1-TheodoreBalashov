import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DatabaseTalker {
    private CqlSession session;
    public DatabaseTalker(CqlSession session) {
        this.session = session;
    }
    public boolean doesUsernameExist(String user){
        return true;
    }
    public boolean createUser(User user){
        return true;
    }
    public Painting createPainting(Painting painting){

        SimpleStatement simp = SimpleStatement.builder("INSERT INTO paintingSeller.painting"+
                "(painting_id, owner, title, url, description, author, isForSale, price) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)")
                .addPositionalValues(painting.painting_id, painting.owner, painting.title, painting.url, painting.desc, painting.author,
                        painting.isForSale, painting.price)
                .build();
        Flux.from(session.executeReactive(simp)).subscribe();
        return painting;
    }
    public boolean hasAmount(User currentUser, double amt) {
        return true;
    }
    public double getBalance(int UserID){
        return 0;
    }
    public double getPaintingCost(int PaintingID){
        return 0;
    }
    public boolean deposit(User user, double amt){
        return true;
    }
    public boolean isForSale(Painting painting){
        return true;
    }
    public boolean withdraw(User user, double amt){
        return true;
    }
    public boolean sellPainting(User user, Painting painting){
        return true;
    }
    public boolean buyPainting(User user, Painting painting){
        return true;
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
        return returnAUser("SELECT * FROM paintingSeller.userActivity");
    }
    public boolean createPaintingAction(PaintingAction paintingAction){
        return true;
    }
    public boolean createUserAction(UserAction userAction){
        return true;
    }
    public Flux<PaintingAction> returnAPaintingAction(String query){
        return Flux.from(session.executeReactive(query))
                .map(row -> new PaintingAction(row.getInt("action_id"), row.getInt("painting_id"),
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
                        row.getString("password")));
    }
    public Flux<Painting> returnAPainting(String query){
        return Flux.from(session.executeReactive(query))
                .map(row -> new Painting(row.getInt("painting_id"), row.getInt("owner"),
                        row.getString("title"), row.getString("url"),
                        row.getString("description"), row.getString("author"),
                        row.getBoolean("isForSale"), row.getDouble("price") ));
    }
}