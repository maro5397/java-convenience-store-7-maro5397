package store.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.common.constant.ErrorMessage;
import store.domain.Product;

public class ProductRepository {
    private static final int BASE_NUMBER_OF_STOCK = 0;
    private static final String PRODUCTS_DELIMITER = ",";
    private static final String NULL_PROMOTION = "null";
    private static final String DIRECTORY_PROPERTY = "user.dir";
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int PRODUCT_PRICE_INDEX = 1;
    private static final int PRODUCT_QUANTITY_INDEX = 2;
    private static final int PRODUCT_PROMOTION_INDEX = 3;

    private final Map<String, Product> products = new LinkedHashMap<>();
    private final String filePath;

    public ProductRepository(String filePath) {
        this.filePath = filePath;
        loadProducts();
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products.values());
    }

    public Product getProductWithName(String productName) {
        if (products.get(productName) == null) {
            throw new IllegalStateException(ErrorMessage.PRODUCT_NOT_FOUND.getMessage());
        }
        return products.get(productName);
    }

    private void loadProducts() {
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
            setProducts(line);
        }
    }

    private void setProducts(String line) {
        String[] values = line.split(PRODUCTS_DELIMITER);
        String name = values[PRODUCT_NAME_INDEX];
        int price = Integer.parseInt(values[PRODUCT_PRICE_INDEX]);
        int quantity = Integer.parseInt(values[PRODUCT_QUANTITY_INDEX]);
        String promotion = values[PRODUCT_PROMOTION_INDEX];
        Product product = products.get(name);
        addProduct(name, price, quantity, promotion, product);
    }

    private void addProduct(String name, int price, int quantity, String promotion, Product product) {
        if (promotion.equals(NULL_PROMOTION)) {
            addProductWithoutPromotion(name, price, quantity, product);
            return;
        }
        addProductWithPromotion(name, price, quantity, promotion, product);
    }

    private void addProductWithPromotion(String name, int price, int quantity, String promotion, Product product) {
        if (product != null) {
            products.put(name, Product.create(name, price, product.getStock(), quantity, promotion));
            return;
        }
        products.put(name, Product.create(name, price, BASE_NUMBER_OF_STOCK, quantity, promotion));
    }

    private void addProductWithoutPromotion(String name, int price, int quantity, Product product) {
        if (product != null) {
            products.put(
                    name,
                    Product.create(name, price, quantity, product.getPromotionStock(), product.getPromotion())
            );
            return;
        }
        products.put(name, Product.create(name, price, quantity, BASE_NUMBER_OF_STOCK, NULL_PROMOTION));
    }
}
