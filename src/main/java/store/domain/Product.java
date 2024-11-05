package store.domain;

import java.math.BigInteger;

public class Product {
    private final String name;
    private final BigInteger price;
    private final BigInteger stock;
    private final BigInteger promotionStock;

    public Product(String name, int price, int stock, int promotionStock) {
        this.name = name;
        this.price = BigInteger.valueOf(price);
        this.stock = BigInteger.valueOf(stock);
        this.promotionStock = BigInteger.valueOf(promotionStock);
        validate(this);
    }

    public String getName() {
        return name;
    }

    public BigInteger getPrice() {
        return price;
    }

    public BigInteger getStock() {
        return stock;
    }

    public BigInteger getPromotionStock() {
        return promotionStock;
    }

    public void hasSufficientStock(int quantity) {
        BigInteger totalStock = stock.add(promotionStock);
        BigInteger quantityBigInteger = BigInteger.valueOf(quantity);
        if(totalStock.compareTo(quantityBigInteger) < 0) {
            throw new IllegalArgumentException("[ERROR] 재고 수량이 충분하지 않습니다.");
        }
    }

    private void validate(Product product) {
        validateName(product.getName());
        validatePrice(product.getPrice());
        validateStock(product.getStock());
        validateStock(product.getPromotionStock());
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상품 이름이 없습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 공백 문자열이 될 수 없습니다.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("[ERROR] 상품 이름은 100자 이하여야 합니다.");
        }
    }

    private void validatePrice(BigInteger price) {
        if (price.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("[ERROR] 가격은 음수나 0이 될 수 없습니다.");
        }
    }

    private void validateStock(BigInteger stock) {
        if (stock.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 재고는 음수가 될 수 없습니다.");
        }
    }
}
