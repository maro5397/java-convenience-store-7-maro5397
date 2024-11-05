package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {
    @DisplayName("상품의 기본 정보 관리")
    @ParameterizedTest(name = "상품이름: {0}, 상품가격: {1}, 상품재고: {2}, 프로모션상품재고: {3}")
    @CsvSource(
            value = {"콜라,1000,10,10", "사이다,1000,8,0", "감자칩,1500,5,5", "초코바,1200,5,5"}
    )
    void testProductFieldManage(String name, int price, int stock, int promotionStock) {
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

    @DisplayName("상품의 이름이 null일 경우")
    @ParameterizedTest(name = "상품이름: {0}, 상품가격: {1}, 상품재고: {2}, 프로모션상품재고: {3}")
    @CsvSource(
            value = {"null,1000,8,0"},
            nullValues = "null"
    )
    void testProductFieldNameNullException(String name, int price, int stock, int promotionStock) {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Product(name, price, stock, promotionStock))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("상품의 이름이 공백, 빈 문자열일 경우")
    @ParameterizedTest(name = "상품이름: {0}")
    @ValueSource(strings = {"", " "})
    void testProductFieldNameBlankEmptyException(String name) {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Product(name, 1000, 10, 10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("상품의 가격이 0원 또는 음수일 경우")
    @ParameterizedTest(name = "상품이름: {0}, 상품가격: {1}, 상품재고: {2}, 프로모션상품재고: {3}")
    @CsvSource(
            value = {"콜라,0,10,10", "사이다,-1000,8,0"}
    )
    void testProductFieldPriceException(String name, int price, int stock, int promotionStock) {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Product(name, price, stock, promotionStock))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("상품의 재고가 음수일 경우")
    @ParameterizedTest(name = "상품이름: {0}, 상품가격: {1}, 상품재고: {2}, 프로모션상품재고: {3}")
    @CsvSource(
            value = {"콜라,0,-10,10", "사이다,-1000,8,-8"}
    )
    void testProductFieldStockException(String name, int price, int stock, int promotionStock) {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Product(name, price, stock, promotionStock))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("구매 수량이 재고 수량을 초과하지 않을 경우")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(10)")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testHasSufficientStock(int quantity) {
        Product product = new Product("콜라", 1000, 10, 0);
        boolean isSufficientStock = product.hasSufficientStock(quantity);
        assertSoftly(softly -> {
            softly.assertThat(isSufficientStock).isTrue();
        });
    }

    @DisplayName("구매 수량이 재고 수량을 초과할 경우")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(10)")
    @ValueSource(ints = {11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    void testHasNotSufficientStock(int quantity) {
        Product product = new Product("콜라", 1000, 10, 0);
        boolean isSufficientStock = product.hasSufficientStock(quantity);
        assertSoftly(softly -> {
            softly.assertThat(isSufficientStock).isFalse();
        });
    }
}