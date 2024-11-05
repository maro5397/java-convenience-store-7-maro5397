package store.domain;

public class Product {
    private final String name;
    private final int price;
    private final int stock;
    private final String promotionStock;

    public Product(String name, int price, int stock, String promotionStock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotionStock = promotionStock;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getPromotionStock() {
        return promotionStock;
    }
}
