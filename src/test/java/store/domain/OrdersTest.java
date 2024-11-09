package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.rmi.NoSuchObjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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
        this.orders = new Orders();
    }

    void settingOrders(String productName1, int quantity1, String productName2, int quantity2)
            throws NoSuchObjectException {
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
    void testAddProductInOrders(String productName1, int quantity1, String productName2, int quantity2)
            throws NoSuchObjectException {
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
    void testTotalPrice(String productName1, int quantity1, String productName2, int quantity2, int total)
            throws NoSuchObjectException {
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
    void testPromotionDiscount(String productName1, int quantity1, String productName2, int quantity2, int discount)
            throws NoSuchObjectException {
        settingOrders(productName1, quantity1, productName2, quantity2);
        assertSoftly(softly -> {
            softly.assertThat(this.orders.getPromotionDiscount()).isEqualTo(discount);
        });
    }

    @DisplayName("고객이 구매 요청한 상품의 맴버쉽 할인 총액")
    @ParameterizedTest(name = "상품이름1: {0}, 구매수량1: {1}, 상품이름2: {2}, 구매수량2: {3}, 할인 총액: {4}")
    @CsvSource(
            value = {
                    "정식도시락,8,콜라,20,8000",
                    "감자칩,5,초코바,5,810",
                    "컵라면,1,에너지바,3,2310"
            }
    )
    void testMembershipDiscount(String productName1, int quantity1, String productName2, int quantity2, int discount)
            throws NoSuchObjectException {
        settingOrders(productName1, quantity1, productName2, quantity2);
        assertSoftly(softly -> {
            softly.assertThat(this.orders.getMembershipDiscount(true)).isEqualTo(discount);
            softly.assertThat(this.orders.getMembershipDiscount(false)).isZero();
        });
    }

    @DisplayName("구매된 수량만큼 재고를 차감")
    @ParameterizedTest(name = "구매 수량({0}), 재고 수량(10)")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void testDecrementStock(int quantity) throws NoSuchObjectException {
        Product product = this.productRepository.getProductWithName("감자칩");
        this.orders.addOrder(product, this.promotionRepository.getPromotionWithName(product.getPromotion()),
                quantity);
        Order order = this.orders.getOrders().getFirst();
        order.applyConsumeStock();
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
    void testDecrementPromotionStock(int quantity, int productStock, int promotionProductStock)
            throws NoSuchObjectException {
        Product product = this.productRepository.getProductWithName("감자칩");
        this.orders.addOrder(product, this.promotionRepository.getPromotionWithName(product.getPromotion()),
                quantity);
        Order order = this.orders.getOrders().getFirst();
        order.applyConsumeStock();
        assertSoftly(softly -> {
            softly.assertThat(order.getProduct().getStock()).isEqualTo(productStock);
            softly.assertThat(order.getProduct().getPromotionStock()).isEqualTo(promotionProductStock);
        });
    }
}