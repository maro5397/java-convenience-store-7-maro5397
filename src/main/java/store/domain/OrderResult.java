package store.domain;

public class OrderResult {
    private final int promotionApplyFreeItemQuantity;
    private final int promotionApplyPaidItemQuantity;
    private final int promotionProductConsumeQuantity;
    private final int productConsumeQuantity;

    private OrderResult(
            int promotionApplyFreeItemQuantity,
            int promotionApplyPaidItemQuantity,
            int promotionProductConsumeQuantity,
            int productConsumeQuantity
    ) {
        this.promotionApplyFreeItemQuantity = promotionApplyFreeItemQuantity;
        this.promotionApplyPaidItemQuantity = promotionApplyPaidItemQuantity;
        this.promotionProductConsumeQuantity = promotionProductConsumeQuantity;
        this.productConsumeQuantity = productConsumeQuantity;
    }

    public static OrderResult create(
            int promotionApplyFreeItemQuantity,
            int promotionApplyPaidItemQuantity,
            int promotionProductConsumeQuantity,
            int productConsumeQuantity
    ) {
        return new OrderResult(
                promotionApplyFreeItemQuantity,
                promotionApplyPaidItemQuantity,
                promotionProductConsumeQuantity,
                productConsumeQuantity
        );
    }

    public int getPromotionApplyFreeItemQuantity() {
        return promotionApplyFreeItemQuantity;
    }

    public int getPromotionApplyPaidItemQuantity() {
        return promotionApplyPaidItemQuantity;
    }

    public int getPromotionProductConsumeQuantity() {
        return promotionProductConsumeQuantity;
    }

    public int getProductConsumeQuantity() {
        return productConsumeQuantity;
    }

    public int getNonDiscountedOrderQuantity() {
        return promotionProductConsumeQuantity + productConsumeQuantity
                - promotionApplyFreeItemQuantity
                - promotionApplyPaidItemQuantity;
    }
}
