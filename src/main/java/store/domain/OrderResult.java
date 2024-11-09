package store.domain;

public class OrderResult {
    private final int promotionApplyfreeItemCount;
    private final int promotionApplypaidItemCount;
    private final int promotionProductConsumeCount;
    private final int productConsumeCount;
    private final boolean isPromotionApply;

    public OrderResult(int freeItemCount, int paidItemCount,
                       int promotionProductConsumeCount, int productConsumeCount, boolean isPromotionApply) {
        this.promotionApplyfreeItemCount = freeItemCount;
        this.promotionApplypaidItemCount = paidItemCount;
        this.promotionProductConsumeCount = promotionProductConsumeCount;
        this.productConsumeCount = productConsumeCount;
        this.isPromotionApply = isPromotionApply;
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

    public boolean isPromotionApply() {
        return isPromotionApply;
    }

    public int getNoneDiscountPromotionStockCount() {
        return promotionProductConsumeCount + productConsumeCount - promotionApplyfreeItemCount
                - promotionApplypaidItemCount;
    }
}
