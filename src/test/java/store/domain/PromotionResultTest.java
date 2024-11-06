package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromotionResultTest {
    @DisplayName("영수증 정상 생성 테스트")
    @ParameterizedTest(name = "프로모션 무료 상품 개수: {0}, 프로모션 유료 상품 개수: {1}, 일반 상품 개수: {2}")
    @CsvSource(
            value = {"10,11,12", "13,14,15"}
    )
    void testProductFieldManage(int freeItemCount, int paidItemCount, int noneDiscountItemCount) {
        PromotionResult promotionResult = new PromotionResult(freeItemCount, paidItemCount, noneDiscountItemCount);
        assertSoftly(softly -> {
            softly.assertThat(promotionResult.getFreeItemCount())
                    .isEqualTo(freeItemCount);
            softly.assertThat(promotionResult.getPaidItemCount())
                    .isEqualTo(paidItemCount);
            softly.assertThat(promotionResult.getNoneDiscountItemCount())
                    .isEqualTo(noneDiscountItemCount);
        });
    }
}