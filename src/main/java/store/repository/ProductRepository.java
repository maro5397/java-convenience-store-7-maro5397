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
        return products.get(productName);
    }

    private void loadProducts() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                setProducts(line);
            }
        } catch (IOException e) {
            System.err.println("[ERROR] 파일을 읽는 중 오류가 발생했습니다: " + System.getProperty("user.dir") + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("[ERROR] 숫자 형식이 올바르지 않습니다: " + e.getMessage());
        }
    }

    private void setProducts(String line) {
        String[] values = line.split(",");
        String name = values[0];
        int price = Integer.parseInt(values[1]);
        int quantity = Integer.parseInt(values[2]);
        String promotion = values[3];
        Product product = products.get(name);
        if (!promotion.equals("null")) {
            if(product != null) {
                products.put(name, new Product(name, price, product.getStock(), quantity, promotion));
            }
            if(product == null) {
                products.put(name, new Product(name, price, 0, quantity, promotion));
            }
        }
        if (promotion.equals("null")) {
            if(product != null) {
                products.put(name, new Product(name, price, quantity, product.getPromotionStock(), product.getPromotion()));
            }
            if(product == null) {
                products.put(name, new Product(name, price, quantity, 0, promotion));
            }
        }
    }
}
