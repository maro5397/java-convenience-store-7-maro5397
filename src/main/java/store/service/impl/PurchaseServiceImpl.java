package store.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.Order;
import store.domain.Orders;
import store.domain.Product;
import store.domain.Promotion;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.PurchaseService;

public class PurchaseServiceImpl implements PurchaseService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public PurchaseServiceImpl(ProductRepository productRepository, PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    @Override
    public List<Product> getStock() {
        return productRepository.getProducts();
    }

    @Override
    public Orders makeOrders(String orderInput) {
        OrderInputToOrdersUtil ordersUtil = new OrderInputToOrdersUtil();
        Orders orders = new Orders();
        for (String order : orderInput.split(",")) {
            ordersUtil.getOrders(orders, order);
        }
        return orders;
    }

    @Override
    public void applyAdditionalPromotionProduct(Order order) {
        order.applyAdditionalPromotion();
    }

    @Override
    public void deleteNonePromotionProduct(Order order) {
        order.deleteNonePromotionAppliedProductCount();
    }

    private class OrderInputToOrdersUtil {
        private final String regex = "(?<![\\[\\{\\(\\w])\\[(?<product>[가-힣a-zA-Z0-9]+)-(?<quantity>\\d+)\\](?![\\]\\}\\)\\w])";
        private final Pattern pattern = Pattern.compile(regex);

        public void getOrders(Orders orders, String order) {
            Matcher matcher = validateOrderFormat(order);
            String productName = matcher.group("product");
            Product product = getProduct(productName);
            Promotion promotion = getPromotion(product);
            int quantity = parseQuantity(matcher.group("quantity"));
            orders.addOrder(product, promotion, quantity);
        }

        private Matcher validateOrderFormat(String order) {
            Matcher matcher = pattern.matcher(order);
            if (!matcher.find()) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
            return matcher;
        }

        private Product getProduct(String productName) {
            return productRepository.getProductWithName(productName);
        }

        private Promotion getPromotion(Product product) {
            return promotionRepository.getPromotionWithName(product.getPromotion());
        }

        private int parseQuantity(String quantityStr) {
            return Integer.parseInt(quantityStr);
        }
    }
}
