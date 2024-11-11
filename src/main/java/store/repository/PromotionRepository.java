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
    private static final int PROMOTION_NAME_INDEX = 0;
    private static final int PROMOTION_BUY_INDEX = 1;
    private static final int PROMOTION_GET_INDEX = 2;
    private static final int PROMOTION_START_DATE_INDEX = 3;
    private static final int PROMOTION_END_DATE_INDEX = 4;

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
                    ErrorMessage.FILE_READ_ERROR.getMessage() + System.getProperty(DIRECTORY_PROPERTY) + e.getMessage()
            );
        }
    }

    private void skipHeader(BufferedReader br) throws IOException {
        br.readLine();
    }

    private void processLines(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            extractPromotions(line);
        }
    }

    private void extractPromotions(String line) {
        String[] values = line.split(PROMOTIONS_DELIMITER);
        String name = values[PROMOTION_NAME_INDEX];
        int buy = Integer.parseInt(values[PROMOTION_BUY_INDEX]);
        int get = Integer.parseInt(values[PROMOTION_GET_INDEX]);
        String startDate = values[PROMOTION_START_DATE_INDEX];
        String endDate = values[PROMOTION_END_DATE_INDEX];
        PromotionStrategy promotionStrategy = LocalDateTimePromotionStrategy.create(startDate, endDate);
        Promotion promotion = Promotion.create(buy, get, promotionStrategy);
        promotions.put(name, promotion);
    }
}
