package store.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import store.domain.Promotion;
import store.domain.strategy.PromotionStrategy;
import store.domain.strategy.impl.LocalDateTimePromotionStrategy;

public class PromotionRepository {
    private final Map<String, Promotion> promotions = new HashMap<>();
    private final String filePath;

    public PromotionRepository(String filePath) {
        this.filePath = filePath;
        loadPromotions();
    }

    public Promotion getPromotionWithName(String promotionName) {
        return promotions.get(promotionName);
    }

    private void loadPromotions() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            skipHeader(br);
            processLines(br);
        } catch (IOException e) {
            handleIOException(e);
        } catch (NumberFormatException e) {
            handleNumberFormatException(e);
        }
    }

    private void skipHeader(BufferedReader br) throws IOException {
        br.readLine();
    }

    private void processLines(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            setPromotions(line);
        }
    }

    private void handleIOException(IOException e) {
        System.err.println("[ERROR] 파일을 읽는 중 오류가 발생했습니다: " + System.getProperty("user.dir") + e.getMessage());
    }

    private void handleNumberFormatException(NumberFormatException e) {
        System.err.println("[ERROR] 숫자 형식이 올바르지 않습니다: " + e.getMessage());
    }

    private void setPromotions(String line) {
        String[] values = line.split(",");
        String name = values[0];
        int buy = Integer.parseInt(values[1]);
        int get = Integer.parseInt(values[2]);
        String startDate = values[3];
        String endDate = values[4];
        PromotionStrategy promotionStrategy = new LocalDateTimePromotionStrategy(startDate, endDate);
        Promotion promotion = new Promotion(buy, get, promotionStrategy);
        promotions.put(name, promotion);
    }
}
