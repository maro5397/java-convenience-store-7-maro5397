package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderResultTest {
    @DisplayName("주문 결과 정상 생성 테스트")
    @ParameterizedTest(name = "프로모션 무료 상품 개수: {0}, 프로모션 유료 상품 개수: {1}, 상품 주문 개수: {2}")
    @CsvSource(
            value = {"10,11,12", "13,14,15"}
    )
    void testProductFieldManage(int freeItemCount, int paidItemCount, int quantity) {
        OrderResult orderResult = new OrderResult(freeItemCount, paidItemCount,
                freeItemCount + paidItemCount, quantity - (freeItemCount + paidItemCount), true);
        assertSoftly(softly -> {
            softly.assertThat(orderResult.getPromotionApplyfreeItemCount())
                    .isEqualTo(freeItemCount);
            softly.assertThat(orderResult.getPromotionApplypaidItemCount())
                    .isEqualTo(paidItemCount);
            softly.assertThat(orderResult.getPromotionProductConsumeCount())
                    .isEqualTo(freeItemCount + paidItemCount);
            softly.assertThat(orderResult.getProductConsumeCount())
                    .isEqualTo(quantity - (freeItemCount + paidItemCount));
            softly.assertThat(orderResult.isPromotionApply())
                    .isEqualTo(true);
        });
    }
}