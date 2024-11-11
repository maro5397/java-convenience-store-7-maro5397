package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

class OrderTest {
    private Product product;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        ProductRepository productRepository = new ProductRepository("src/main/resources/products.md");
        PromotionRepository promotionRepository = new PromotionRepository("src/main/resources/promotions.md");
        this.product = productRepository.getProductWithName("감자칩");
        this.promotion = promotionRepository.getPromotionWithName(product.getPromotion());
    }

    @DisplayName("구매된 수량만큼 재고를 차감")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(10)")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testDecrementStock(int quantity) {
        Order order = Order.create(this.product, this.promotion, quantity);
        order.consumeStockForOrder();
        assertSoftly(softly -> {
            softly.assertThat(order.getProduct().getStock() + order.getProduct().getPromotionStock())
                    .isEqualTo(10 - quantity);
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
        Order order = Order.create(this.product, this.promotion, quantity);
        order.consumeStockForOrder();
        assertSoftly(softly -> {
            softly.assertThat(order.getProduct().getStock()).isEqualTo(productStock);
            softly.assertThat(order.getProduct().getPromotionStock()).isEqualTo(promotionProductStock);
        });
    }
}