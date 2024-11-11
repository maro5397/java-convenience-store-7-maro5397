package store.domain;

import store.common.constant.ErrorMessage;

public class Order {
    private final Product product;
    private final Promotion promotion;
    private int quantity;
    private OrderResult orderResult;
    private boolean canApplyAdditionalPromotion;

    private Order(Product product, Promotion promotion, int quantity) {
        this.product = product;
        this.promotion = promotion;
        this.quantity = quantity;
        validate();
        generateOrderResultWithPromotion();
    }

    public static Order create(Product product, Promotion promotion, int quantity) {
        return new Order(product, promotion, quantity);
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
        return OrderResult.create(
                orderResult.getPromotionApplyFreeItemQuantity(),
                orderResult.getPromotionApplyPaidItemQuantity(),
                orderResult.getPromotionProductConsumeQuantity(),
                orderResult.getProductConsumeQuantity()
        );
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean getCanApplyAdditionalPromotion() {
        return canApplyAdditionalPromotion;
    }

    public void applyAdditionalPromotion() {
        quantity += promotion.getGet();
        generateOrderResultWithPromotion();
    }

    public void reduceQuantityForNonDiscountedOrder() {
        quantity -= this.orderResult.getPromotionProductConsumeQuantity()
                + this.orderResult.getProductConsumeQuantity()
                - this.orderResult.getPromotionApplyFreeItemQuantity()
                - this.orderResult.getPromotionApplyPaidItemQuantity();
        generateOrderResultWithPromotion();
    }

    public void consumeStockForOrder() {
        if (!this.product.getPromotion().isEmpty()) {
            this.product.decrementPromotionStock(this.orderResult.getPromotionProductConsumeQuantity());
        }
        this.product.decrementStock(this.orderResult.getProductConsumeQuantity());
    }

    private void setCanApplyAdditionalPromotion() {
        this.canApplyAdditionalPromotion = this.promotion.canApplyPromotion(
                quantity,
                this.product.getPromotionStock() - this.orderResult.getPromotionProductConsumeQuantity()
        );
    }

    private void generateOrderResultWithPromotion() {
        if (this.product.getPromotion().isEmpty()) {
            this.orderResult = OrderResult.create(0, 0, 0, quantity);
            return;
        }
        OrderResult orderResult = this.promotion.calculatePromotionDiscount(
                this.product.getPromotionStock(),
                this.quantity
        );
        this.orderResult = consumePromotionalStockFirst(orderResult);
        setCanApplyAdditionalPromotion();
    }

    private OrderResult consumePromotionalStockFirst(OrderResult orderResult) {
        int productConsumeQuantity = orderResult.getProductConsumeQuantity();
        int promotionProductConsumeQuantity = orderResult.getPromotionProductConsumeQuantity();
        promotionProductConsumeQuantity = calculatePromotionProductConsumeQuantity(
                productConsumeQuantity,
                promotionProductConsumeQuantity
        );
        productConsumeQuantity -= (promotionProductConsumeQuantity - orderResult.getPromotionProductConsumeQuantity());
        return createUpdatedOrderResult(orderResult, promotionProductConsumeQuantity, productConsumeQuantity);
    }

    private int calculatePromotionProductConsumeQuantity(
            int productConsumeQuantity,
            int promotionProductConsumeQuantity
    ) {
        for (
                int i = 0;
                (i < productConsumeQuantity) && (this.product.getPromotionStock() > promotionProductConsumeQuantity);
                i++
        ) {
            promotionProductConsumeQuantity += 1;
        }
        return promotionProductConsumeQuantity;
    }

    private OrderResult createUpdatedOrderResult(
            OrderResult orderResult,
            int promotionProductConsumeQuantity,
            int productConsumeQuantity
    ) {
        return OrderResult.create(orderResult.getPromotionApplyFreeItemQuantity(),
                orderResult.getPromotionApplyPaidItemQuantity(),
                promotionProductConsumeQuantity, productConsumeQuantity);
    }

    private void validate() {
        validateQuantity();
    }

    private void validateQuantity() {
        if (this.quantity > product.getPromotionStock() + product.getStock()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_OUT_OF_STOCK.getMessage());
        }
    }
}
