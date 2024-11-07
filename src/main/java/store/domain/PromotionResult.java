package store.domain;

public class PromotionResult {
    private final int freeItemCount;
    private final int paidItemCount;
    private final int promotionProductConsumeCount;
    private final int productConsumeCount;

    public PromotionResult(int freeItemCount, int paidItemCount,
                           int promotionProductConsumeCount, int productConsumeCount) {
        this.freeItemCount = freeItemCount;
        this.paidItemCount = paidItemCount;
        this.promotionProductConsumeCount = promotionProductConsumeCount;
        this.productConsumeCount = productConsumeCount;
    }

    public int getFreeItemCount() {
        return freeItemCount;
    }

    public int getPaidItemCount() {
        return paidItemCount;
    }

    public int getPromotionProductConsumeCount() {
        return promotionProductConsumeCount;
    }

    public int getProductConsumeCount() {
        return productConsumeCount;
    }

    public int getNoneDiscountPromotionStockCount() {
        return promotionProductConsumeCount - freeItemCount - paidItemCount;
    }
}
