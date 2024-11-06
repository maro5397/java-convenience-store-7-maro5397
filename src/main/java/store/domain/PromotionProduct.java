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
        int paidItemCount = promotionResult.getPaidItemCount();
        int noneDiscountItemCount = promotionResult.getNoneDiscountItemCount();
        stock -= promotionResult.getFreeItemCount() + promotionResult.getPaidItemCount();
        for(int i = 0; i < promotionResult.getNoneDiscountItemCount() && stock != 0; i++) {
            stock -= 1;
            paidItemCount += 1;
            noneDiscountItemCount -= 1;
        }
        return new PromotionResult(promotionResult.getFreeItemCount(), paidItemCount, noneDiscountItemCount);
    }
}
