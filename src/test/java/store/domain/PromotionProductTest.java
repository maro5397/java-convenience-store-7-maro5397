package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.strategy.PromotionStrategy;
import store.domain.strategy.impl.LocalDateTimePromotionStrategy;

class PromotionProductTest {
    private final PromotionStrategy promotionStrategy = new LocalDateTimePromotionStrategy(
            "2024-01-01 00:00",
            "2024-12-31 00:00"
    );
    private final Promotion promotion = new Promotion("탄산2+1", 2, 1, promotionStrategy);

    @DisplayName("구매된 수량만큼 프로모션 재고를 차감")
    @ParameterizedTest(name = "프로모션 구매 수량: {0}")
    @CsvSource(
            value = {
                    "1,0,1,0", "2,0,2,0", "3,1,2,0", "4,1,3,0", "5,1,4,0",
                    "6,2,4,0", "7,2,5,0", "8,2,6,0", "9,3,6,0", "10,3,7,0"
            }
    )
    void testDecrementStock(int quantity, int freeItemCount, int paidItemCount, int noneDiscountItemCount) {
        PromotionProduct cola = new PromotionProduct("콜라", 1000, 10, promotion);
        PromotionResult promotionResult = cola.decrementStock(quantity);
        assertSoftly(softly -> {
            softly.assertThat(promotionResult.getFreeItemCount()).isEqualTo(freeItemCount);
            softly.assertThat(promotionResult.getPaidItemCount()).isEqualTo(paidItemCount);
            softly.assertThat(promotionResult.getNoneDiscountItemCount()).isEqualTo(noneDiscountItemCount);
        });
    }

    @DisplayName("구매된 수량보다 프로모션 재고가 부족")
    @ParameterizedTest(name = "프로모션 구매 수량: {0}")
    @CsvSource(
            value = {
                    "11,3,7,1", "12,3,7,2", "13,3,7,3"
            }
    )
    void testDecrementLessStock(int quantity, int freeItemCount, int paidItemCount, int noneDiscountItemCount) {
        PromotionProduct cola = new PromotionProduct("콜라", 1000, 10, promotion);
        PromotionResult promotionResult = cola.decrementStock(quantity);
        assertSoftly(softly -> {
            softly.assertThat(promotionResult.getFreeItemCount()).isEqualTo(freeItemCount);
            softly.assertThat(promotionResult.getPaidItemCount()).isEqualTo(paidItemCount);
            softly.assertThat(promotionResult.getNoneDiscountItemCount()).isEqualTo(noneDiscountItemCount);
        });
    }
}