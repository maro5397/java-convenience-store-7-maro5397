package store.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.domain.Product;

public class ProductRepository {
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
        Product product = products.get(productName);
        if (product == null) {
            throw new IllegalStateException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
        return products.get(productName);
    }

    private void loadProducts() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            skipHeader(br);
            processLines(br);
        } catch (IOException e) {
            System.err.println("[ERROR] 파일을 읽는 중 오류가 발생했습니다: " + System.getProperty("user.dir") + e.getMessage());
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
        String[] values = line.split(",");
        String name = values[0];
        int price = Integer.parseInt(values[1]);
        int quantity = Integer.parseInt(values[2]);
        String promotion = values[3];
        Product product = products.get(name);
        addProduct(name, price, quantity, promotion, product);
    }

    private void addProduct(String name, int price, int quantity, String promotion, Product product) {
        if (promotion.equals("null")) {
            addProductWithoutPromotion(name, price, quantity, product);
        } else {
            addProductWithPromotion(name, price, quantity, promotion, product);
        }
    }

    private void addProductWithPromotion(String name, int price, int quantity, String promotion, Product product) {
        if (product != null) {
            products.put(name, new Product(name, price, product.getStock(), quantity, promotion));
        } else {
            products.put(name, new Product(name, price, 0, quantity, promotion));
        }
    }

    private void addProductWithoutPromotion(String name, int price, int quantity, Product product) {
        if (product != null) {
            products.put(name, new Product(name, price, quantity, product.getPromotionStock(), product.getPromotion()));
        } else {
            products.put(name, new Product(name, price, quantity, 0, "null"));
        }
    }
}
