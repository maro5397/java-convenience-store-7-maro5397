package store.domain;

public class OrderResult {
    private final int promotionApplyFreeItemCount;
    private final int promotionApplyPaidItemCount;
    private final int promotionProductConsumeCount;
    private final int productConsumeCount;

    private OrderResult(
            int freeItemCount,
            int paidItemCount,
            int promotionProductConsumeCount,
            int productConsumeCount
    ) {
        this.promotionApplyFreeItemCount = freeItemCount;
        this.promotionApplyPaidItemCount = paidItemCount;
        this.promotionProductConsumeCount = promotionProductConsumeCount;
        this.productConsumeCount = productConsumeCount;
    }

    public static OrderResult create(
            int freeItemCount,
            int paidItemCount,
            int promotionProductConsumeCount,
            int productConsumeCount
    ) {
        return new OrderResult(freeItemCount, paidItemCount, promotionProductConsumeCount, productConsumeCount);
    }

    public int getPromotionApplyFreeItemCount() {
        return promotionApplyFreeItemCount;
    }

    public int getPromotionApplyPaidItemCount() {
        return promotionApplyPaidItemCount;
    }

    public int getPromotionProductConsumeCount() {
        return promotionProductConsumeCount;
    }

    public int getProductConsumeCount() {
        return productConsumeCount;
    }

    public int getNoneDiscountPromotionStockCount() {
        return promotionProductConsumeCount + productConsumeCount
                - promotionApplyFreeItemCount
                - promotionApplyPaidItemCount;
    }
}
