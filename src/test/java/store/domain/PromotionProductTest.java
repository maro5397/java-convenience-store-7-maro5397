package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.strategy.PromotionStrategy;
import store.domain.strategy.impl.LocalDateTimePromotionStrategy;

class PromotionProductTest {
    private final PromotionStrategy promotionStrategy = new LocalDateTimePromotionStrategy(
            "2024-01-01 00:00",
            "2024-12-31 00:00"
    );
    private final Promotion promotion = new Promotion("탄산2+1", 2, 1, promotionStrategy);

    @DisplayName("구매된 수량만큼 재고를 차감")
    @ParameterizedTest(name = "프로모션 구매 수량: {0}")
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void testDecrementStock(int quantity) {
        PromotionProduct cola = new PromotionProduct("콜라", 1000, 10, promotion);
        assertSoftly(softly -> {
            softly.assertThat(cola.decrementStock(quantity))
                    .isEqualTo(10 - quantity);
        });
    }
}