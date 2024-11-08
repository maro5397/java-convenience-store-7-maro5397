package store.domain;

public class Order {
    private final Product product;
    private final Promotion promotion;
    private boolean canApplyAdditionalPromotion;
    private OrderResult orderResult;
    private int quantity;

    public Order(Product product, Promotion promotion, int quantity) {
        this.product = product;
        this.promotion = promotion;
        this.quantity = quantity;
        consumePromotionProduct();
    }

    public Product getProduct() {
        return product;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public OrderResult getOrderResult() {
        return orderResult;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean getCanApplyAdditionalPromotion() {
        return canApplyAdditionalPromotion;
    }

    public void applyAdditionalPromotion() {
        quantity += promotion.getGet();
        consumePromotionProduct();
    }

    public void deleteNonePromotionAppliedProductCount() {
        quantity -= this.orderResult.getPromotionProductConsumeCount()
                + this.orderResult.getProductConsumeCount()
                - this.orderResult.getPromotionApplyfreeItemCount()
                - this.orderResult.getPromotionApplypaidItemCount();
        consumePromotionProduct();
    }

    public void applyConsumeStock() {
        if (!this.product.getPromotion().isEmpty()) {
            this.product.decrementPromotionStock(this.orderResult.getPromotionProductConsumeCount());
        }
        this.product.decrementStock(this.orderResult.getProductConsumeCount());
    }

    private void canGetAdditionalProductByPromotion() {
        this.canApplyAdditionalPromotion = this.promotion.canApplyPromotion(quantity,
                this.product.getPromotionStock() - this.orderResult.getPromotionProductConsumeCount());
    }

    private void consumePromotionProduct() {
        if (this.product.getPromotion().isEmpty()) {
            this.orderResult = new OrderResult(0, 0, 0, quantity);
            return;
        }
        OrderResult orderResult = this.promotion.calculatePromotionDiscount(this.product.getPromotionStock(),
                this.quantity);
        int productConsumeCount = orderResult.getProductConsumeCount();
        int promotionProductConsumeCount = orderResult.getPromotionProductConsumeCount();
        for (int i = 0;
             i < orderResult.getProductConsumeCount()
                     && this.product.getPromotionStock() > promotionProductConsumeCount;
             i++) {
            promotionProductConsumeCount += 1;
            productConsumeCount -= 1;
        }
        this.orderResult = new OrderResult(orderResult.getPromotionApplyfreeItemCount(),
                orderResult.getPromotionApplypaidItemCount(),
                promotionProductConsumeCount, productConsumeCount);
        canGetAdditionalProductByPromotion();
    }
}
