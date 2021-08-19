public class PaintingAction {
    private int action_id;
    private int user_id;
    private int painting_id;
    private String action;
    private double amount;

    public PaintingAction(int action_id, int user_id, int painting_id, String action, double amount) {
        this.action_id=action_id;
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

    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
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
