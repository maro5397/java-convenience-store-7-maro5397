package store.service.impl;

import java.rmi.NoSuchObjectException;
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
    public Orders makeOrders(String orderInput) throws NoSuchObjectException {
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
        private final String regex = "\\[(?<product>[가-힣a-zA-Z0-9]+)-(?<quantity>\\d+)\\]";
        private final Pattern pattern = Pattern.compile(regex);

        public void getOrders(Orders orders, String order) throws NoSuchObjectException {
            Matcher matcher = pattern.matcher(order);
            if (!matcher.find()) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
            String productName = matcher.group("product");
            Product product = productRepository.getProductWithName(productName);
            Promotion promotion = promotionRepository.getPromotionWithName(product.getPromotion());
            int quantity = Integer.parseInt(matcher.group("quantity"));
            orders.addOrder(product, promotion, quantity);
        }
    }
}
