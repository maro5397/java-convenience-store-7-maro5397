package store.domain;

public class OrderResult {
    private final int promotionApplyfreeItemCount;
    private final int promotionApplypaidItemCount;
    private final int promotionProductConsumeCount;
    private final int productConsumeCount;

    public OrderResult(int freeItemCount, int paidItemCount,
                       int promotionProductConsumeCount, int productConsumeCount) {
        this.promotionApplyfreeItemCount = freeItemCount;
        this.promotionApplypaidItemCount = paidItemCount;
        this.promotionProductConsumeCount = promotionProductConsumeCount;
        this.productConsumeCount = productConsumeCount;
    }

    public int getPromotionApplyfreeItemCount() {
        return promotionApplyfreeItemCount;
    }

    public int getPromotionApplypaidItemCount() {
        return promotionApplypaidItemCount;
    }

    public int getPromotionProductConsumeCount() {
        return promotionProductConsumeCount;
    }

    public int getProductConsumeCount() {
        return productConsumeCount;
    }

    public int getNoneDiscountPromotionStockCount() {
        return promotionProductConsumeCount + productConsumeCount - promotionApplyfreeItemCount
                - promotionApplypaidItemCount;
    }
}
