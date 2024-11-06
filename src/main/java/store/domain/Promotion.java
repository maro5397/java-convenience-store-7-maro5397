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

    public int calculatePromotionDiscount(int quantity) {
        if (!isWithinPromotionPeriod()) {
            return 0;
        }
        return (quantity / (buy + get)) * get;
    }

    private boolean isWithinPromotionPeriod() {
        return this.promotionStrategy.getPromotionConditionChecker();
    }
}
