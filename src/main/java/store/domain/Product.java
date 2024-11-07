package store.domain;

public class Product {
    private final String name;
    private final int price;
    private int stock;
    private final PromotionProduct promotionProduct;

    public Product(String name, int price, int stock, PromotionProduct promotionProduct) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotionProduct = promotionProduct;
        validate();
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

    public PromotionProduct getPromotionProduct() {
        return promotionProduct;
    }

    public PromotionResult decrementStock(int quantity) {
        if (this.promotionProduct == null) {
            hasSufficientStock(quantity);
            stock -= quantity;
            return new PromotionResult(0, 0, quantity);
        }
        hasSufficientStock(quantity);
        PromotionResult promotionResult = this.promotionProduct.decrementStock(quantity);
        stock -= promotionResult.getNoneDiscountItemCount();
        return promotionResult;
    }

    private void validate() {
        validateName();
        validatePrice();
        validateStock();
        validateStock();
    }

    private void validateName() {
        if (this.name == null) {
            throw new IllegalArgumentException("[ERROR] 상품 이름이 없습니다.");
        }
        if (this.name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 공백 문자열이 될 수 없습니다.");
        }
        if (this.name.length() > 100) {
            throw new IllegalArgumentException("[ERROR] 상품 이름은 100자 이하여야 합니다.");
        }
    }

    private void validatePrice() {
        if (this.price <= 0) {
            throw new IllegalArgumentException("[ERROR] 가격은 음수나 0이 될 수 없습니다.");
        }
    }

    private void validateStock() {
        if (this.stock < 0) {
            throw new IllegalArgumentException("[ERROR] 재고는 음수가 될 수 없습니다.");
        }
    }

    private void hasSufficientStock(int quantity) {
        if (this.promotionProduct == null && stock < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량이 충분하지 않습니다.");
        }
        if (this.promotionProduct != null && stock + this.promotionProduct.getStock() < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량이 충분하지 않습니다.");
        }
    }
}
