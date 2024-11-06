package store.domain;

public class PromotionProduct {
    private final String name;
    private final int price;
    private int stock;
    private final Promotion promotion;

    public PromotionProduct(String name, int price, int stock, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public int decrementStock(int quantity) {
        stock -= quantity;
        return stock;
    }
}
