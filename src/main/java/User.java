public class User {
    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
    public User(int user_id, String name, String password){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public boolean attemptLogin() {
        return false;
    }
    public boolean addPainting(){
        return false;
    }
    public boolean showInventory(){
        return true;
    }
}