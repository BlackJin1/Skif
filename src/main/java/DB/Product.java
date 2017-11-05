package DB;

/**
 * Created by Вячеслав on 06.11.2017.
 */
public class Product {
    private int id;
    private String good;
    private double price;
    private String category_name;

    public int getId() {
        return id;
    }

    public String getGood() {
        return good;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory_name() {
        return category_name;
    }



    public Product(int id, String good, double price, String category_name) {
        this.id = id;
        this.good = good;
        this.price = price;
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Товар: %s | Цена: %s | Категория: %s",
                this.id, this.good, this.price, this.category_name);
    }
}
