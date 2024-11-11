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

    private void settingPromotion(String dateTime) {
        PromotionStrategy promotionStrategy = new MockPromotionStrategy(
                LocalDateTime.parse("2024-01-01 00:00", formatter),
                LocalDateTime.parse("2024-12-31 23:59", formatter),
                LocalDateTime.parse(dateTime, formatter)
        );
        softDrinkPromotion = Promotion.create(2, 2, promotionStrategy);
    }

    @DisplayName("현재 날짜가 프로모션 기간 내")
    @ParameterizedTest(name = "프로모션 날짜: {1}")
    @CsvSource(
            value = {
                    "6,2024-11-06 00:00,2,2", "7,2024-11-06 00:00,2,2", "8,2024-11-06 00:00,4,4",
                    "19,2024-11-06 00:00,4,4", "30,2024-11-06 00:00,4,4", "50,2024-11-06 00:00,4,4"
            }
    )
    void testWithinPromotionPeriod(int quantity, String dateTime, int freeItemCount, int paidItemCount) {
        settingPromotion(dateTime);
        OrderResult orderResult = softDrinkPromotion.calculatePromotionDiscount(10, quantity);
        assertSoftly(softly -> {
            softly.assertThat(orderResult.getPromotionApplyFreeItemQuantity()).isEqualTo(freeItemCount);
            softly.assertThat(orderResult.getPromotionApplyPaidItemQuantity()).isEqualTo(paidItemCount);
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
        OrderResult orderResult = softDrinkPromotion.calculatePromotionDiscount(10, quantity);
        assertSoftly(softly -> {
            softly.assertThat(orderResult.getPromotionApplyFreeItemQuantity()).isEqualTo(0);
            softly.assertThat(orderResult.getPromotionApplyPaidItemQuantity()).isEqualTo(0);
        });
    }

    @DisplayName("프로모션 추가 적용 가능")
    @ParameterizedTest(name = "구매 수량: {0}")
    @CsvSource(
            value = {
                    "2,2024-11-06 00:00",
                    "6,2024-11-06 00:00",
                    "10,2024-11-06 00:00"
            }
    )
    void testCanApplyPromotion(int quantity, String dateTime) {
        settingPromotion(dateTime);
        assertSoftly(softly -> {
            softly.assertThat(softDrinkPromotion.canApplyPromotion(quantity, 10)).isTrue();
        });
    }

    @DisplayName("프로모션 추가 적용 불가")
    @ParameterizedTest(name = "프로모션 날짜: {0}")
    @CsvSource(
            value = {
                    "3,2024-11-06 00:00",
                    "7,2024-11-06 00:00",
                    "9,2024-11-06 00:00"
            }
    )
    void testCannotApplyPromotion(int quantity, String dateTime) {
        settingPromotion(dateTime);
        assertSoftly(softly -> {
            softly.assertThat(softDrinkPromotion.canApplyPromotion(quantity, 10)).isFalse();
        });
    }

    private record MockPromotionStrategy(
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime localDateTime
    ) implements PromotionStrategy {
        @Override
        public boolean getPromotionConditionChecker() {
            return (localDateTime.isEqual(startDate) || localDateTime.isAfter(startDate)) &&
                    (localDateTime.isEqual(endDate) || localDateTime.isBefore(endDate));
        }
    }
}