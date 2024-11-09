package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {
    @Test
    void 파일에_있는_상품_목록_출력() {
        assertSimpleTest(() -> {
            run("[물-1]", "N", "N");
            assertThat(output()).contains(
                    "- 콜라 1,000원 10개 탄산2+1",
                    "- 콜라 1,000원 10개",
                    "- 사이다 1,000원 8개 탄산2+1",
                    "- 사이다 1,000원 7개",
                    "- 오렌지주스 1,800원 9개 MD추천상품",
                    "- 오렌지주스 1,800원 재고 없음",
                    "- 탄산수 1,200원 5개 탄산2+1",
                    "- 탄산수 1,200원 재고 없음",
                    "- 물 500원 10개",
                    "- 비타민워터 1,500원 6개",
                    "- 감자칩 1,500원 5개 반짝할인",
                    "- 감자칩 1,500원 5개",
                    "- 초코바 1,200원 5개 MD추천상품",
                    "- 초코바 1,200원 5개",
                    "- 에너지바 2,000원 5개",
                    "- 정식도시락 6,400원 8개",
                    "- 컵라면 1,700원 1개 MD추천상품",
                    "- 컵라면 1,700원 10개"
            );
        });
    }

    @Test
    void 여러_개의_일반_상품_구매() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void 기간에_해당하지_않는_프로모션_적용() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }

    @Test
    @DisplayName("README에 있는 실행 예시 테스트")
    void testBasedOnRequirementsDocument() {
        assertSimpleTest(() -> {
            run("[콜라-3],[에너지바-5]", "Y", "Y", "[콜라-10]", "Y", "N", "Y", "[오렌지주스-1]", "Y", "Y", "N");
            assertThat(output()).contains(
                    "멤버십 할인을 받으시겠습니까? (Y/N)",
                    "현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)",
                    "현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
            );
        });
    }

    @Test
    @DisplayName("프로모션 할인 거절 테스트")
    void testRefuseNonePromotionDiscount() {
        assertSimpleTest(() -> {
            run("[콜라-4]", "N", "N", "N");
            assertThat(output()).contains(
                    "멤버십 할인을 받으시겠습니까? (Y/N)",
                    "현재 콜라 1개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"
            );
        });
    }

    @Test
    @DisplayName("추가 프로모션 상품 구매 거절 테스트")
    void testRefuseConfirmationFreeAdditionInput() {
        assertSimpleTest(() -> {
            run("[오렌지주스-1]", "Y", "N", "N");
            assertThat(output()).contains(
                    "멤버십 할인을 받으시겠습니까? (Y/N)",
                    "현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
            );
        });
    }

    @ParameterizedTest(name = "입력 주문: {0}")
    @ValueSource(strings = {"[abc-12]", "[테스트-7]", "[솜사탕-5]"})
    @DisplayName("존재하지 않는 상품 입력")
    void testNotExistedProduct(String input) {
        assertSimpleTest(() -> {
            runException(input);
            assertThat(output()).contains("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        });
    }

    @ParameterizedTest(name = "수락 입력(Y/N): {0}")
    @ValueSource(strings = {"yes", "y", "no", "n", "test", "wrong-answer", "answer"})
    @DisplayName("잘못된 수락 입력(Y/N)")
    void testWrongAnswer(String input) {
        assertSimpleTest(() -> {
            runException("[오렌지주스-1]", input);
            assertThat(output()).contains("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
