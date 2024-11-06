package store.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromotionTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Promotion softDrinkPromotion;

    @BeforeEach
    void setUp() {
        softDrinkPromotion = new Promotion(
                "탄산2+1",
                2,
                1,
                LocalDateTime.parse("2024-01-01 00:00", formatter),
                LocalDateTime.parse("2024-12-31 23:59", formatter)
        );
    }

    @DisplayName("현재 날짜가 프로모션 기간 내")
    @ParameterizedTest(name = "프로모션 날짜: {1}")
    @CsvSource(
            value = {
                    "2024-11-06 00:00", "2024-11-06 00:00", "2024-11-06 00:00"
            }
    )
    void testWithinPromotionPeriod(String dateTime) {
        LocalDateTime now = LocalDateTime.parse(dateTime, formatter);
        assertSoftly(softly -> {
            softly.assertThat(softDrinkPromotion.isWithinPromotionPeriod(now))
                    .isTrue();
        });
    }

    @DisplayName("현재 날짜가 프로모션 기간 외")
    @ParameterizedTest(name = "프로모션 날짜: {0}")
    @CsvSource(
            value = {
                    "2023-11-06 00:00", "2025-01-01 00:00",
                    "2023-11-06 00:00", "2025-01-01 00:00",
                    "2023-11-06 00:00", "2025-01-01 00:00"
            }
    )
    void testWithoutPromotionPeriod(String dateTime) {
        LocalDateTime now = LocalDateTime.parse(dateTime, formatter);
        assertSoftly(softly -> {
            softly.assertThat(softDrinkPromotion.isWithinPromotionPeriod(now))
                    .isFalse();
        });
    }
}