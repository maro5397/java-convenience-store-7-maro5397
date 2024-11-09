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
        validate();
        createOrderResult();
    }

    public Product getProduct() {
        return product;
    }

    public Promotion getPromotion() {
        if (promotion == null || !promotion.isWithinPromotionPeriod()) {
            return null;
        }
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
        createOrderResult();
    }

    public void deleteNonePromotionAppliedProductCount() {
        quantity -= this.orderResult.getPromotionProductConsumeCount()
                + this.orderResult.getProductConsumeCount()
                - this.orderResult.getPromotionApplyfreeItemCount()
                - this.orderResult.getPromotionApplypaidItemCount();
        createOrderResult();
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

    private void createOrderResult() {
        if (this.product.getPromotion().isEmpty()) {
            this.orderResult = new OrderResult(0, 0, 0, quantity);
            return;
        }
        OrderResult orderResult = this.promotion.calculatePromotionDiscount(this.product.getPromotionStock(),
                this.quantity);
        this.orderResult = applyPromotionProductFirst(orderResult);
        canGetAdditionalProductByPromotion();
    }

    private OrderResult applyPromotionProductFirst(OrderResult orderResult) {
        int productConsumeCount = orderResult.getProductConsumeCount();
        int promotionProductConsumeCount = orderResult.getPromotionProductConsumeCount();
        promotionProductConsumeCount = calculatePromotionProductConsumeCount(productConsumeCount,
                promotionProductConsumeCount);
        productConsumeCount -= (promotionProductConsumeCount - orderResult.getPromotionProductConsumeCount());
        return createUpdatedOrderResult(orderResult, promotionProductConsumeCount, productConsumeCount);
    }

    private int calculatePromotionProductConsumeCount(int productConsumeCount, int promotionProductConsumeCount) {
        for (int i = 0;
             (i < productConsumeCount) && (this.product.getPromotionStock() > promotionProductConsumeCount);
             i++) {
            promotionProductConsumeCount += 1;
        }
        return promotionProductConsumeCount;
    }

    private OrderResult createUpdatedOrderResult(OrderResult orderResult, int promotionProductConsumeCount,
                                                 int productConsumeCount) {
        return new OrderResult(orderResult.getPromotionApplyfreeItemCount(),
                orderResult.getPromotionApplypaidItemCount(),
                promotionProductConsumeCount, productConsumeCount);
    }

    private void validate() {
        validateQuantity();
    }

    private void validateQuantity() {
        if (this.quantity > product.getPromotionStock() + product.getStock()) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }
}
