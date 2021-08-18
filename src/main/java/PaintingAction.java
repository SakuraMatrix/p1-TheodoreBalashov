public class PaintingAction {

    int user_id;
    int painting_id;
    String action;
    double amount;

    public PaintingAction(int user_id, int painting_id, String action, double amount) {
        this.painting_id = painting_id;
        this.user_id = user_id;
        this.action = action;
        this.amount = amount;
    }

    public int getPainting_id() {
        return painting_id;
    }

    public void setPainting_id(int painting_id) {
        this.painting_id = painting_id;
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
        return "PaintingAction{" +
                "user_id=" + user_id +
                ", painting_id=" + painting_id +
                ", action='" + action + '\'' +
                ", amount=" + amount +
                '}';
    }
}
