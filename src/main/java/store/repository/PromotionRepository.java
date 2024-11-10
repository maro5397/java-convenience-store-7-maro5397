package store.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import store.common.constant.ErrorMessage;
import store.domain.Promotion;
import store.domain.strategy.PromotionStrategy;
import store.domain.strategy.impl.LocalDateTimePromotionStrategy;

public class PromotionRepository {
    private static final String PROMOTIONS_DELIMITER = ",";
    private static final String DIRECTORY_PROPERTY = "user.dir";

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
            System.err.println(
                    ErrorMessage.FILE_READ_ERROR.getMessage() + System.getProperty(DIRECTORY_PROPERTY)
                            + e.getMessage());
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

    private void setPromotions(String line) {
        String[] values = line.split(PROMOTIONS_DELIMITER);
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
