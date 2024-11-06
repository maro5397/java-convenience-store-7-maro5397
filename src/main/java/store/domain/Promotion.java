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

    public Receipt calculatePromotionDiscount(int quantity) {
        if (!isWithinPromotionPeriod()) {
            return new Receipt(0, 0, quantity);
        }
        int freeItemCount = (quantity / (buy + get)) * get;
        int paidItemCount = freeItemCount / get * buy;
        return new Receipt(freeItemCount, paidItemCount, quantity - paidItemCount - freeItemCount);
    }

    private boolean isWithinPromotionPeriod() {
        return this.promotionStrategy.getPromotionConditionChecker();
    }
}
