package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.strategy.PromotionStrategy;
import store.domain.strategy.impl.LocalDateTimePromotionStrategy;

class PromotionProductTest {
    private final PromotionStrategy promotionStrategy = new LocalDateTimePromotionStrategy(
            "2024-01-01",
            "2024-12-31"
    );
    private final Promotion promotion = new Promotion("탄산2+1", 2, 1, promotionStrategy);

    @DisplayName("구매된 수량만큼 프로모션 재고를 차감")
    @ParameterizedTest(name = "프로모션 구매 수량: {0}")
    @CsvSource(
            value = {
                    "1,0,0", "2,0,0", "3,1,2", "4,1,2", "5,1,2",
                    "6,2,4", "7,2,4", "8,2,4", "9,3,6", "10,3,6"
            }
    )
    void testDecrementStock(int quantity, int freeItemCount, int paidItemCount) {
        PromotionProduct cola = new PromotionProduct("콜라", 1000, 10, promotion);
        PromotionResult promotionResult = cola.decrementStock(quantity);
        assertSoftly(softly -> {
            softly.assertThat(promotionResult.getFreeItemCount()).isEqualTo(freeItemCount);
            softly.assertThat(promotionResult.getPaidItemCount()).isEqualTo(paidItemCount);
        });
    }

//    @DisplayName("구매된 수량보다 프로모션 재고가 부족")
//    @ParameterizedTest(name = "프로모션 구매 수량: {0}")
//    @CsvSource(
//            value = {
//                    "11,3,7,1", "12,3,7,2", "13,3,7,3"
//            }
//    )
//    void testDecrementLessStock(int quantity, int freeItemCount, int paidItemCount, int noneDiscountItemCount) {
//        PromotionProduct cola = new PromotionProduct("콜라", 1000, 10, promotion);
//        PromotionResult promotionResult = cola.decrementStock(quantity);
//        assertSoftly(softly -> {
//            softly.assertThat(promotionResult.getFreeItemCount()).isEqualTo(freeItemCount);
//            softly.assertThat(promotionResult.getPaidItemCount()).isEqualTo(paidItemCount);
//            softly.assertThat(promotionResult.getNoneDiscountItemCount()).isEqualTo(noneDiscountItemCount);
//        });
//    }
}