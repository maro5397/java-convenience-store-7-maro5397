package store.domain;

public class PromotionProduct {
    private final String name;
    private final int price;
    private int stock;
    private final Promotion promotion;

    public PromotionProduct(String name, int price, int stock, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public PromotionResult decrementStock(int quantity) {
        PromotionResult promotionResult = promotion.calculatePromotionDiscount(stock, quantity);
        int productConsumeCount = promotionResult.getProductConsumeCount();
        int promotionProductConsumeCount = promotionResult.getPromotionProductConsumeCount();
        for (int i = 0; i < promotionResult.getProductConsumeCount() && stock > promotionProductConsumeCount; i++) {
            promotionProductConsumeCount += 1;
            productConsumeCount -= 1;
        }
        stock -= promotionProductConsumeCount;
        return new PromotionResult(promotionResult.getFreeItemCount(), promotionResult.getPaidItemCount(),
                promotionProductConsumeCount, productConsumeCount);
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
