package store.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.Order;
import store.domain.Orders;
import store.domain.Product;
import store.domain.OrderResult;
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
        return ordersUtil.getOrders(orderInput);
    }

    @Override
    public OrderResult applyPromotionProduct(Order order) {
        return order.consumePromotionProduct();
    }

    @Override
    public Order addApplyPromotionProduct(String productName, int additionalQuantity) {
        return null;
    }

    @Override
    public void deleteNonePromotionProduct(Order order) {
        return;
    }

    @Override
    public int calculateMembershipDiscount(OrderResult promotionResult) {
        return 0;
    }

    private class OrderInputToOrdersUtil {
        private final String regex = "\\[(?<product>[가-힣a-zA-Z0-9]+)-(?<quantity>\\d+)\\]";
        private final Pattern pattern = Pattern.compile(regex);

        public Orders getOrders(String input) {
            Matcher matcher = pattern.matcher(input);
            Orders orders = new Orders();
            while (matcher.find()) {
                String productName = matcher.group("product");
                Product product = productRepository.getProductWithName(productName);
                Promotion promotion = promotionRepository.getPromotionWithName(product.getPromotion());
                int quantity = Integer.parseInt(matcher.group("quantity"));
                orders.addOrder(product, promotion, quantity);
            }
            return orders;
        }
    }
}
