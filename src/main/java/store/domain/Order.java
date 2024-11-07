package store.domain;

public class Order {
    private final Product product;
    private final int quantity;
    private PromotionResult promotionResult;

    public Order(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void applyPromotionDiscount() {
        promotionResult = product.decrementStock(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public PromotionResult getPromotionResult() {
        return promotionResult;
    }
}
