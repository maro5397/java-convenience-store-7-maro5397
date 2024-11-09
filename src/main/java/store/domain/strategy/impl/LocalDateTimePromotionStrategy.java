package store.domain.strategy.impl;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import store.domain.strategy.PromotionStrategy;

public class LocalDateTimePromotionStrategy implements PromotionStrategy {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public LocalDateTimePromotionStrategy(String startDate, String endDate) {
        this.startDate = LocalDate.parse(startDate).atStartOfDay();
        this.endDate = LocalDate.parse(endDate).atStartOfDay();
    }

    @Override
    public boolean getPromotionConditionChecker() {
        LocalDateTime now = DateTimes.now();
        return (now.isEqual(startDate) || now.isAfter(startDate)) &&
                (now.isEqual(endDate) || now.isBefore(endDate));
    }
}
