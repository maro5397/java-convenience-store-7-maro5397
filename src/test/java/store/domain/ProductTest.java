package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {
    @DisplayName("상품의 기본 정보 관리")
    @ParameterizedTest(name = "상품이름: {0}, 상품가격: {1}, 상품재고: {2}, 상품프로모션종류: {3}")
    @CsvSource(
            value = {"콜라,1000,10,탄산2+1", "콜라,1000,10,null", "사이다,1000,8,탄산2+1", "사이다,1000,7,null"},
            nullValues = {"null"}
    )
    void testProductFieldManage(String name, int price, int stock, String promotionStock) {
        Product product = new Product(name, price, stock, promotionStock);
        assertSoftly(softly -> {
            softly.assertThat(product.getName())
                    .isEqualTo(name);
            softly.assertThat(product.getPrice())
                    .isEqualTo(price);
            softly.assertThat(product.getStock())
                    .isEqualTo(stock);
            softly.assertThat(product.getPromotionStock())
                    .isEqualTo(promotionStock);
        });
    }

    @DisplayName("구매 수량이 재고 수량을 초과하지 않을 경우")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(10)")
    @ValueSource(ints = {1,2,3,4,5,6,7,8,9,10})
    void testHasSufficientStock(int quantity) {
        Product product = new Product("콜라", 1000, 10, null);
        boolean isSufficientStock = product.hasSufficientStock(quantity);
        assertSoftly(softly -> {
            softly.assertThat(isSufficientStock).isTrue();
        });
    }

    @DisplayName("구매 수량이 재고 수량을 초과할 경우")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(10)")
    @ValueSource(ints = {11,12,13,14,15,16,17,18,19,20})
    void testHasNotSufficientStock(int quantity) {
        Product product = new Product("콜라", 1000, 10, null);
        boolean isSufficientStock = product.hasSufficientStock(quantity);
        assertSoftly(softly -> {
            softly.assertThat(isSufficientStock).isFalse();
        });
    }
}