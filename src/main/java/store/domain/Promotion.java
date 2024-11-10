package store.domain;

import store.domain.strategy.PromotionStrategy;

public class Promotion {
    private final int buy;
    private final int get;
    private final PromotionStrategy promotionStrategy;

    private Promotion(int buy, int get, PromotionStrategy promotionStrategy) {
        this.buy = buy;
        this.get = get;
        this.promotionStrategy = promotionStrategy;
    }

    public static Promotion create(int buy, int get, PromotionStrategy promotionStrategy) {
        return new Promotion(buy, get, promotionStrategy);
    }

    public int getGet() {
        return get;
    }

    public OrderResult calculatePromotionDiscount(int promotionStock, int quantity) {
        if (!isWithinPromotionPeriod()) {
            return OrderResult.create(0, 0, 0, quantity);
        }
        return getWithinPromotionDiscount(promotionStock, quantity);
    }

    public boolean canApplyPromotion(int quantity, int remainingPromotionStock) {
        return quantity % (get + buy) - buy == 0 && remainingPromotionStock >= get && isWithinPromotionPeriod();
    }

    public boolean isWithinPromotionPeriod() {
        return this.promotionStrategy.getPromotionConditionChecker();
    }

    private OrderResult getWithinPromotionDiscount(int promotionStock, int quantity) {
        int freeItemCount = 0;
        int paidItemCount = 0;
        while (promotionStock - freeItemCount - paidItemCount >= get + buy
                && quantity - freeItemCount - paidItemCount >= get + buy) {
            freeItemCount += get;
            paidItemCount += buy;
        }
        return OrderResult.create(freeItemCount, paidItemCount,
                freeItemCount + paidItemCount, quantity - paidItemCount - freeItemCount);
    }
}
