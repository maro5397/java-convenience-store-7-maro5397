package store.domain;

public class PromotionResult {
    private final int freeItemCount;
    private final int paidItemCount;
    private final int noneDiscountItemCount;

    public PromotionResult(int freeItemCount, int paidItemCount, int noneDiscountItemCount) {
        this.freeItemCount = freeItemCount;
        this.paidItemCount = paidItemCount;
        this.noneDiscountItemCount = noneDiscountItemCount;
    }

    public int getFreeItemCount() {
        return freeItemCount;
    }

    public int getPaidItemCount() {
        return paidItemCount;
    }

    public int getNoneDiscountItemCount() {
        return noneDiscountItemCount;
    }
}
