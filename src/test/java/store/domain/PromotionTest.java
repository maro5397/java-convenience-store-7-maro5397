package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.strategy.PromotionStrategy;

class PromotionTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Promotion softDrinkPromotion;
    private PromotionStrategy promotionStrategy;

    @DisplayName("현재 날짜가 프로모션 기간 내")
    @ParameterizedTest(name = "프로모션 날짜: {1}")
    @CsvSource(
            value = {
                    "1,2024-11-06 00:00", "2,2024-11-06 00:00", "3,2024-11-06 00:00"
            }
    )
    void testWithinPromotionPeriod(int quantity, String dateTime) {
        settingPromotion(dateTime);
        LocalDateTime now = LocalDateTime.parse(dateTime, formatter);
        assertSoftly(softly -> {
            softly.assertThat(softDrinkPromotion.calculatePromotionDiscount(quantity))
                    .isEqualTo((quantity / (2 + 1)));
        });
    }

    @DisplayName("현재 날짜가 프로모션 기간 외")
    @ParameterizedTest(name = "프로모션 날짜: {0}")
    @CsvSource(
            value = {
                    "1,2023-11-06 00:00", "2,2025-01-01 00:00",
                    "3,2023-11-06 00:00", "4,2025-01-01 00:00",
                    "5,2023-11-06 00:00", "6,2025-01-01 00:00"
            }
    )
    void testWithoutPromotionPeriod(int quantity, String dateTime) {
        settingPromotion(dateTime);
        assertSoftly(softly -> {
            softly.assertThat(softDrinkPromotion.calculatePromotionDiscount(quantity))
                    .isZero();
        });
    }

    private void settingPromotion(String dateTime) {
        promotionStrategy = new MockPromotionStrategy(
                LocalDateTime.parse("2024-01-01 00:00", formatter),
                LocalDateTime.parse("2024-12-31 23:59", formatter),
                LocalDateTime.parse(dateTime, formatter)
        );
        softDrinkPromotion = new Promotion(
                "탄산2+1",
                2,
                1,
                promotionStrategy
        );
    }

    private class MockPromotionStrategy implements PromotionStrategy {
        private final LocalDateTime startDate;
        private final LocalDateTime endDate;
        private final LocalDateTime localDateTime;

        public MockPromotionStrategy(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime localDateTime) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.localDateTime = localDateTime;
        }

        @Override
        public boolean getPromotionConditionChecker() {
            return (localDateTime.isEqual(startDate) || localDateTime.isAfter(startDate)) &&
                    (localDateTime.isEqual(endDate) || localDateTime.isBefore(endDate));
        }
    }
}