package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductTest {
    @ParameterizedTest
    @DisplayName("상품의 기본 정보 관리")
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
}