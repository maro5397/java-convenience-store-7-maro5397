package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.strategy.impl.LocalDateTimePromotionStrategy;

class OrdersTest {
    private final Map<String, Promotion> promotions = new HashMap<>();
    private final Map<String, Product> products = new HashMap<>();

    @BeforeEach
    void setUp() {
        promotions.put("탄산2+1",
                new Promotion("탄산2+1", 2, 1, new LocalDateTimePromotionStrategy("2024-01-01", "2024-12-31")));
        promotions.put("MD추천상품",
                new Promotion("MD추천상품", 1, 1, new LocalDateTimePromotionStrategy("2024-01-01", "2024-12-31")));
        promotions.put("반짝할인",
                new Promotion("반짝할인", 1, 1, new LocalDateTimePromotionStrategy("2024-11-01", "2024-11-30")));

        products.put("콜라",
                new Product("콜라", 1000, 10, new PromotionProduct("콜라", 1000, 10, promotions.get("탄산2+1"))));

        products.put("사이다",
                new Product("사이다", 1000, 8, new PromotionProduct("사이다", 1000, 8, promotions.get("탄산2+1"))));

        products.put("오렌지주스",
                new Product("오렌지주스", 1800, 0, new PromotionProduct("오렌지주스", 1800, 9, promotions.get("MD추천상품"))));

        products.put("탄산수",
                new Product("탄산수", 1200, 0, new PromotionProduct("탄산수", 1200, 5, promotions.get("탄산2+1"))));

        products.put("물",
                new Product("물", 500, 10, null));

        products.put("비타민워터",
                new Product("비타민워터", 1500, 6, null));

        products.put("감자칩",
                new Product("감자칩", 1500, 5, new PromotionProduct("감자칩", 1500, 5, promotions.get("반짝할인"))));

        products.put("초코바",
                new Product("초코바", 1200, 5, new PromotionProduct("초코바", 1200, 5, promotions.get("MD추천상품"))));

        products.put("에너지바",
                new Product("에너지바", 2000, 5, null));

        products.put("정식도시락",
                new Product("정식도시락", 6400, 8, null));

        products.put("컵라면",
                new Product("컵라면", 1700, 10, new PromotionProduct("컵라면", 1700, 1, promotions.get("MD추천상품"))));
    }


    @DisplayName("고객이 구매 요청한 상품을 목록에 추가")
    @ParameterizedTest(name = "상품이름: {0}, 구매수량: {1}, 상품이름: {2}, 구매수량: {3}")
    @CsvSource(
            value = {
                    "콜라,10,사이다,8",
                    "감자칩,5,초코바,5",
                    "컵라면,1,에너지바,3"
            }
    )
    void testProductFieldManage(String productName1, int quantity1, String productName2, int quantity2) {
        Orders orders = new Orders();
        orders.addOrder(products.get(productName1), quantity1);
        orders.addOrder(products.get(productName2), quantity2);

        Order orderByProductName1 = orders.getOrderByProductName(productName1);
        Order orderByProductName2 = orders.getOrderByProductName(productName2);
        assertSoftly(softly -> {
            softly.assertThat(orderByProductName1.getProduct().getName()).isEqualTo(productName1);
            softly.assertThat(orderByProductName1.getQuantity()).isEqualTo(quantity1);
            softly.assertThat(orderByProductName2.getProduct().getName()).isEqualTo(productName2);
            softly.assertThat(orderByProductName2.getQuantity()).isEqualTo(quantity2);
        });
    }

    @DisplayName("고객이 구매 요청한 상품 총액")
    @ParameterizedTest(name = "상품이름: {0}, 구매수량: {1}, 상품이름: {2}, 구매수량: {3}, 총액: {4}")
    @CsvSource(
            value = {
                    "컵라면,1,에너지바,3,7700",
                    "콜라,10,사이다,8,18000",
                    "감자칩,5,초코바,5,13500"
            }
    )
    void testTotalPrice(String productName1, int quantity1, String productName2, int quantity2, int total) {
        Orders orders = new Orders();
        orders.addOrder(products.get(productName1), quantity1);
        orders.addOrder(products.get(productName2), quantity2);
        assertSoftly(softly -> {
            softly.assertThat(orders.getTotalPrice()).isEqualTo(total);
        });
    }

    @DisplayName("고객이 구매 요청한 상품의 프로모션 할인 총액")
    @ParameterizedTest(name = "상품이름: {0}, 구매수량: {1}, 상품이름: {2}, 구매수량: {3}, 할인 총액: {4}")
    @CsvSource(
            value = {
                    "콜라,10,사이다,8,5000",
                    "감자칩,5,초코바,5,5400",
                    "컵라면,1,에너지바,3,0"
            }
    )
    void testPromotionDiscount(String productName1, int quantity1, String productName2, int quantity2, int discount) {
        Orders orders = new Orders();
        orders.addOrder(products.get(productName1), quantity1);
        orders.addOrder(products.get(productName2), quantity2);
        assertSoftly(softly -> {
            softly.assertThat(orders.getPromotionDiscount()).isEqualTo(discount);
        });
    }

    @DisplayName("고객이 구매 요청한 상품의 맴버쉽 할인 총액")
    @ParameterizedTest(name = "상품이름: {0}, 구매수량: {1}, 상품이름: {2}, 구매수량: {3}, 할인 총액: {4}")
    @CsvSource(
            value = {
                    "정식도시락,8,콜라,20,8000",
                    "감자칩,5,초코바,5,810",
                    "컵라면,1,에너지바,3,2310"
            }
    )
    void testMembershipDiscount(String productName1, int quantity1, String productName2, int quantity2, int discount) {
        Orders orders = new Orders();
        orders.addOrder(products.get(productName1), quantity1);
        orders.addOrder(products.get(productName2), quantity2);
        assertSoftly(softly -> {
            softly.assertThat(orders.getMembershipDiscount()).isEqualTo(discount);
        });
    }
}