package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.strategy.impl.LocalDateTimePromotionStrategy;

class ProductTest {
    private final Map<String, Promotion> promotions = new HashMap<>();
    private PromotionProduct mockPromotionProduct;

    @BeforeEach
    void setUp() {
        promotions.put("탄산2+1",
                new Promotion("탄산2+1", 2, 1, new LocalDateTimePromotionStrategy("2024-01-01", "2024-12-31")));
        promotions.put("MD추천상품",
                new Promotion("MD추천상품", 1, 1, new LocalDateTimePromotionStrategy("2024-01-01", "2024-12-31")));
        promotions.put("반짝할인",
                new Promotion("반짝할인", 1, 1, new LocalDateTimePromotionStrategy("2024-11-01", "2024-11-30")));
        mockPromotionProduct = new PromotionProduct(
                "mockPromotionProductName",
                1000,
                5,
                this.promotions.get("탄산2+1")
        );
    }

    @DisplayName("상품의 기본 정보 관리")
    @ParameterizedTest(name = "상품이름: {0}, 상품가격: {1}, 상품재고: {2}, 프로모션상품재고: {3}")
    @CsvSource(
            value = {
                    "콜라,1000,10,탄산2+1,콜라,1000,10",
                    "사이다,1000,8,탄산2+1,사이다,1000,7",
                    "감자칩,1500,5,반짝할인,감자칩,1500,5",
                    "초코바,1200,5,MD추천상품,초코바,1200,5",
                    "컵라면,1700,1,MD추천상품,컵라면,1700,10"
            }
    )
    void testProductFieldManage(String promotionProductName, int promotionProductPrice, int promotionProductStock,
                                String promotionType, String productName, int productPrice, int productStock) {
        PromotionProduct promotionProduct = new PromotionProduct(
                promotionProductName,
                promotionProductPrice,
                promotionProductStock,
                this.promotions.get(promotionType)
        );
        Product product = new Product(productName, productPrice, productStock, promotionProduct);
        assertSoftly(softly -> {
            softly.assertThat(product.getName())
                    .isEqualTo(productName);
            softly.assertThat(product.getPrice())
                    .isEqualTo(productPrice);
            softly.assertThat(product.getStock())
                    .isEqualTo(productStock);
            softly.assertThat(product.getPromotionProduct())
                    .isEqualTo(promotionProduct);
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
            softly.assertThatThrownBy(() -> new Product(name, price, stock, mockPromotionProduct))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("상품의 이름이 공백, 빈 문자열일 경우")
    @ParameterizedTest(name = "상품이름: {0}")
    @ValueSource(strings = {"", " "})
    void testProductFieldNameBlankEmptyException(String name) {
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> new Product(name, 1000, 10, mockPromotionProduct))
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
            softly.assertThatThrownBy(() -> new Product(name, 1000, 10, mockPromotionProduct))
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
            softly.assertThatThrownBy(() -> new Product(name, price, stock, mockPromotionProduct))
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
            softly.assertThatThrownBy(() -> new Product(name, price, stock, mockPromotionProduct))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("구매 수량이 재고 수량을 초과하지 않을 경우")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(10)")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testHasSufficientStock(int quantity) {
        Product product = new Product("콜라", 1000, 5, mockPromotionProduct);
        assertSoftly(softly -> {
            softly.assertThatCode(() -> {
                product.decrementStock(quantity);
            }).doesNotThrowAnyException();
        });
    }

    @DisplayName("구매 수량이 재고 수량을 초과할 경우")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(10)")
    @ValueSource(ints = {11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    void testHasNotSufficientStock(int quantity) {
        Product product = new Product("콜라", 1000, 5, mockPromotionProduct);
        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> product.decrementStock(quantity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("[ERROR]");
        });
    }

    @DisplayName("구매된 수량만큼 재고를 차감")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(10)")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testDecrementStock(int quantity) {
        Product product = new Product("콜라", 1000, 5, mockPromotionProduct);
        product.decrementStock(quantity);
        assertSoftly(softly -> {
            softly.assertThat(product.getStock() + product.getPromotionProduct().getStock()).isEqualTo(10 - quantity);
        });
    }

    @DisplayName("프로모션용 재고를 우선적으로 차감")
    @ParameterizedTest(name = "구매 수량({0}), 일반 재고 수량({1}), 프로모션 재고 수량({2})")
    @CsvSource(
            value = {
                    "1,5,4", "2,5,3", "3,5,2", "4,5,1", "5,5,0", "6,4,0", "7,3,0", "8,2,0", "9,1,0", "10,0,0"
            }
    )
    void testDecrementPromotionStock(int quantity, int productStock, int promotionProductStock) {
        Product product = new Product("콜라", 1000, 5, mockPromotionProduct);
        product.decrementStock(quantity);
        assertSoftly(softly -> {
            softly.assertThat(product.getStock()).isEqualTo(productStock);
            softly.assertThat(product.getPromotionProduct().getStock()).isEqualTo(promotionProductStock);
        });
    }
}