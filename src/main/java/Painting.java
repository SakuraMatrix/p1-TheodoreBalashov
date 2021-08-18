public class Painting {
    int painting_id;
    int owner;
    String title;
    String url;
    String desc;
    String author;
    double price;
    boolean isForSale;

    public int getId() {
        return painting_id;
    }

    public void setId(int id) {
        this.painting_id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isForSale() {
        return isForSale;
    }

    public void setForSale(boolean forSale) {
        isForSale = forSale;
    }

    public Painting(int id, int owner, String title, String url, String desc, String author, boolean isForSale, double price){
        this.painting_id = id;
        this.owner = owner;
        this.title = title;
        this.url = url;
        this.desc = desc;
        this.author = author;
        this.isForSale = isForSale;
        this.price = price;
    }
    public boolean verifyURL(){
        return true;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String toString(){
        return "Painting{"+"painting_id="+painting_id+", owner="+owner+",title="+title+", url="+
                url+", desc="+desc+", author="+author+", isForSale="+isForSale+
                ", price="+price+"}";
    }
}