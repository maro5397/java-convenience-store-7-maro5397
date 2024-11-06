package store.domain;

import store.domain.strategy.PromotionStrategy;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final PromotionStrategy promotionStrategy;

    public Promotion(
            String name,
            int buy,
            int get,
            PromotionStrategy promotionStrategy
    ) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.promotionStrategy = promotionStrategy;
    }

    public PromotionResult calculatePromotionDiscount(int stock, int quantity) {
        int freeItemCount = 0;
        int paidItemCount = 0;
        if (!isWithinPromotionPeriod()) {
            return new PromotionResult(freeItemCount, paidItemCount, quantity);
        }
        while (stock - freeItemCount - paidItemCount >= get + buy
                && quantity - freeItemCount - paidItemCount >= get + buy) {
            freeItemCount += get;
            paidItemCount += buy;
        }
        return new PromotionResult(freeItemCount, paidItemCount, quantity - paidItemCount - freeItemCount);
    }

    private boolean isWithinPromotionPeriod() {
        return this.promotionStrategy.getPromotionConditionChecker();
    }
}
