package store.service.impl;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.common.constant.ErrorMessage;
import store.domain.Order;
import store.domain.Orders;
import store.domain.Product;
import store.domain.Promotion;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.PurchaseService;
import store.view.InputView;

public class PurchaseServiceImpl implements PurchaseService {
    private static final String MATCHER_PRODUCT_KEYWORD = "product";
    private static final String MATCHER_QUANTITY_KEYWORD = "quantity";
    private static final String REGULAR_EXPRESSION = "(?<![\\[\\{\\(\\w])\\[(?<" + MATCHER_PRODUCT_KEYWORD
            + ">[가-힣a-zA-Z0-9]+)-(?<" + MATCHER_QUANTITY_KEYWORD + ">\\d+)\\](?![\\]\\}\\)\\w])";
    private static final String ORDER_DELIMITER = ",";

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
        Orders orders = Orders.create();
        for (String order : orderInput.split(ORDER_DELIMITER)) {
            ordersUtil.getOrders(orders, order);
        }
        return orders;
    }

    @Override
    public boolean processAdditionalPromotionDiscount(Orders orders, InputView inputView) {
        return processDiscount(orders,
                Order::getCanApplyAdditionalPromotion,
                inputView::getConfirmationFreeAdditionInput,
                Order::applyAdditionalPromotion
        );
    }

    @Override
    public boolean processNonePromotionProductDelete(Orders orders, InputView inputView) {
        return processDiscount(orders,
                order -> order.getPromotion() != null
                        && order.getOrderResult().getNoneDiscountPromotionStockCount() != 0,
                inputView::getConfirmationNonePromotionInput,
                Order::deleteNonePromotionAppliedProductCount
        );
    }

    private boolean processDiscount(
            Orders orders,
            Predicate<Order> condition,
            Function<Order, Boolean> confirmationInput,
            Consumer<Order> action
    ) {
        return orders.getOrders().stream()
                .filter(condition)
                .map(order -> applyDiscountIfConfirmed(order, confirmationInput, action))
                .reduce(false, Boolean::logicalOr);
    }

    private boolean applyDiscountIfConfirmed(
            Order order,
            Function<Order, Boolean> confirmationInput,
            Consumer<Order> action
    ) {
        boolean confirmation = getConfirmation(order, confirmationInput);
        if (confirmation) {
            action.accept(order);
        }
        return confirmation;
    }

    private boolean getConfirmation(Order order, Function<Order, Boolean> confirmationInput) {
        return confirmationInput.apply(order);
    }

    private class OrderInputToOrdersUtil {
        private final Pattern pattern = Pattern.compile(REGULAR_EXPRESSION);

        public void getOrders(Orders orders, String order) {
            Matcher matcher = validateOrderFormat(order);
            String productName = matcher.group(MATCHER_PRODUCT_KEYWORD);
            Product product = getProduct(productName);
            Promotion promotion = getPromotion(product);
            int quantity = parseQuantity(matcher.group(MATCHER_QUANTITY_KEYWORD));
            orders.addOrder(product, promotion, quantity);
        }

        private Matcher validateOrderFormat(String order) {
            Matcher matcher = pattern.matcher(order);
            if (!matcher.find()) {
                throw new IllegalArgumentException(ErrorMessage.INVALID_FORMAT.getMessage());
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
