package store.domain;

import store.common.constant.ErrorMessage;

public class Product {
    private static final String BASE_STRING_OF_PROMOTION = "";
    private static final String NULL_PROMOTION = "null";
    private static final int MAX_PRODUCT_NAME = 100;

    private final String name;
    private final int price;
    private int stock;
    private int promotionStock;
    private String promotion;

    private Product(String name, int price, int stock, int promotionStock, String promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotionStock = promotionStock;
        this.promotion = promotion;
        if (promotion.equals(NULL_PROMOTION)) {
            this.promotion = BASE_STRING_OF_PROMOTION;
        }
        validate();
    }

    public static Product create(String name, int price, int stock, int promotionStock, String promotion) {
        return new Product(name, price, stock, promotionStock, promotion);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public String getPromotion() {
        return promotion;
    }

    public void decrementStock(int quantity) {
        hasSufficientStock(quantity);
        stock -= quantity;
    }

    public void decrementPromotionStock(int quantity) {
        hasSufficientPromotionStock(quantity);
        promotionStock -= quantity;
    }

    private void validate() {
        validateName();
        validatePrice();
        validateStock();
    }

    private void validateName() {
        if (this.name == null) {
            throw new IllegalArgumentException(ErrorMessage.PRODUCT_NAME_MISSING.getMessage());
        }
        if (this.name.isBlank()) {
            throw new IllegalArgumentException(ErrorMessage.NAME_CANNOT_BE_EMPTY.getMessage());
        }
        if (this.name.length() > MAX_PRODUCT_NAME) {
            throw new IllegalArgumentException(ErrorMessage.PRODUCT_NAME_TOO_LONG.getMessage());
        }
    }

    private void validatePrice() {
        if (this.price <= 0) {
            throw new IllegalArgumentException(ErrorMessage.PRICE_CANNOT_BE_NEGATIVE.getMessage());
        }
    }

    private void validateStock() {
        if (this.stock < 0 || this.promotionStock < 0) {
            throw new IllegalArgumentException(ErrorMessage.STOCK_CANNOT_BE_NEGATIVE.getMessage());
        }
    }

    private void hasSufficientStock(int quantity) {
        if (stock < quantity) {
            throw new IllegalArgumentException(ErrorMessage.INSUFFICIENT_STOCK.getMessage());
        }
    }

    private void hasSufficientPromotionStock(int quantity) {
        if (promotionStock < quantity) {
            throw new IllegalArgumentException(ErrorMessage.INSUFFICIENT_STOCK.getMessage());
        }
    }
}
