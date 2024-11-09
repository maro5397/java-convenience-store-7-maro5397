package store.repository;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RepositoryTest {
    private ByteArrayOutputStream errorStream;

    @BeforeEach
    void setUp() {
        errorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorStream));
    }

    @AfterEach
    void tearDown() {
        System.setErr(System.err);
    }

    @Test
    @DisplayName("틀린 파일 경로 입력 - ProductRepository")
    void testWrongFilePathWithProductRepository() {
        new ProductRepository("wrong-path");
        assertSoftly(softly -> {
            softly.assertThat(errorStream.toString().trim()).contains("[ERROR] 파일을 읽는 중 오류가 발생했습니다");
        });
    }

    @Test
    @DisplayName("틀린 파일 경로 입력 - PromotionRepository")
    void testWrongFilePathWithPromotionRepository() {
        new PromotionRepository("wrong-path");
        assertSoftly(softly -> {
            softly.assertThat(errorStream.toString().trim()).contains("[ERROR] 파일을 읽는 중 오류가 발생했습니다");
        });
    }
}