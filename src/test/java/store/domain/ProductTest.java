package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.repository.PromotionRepository;

class ProductTest {
    private PromotionRepository promotionRepository;

    @BeforeEach
    void setUp() {
        promotionRepository = new PromotionRepository("src/main/resources/promotions.md");
    }

    @DisplayName("상품의 기본 정보 관리")
    @ParameterizedTest(name = "상품이름: {0}, 상품가격: {1}, 상품재고: {2}, 프로모션상품재고: {3}, 프로모션이름: {4}")
    @CsvSource(
            value = {
                    "콜라,1000,10,10,탄산2+1",
                    "사이다,1000,8,7,탄산2+1",
                    "감자칩,1500,5,5,반짝할인",
                    "초코바,1200,5,5,MD추천상품",
                    "컵라면,1700,1,10,MD추천상품"
            }
    )
    void testProductFieldManage(String productName, int productPrice, int productStock, int promotionProductStock,
                                String promotionType) {
        Product product = new Product(productName, productPrice, productStock, promotionProductStock, promotionType);
        assertSoftly(softly -> {
            softly.assertThat(product.getName())
                    .isEqualTo(productName);
            softly.assertThat(product.getPrice())
                    .isEqualTo(productPrice);
            softly.assertThat(product.getStock())
                    .isEqualTo(productStock);
            softly.assertThat(product.getPromotion())
                    .isEqualTo(promotionType);
        });
    }

    @DisplayName("상품의 이름이 null일 경우")
    @ParameterizedTest(name = "상품이름: {0}, 상품가격: {1}, 상품재고: {2}, 프로모션상품재고: {3}")
    @CsvSource(
            value = {"null,1000,8"},
            nullValues = "null"
    )
    void testProductFieldNameNullException(String name, int price, int stock) {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Product(name, price, stock, 0, ""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("상품의 이름이 공백, 빈 문자열일 경우")
    @ParameterizedTest(name = "상품이름: {0}")
    @ValueSource(strings = {"", " "})
    void testProductFieldNameBlankEmptyException(String name) {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Product(name, 1000, 10, 0, ""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("상품의 이름이 100자를 초과하는 문자열일 경우")
    @ParameterizedTest(name = "상품이름이 100자를 초과함")
    @ValueSource(strings = {
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void testProductFieldNameOver100Exception(String name) {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Product(name, 1000, 10, 0, ""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("상품의 가격이 0원 또는 음수일 경우")
    @ParameterizedTest(name = "상품이름: {0}, 상품가격: {1}, 상품재고: {2}, 프로모션상품재고: {3}")
    @CsvSource(
            value = {"콜라,0,10", "사이다,-1000,8"}
    )
    void testProductFieldPriceException(String name, int price, int stock) {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Product(name, price, stock, 0, ""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("상품의 재고가 음수일 경우")
    @ParameterizedTest(name = "상품이름: {0}, 상품가격: {1}, 상품재고: {2}, 프로모션상품재고: {3}")
    @CsvSource(
            value = {"콜라,0,-10", "사이다,1000,-8"}
    )
    void testProductFieldStockException(String name, int price, int stock) {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Product(name, price, stock, 0, ""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("구매 수량이 재고 수량을 초과하지 않을 경우")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(5)")
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void testHasSufficientStock(int quantity) {
        Product product = new Product("콜라", 1000, 5, 0, "");
        assertSoftly(softly -> {
            softly.assertThatCode(() -> {
                product.decrementStock(quantity);
            }).doesNotThrowAnyException();
        });
    }

    @DisplayName("구매 수량이 재고 수량을 초과할 경우")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(5)")
    @ValueSource(ints = {11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    void testHasNotSufficientStock(int quantity) {
        Product product = new Product("콜라", 1000, 5, 0, "");
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> product.decrementStock(quantity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }
}