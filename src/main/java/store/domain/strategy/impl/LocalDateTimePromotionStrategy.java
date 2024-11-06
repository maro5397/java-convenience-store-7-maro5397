package store.domain.strategy.impl;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import store.domain.strategy.PromotionStrategy;

public class LocalDateTimePromotionStrategy implements PromotionStrategy {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public LocalDateTimePromotionStrategy(final LocalDateTime startDate, final LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean getPromotionConditionChecker() {
        LocalDateTime now = DateTimes.now();
        return (now.isEqual(startDate) || now.isAfter(startDate)) &&
                (now.isEqual(endDate) || now.isBefore(endDate));
    }
}
