package store.domain.strategy.impl;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import store.domain.strategy.PromotionStrategy;

public class LocalDateTimePromotionStrategy implements PromotionStrategy {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public LocalDateTimePromotionStrategy(String startDate, String endDate) {
        this.startDate = LocalDateTime.parse(startDate, formatter);;
        this.endDate = LocalDateTime.parse(endDate, formatter);;
    }

    @Override
    public boolean getPromotionConditionChecker() {
        LocalDateTime now = DateTimes.now();
        return (now.isEqual(startDate) || now.isAfter(startDate)) &&
                (now.isEqual(endDate) || now.isBefore(endDate));
    }
}
