package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

class OrdersTest {
    private ProductRepository productRepository;
    private PromotionRepository promotionRepository;
    private Orders orders;

    @BeforeEach
    void setUp() {
        this.productRepository = new ProductRepository("src/main/resources/products.md");
        this.promotionRepository = new PromotionRepository("src/main/resources/promotions.md");
        this.orders = Orders.create();
    }

    void settingOrders(String productName1, int quantity1, String productName2, int quantity2) {
        Product product1 = this.productRepository.getProductWithName(productName1);
        Product product2 = this.productRepository.getProductWithName(productName2);
        this.orders.addOrder(product1, this.promotionRepository.getPromotionWithName(product1.getPromotion()),
                quantity1);
        this.orders.addOrder(product2, this.promotionRepository.getPromotionWithName(product2.getPromotion()),
                quantity2);
    }

    @DisplayName("고객이 구매 요청한 상품을 주문목록에 추가")
    @ParameterizedTest(name = "상품이름1: {0}, 구매수량1: {1}, 상품이름2: {2}, 구매수량2: {3}")
    @CsvSource(
            value = {
                    "콜라,10,사이다,8",
                    "감자칩,5,초코바,5",
                    "컵라면,1,에너지바,3"
            }
    )
    void testAddProductInOrders(String productName1, int quantity1, String productName2, int quantity2) {
        settingOrders(productName1, quantity1, productName2, quantity2);
        Order orderByProductName1 = this.orders.getOrders().getFirst();
        Order orderByProductName2 = this.orders.getOrders().getLast();
        assertSoftly(softly -> {
            softly.assertThat(orderByProductName1.getProduct().getName()).isEqualTo(productName1);
            softly.assertThat(orderByProductName1.getQuantity()).isEqualTo(quantity1);
            softly.assertThat(orderByProductName2.getProduct().getName()).isEqualTo(productName2);
            softly.assertThat(orderByProductName2.getQuantity()).isEqualTo(quantity2);
        });
    }

    @DisplayName("고객이 구매 요청한 상품 총액")
    @ParameterizedTest(name = "상품이름1: {0}, 구매수량1: {1}, 상품이름2: {2}, 구매수량2: {3}, 총액: {4}")
    @CsvSource(
            value = {
                    "컵라면,1,에너지바,3,7700",
                    "콜라,10,사이다,8,18000",
                    "감자칩,5,초코바,5,13500"
            }
    )
    void testTotalPrice(String productName1, int quantity1, String productName2, int quantity2, int total) {
        settingOrders(productName1, quantity1, productName2, quantity2);
        assertSoftly(softly -> {
            softly.assertThat(this.orders.getTotalPrice()).isEqualTo(total);
        });
    }

    @DisplayName("고객이 구매 요청한 상품의 프로모션 할인 총액")
    @ParameterizedTest(name = "상품이름1: {0}, 구매수량1: {1}, 상품이름2: {2}, 구매수량2: {3}, 할인 총액: {4}")
    @CsvSource(
            value = {
                    "콜라,10,사이다,8,5000",
                    "감자칩,5,초코바,5,5400",
                    "컵라면,1,에너지바,3,0"
            }
    )
    void testPromotionDiscount(String productName1, int quantity1, String productName2, int quantity2, int discount) {
        settingOrders(productName1, quantity1, productName2, quantity2);
        assertSoftly(softly -> {
            softly.assertThat(this.orders.getTotalPromotionDiscount()).isEqualTo(discount);
        });
    }

    @DisplayName("고객이 구매 요청한 상품의 맴버쉽 할인 총액")
    @ParameterizedTest(name = "상품이름1: {0}, 구매수량1: {1}, 상품이름2: {2}, 구매수량2: {3}, 할인 총액: {4}")
    @CsvSource(
            value = {
                    "정식도시락,8,콜라,20,8000",
                    "감자칩,5,초코바,5,810",
                    "감자칩,7,초코바,7,2430",
                    "컵라면,1,에너지바,3,2310"
            }
    )
    void testMembershipDiscount(String productName1, int quantity1, String productName2, int quantity2, int discount) {
        settingOrders(productName1, quantity1, productName2, quantity2);
        assertSoftly(softly -> {
            softly.assertThat(this.orders.getMembershipDiscount(true)).isEqualTo(discount);
            softly.assertThat(this.orders.getMembershipDiscount(false)).isZero();
        });
    }
}