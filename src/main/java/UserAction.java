public class UserAction {
    int action_id;
    int user_id;
    String action;
    double amount;

    public UserAction(int action_id, int user_id, String action, double amount) {
        this.action_id = action_id;
        this.user_id = user_id;
        this.action = action;
        this.amount = amount;
    }

    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "UserAction{" +
                "action_id=" + action_id +
                ", user_id=" + user_id +
                ", action='" + action + '\'' +
                ", amount=" + amount +
                '}';
    }
}
