package store.service;

import java.util.List;
import store.domain.Order;
import store.domain.Orders;
import store.domain.Product;
import store.domain.OrderResult;

public interface PurchaseService {
    List<Product> getStock();

    Orders makeOrders(String orderInput);

    Order addApplyPromotionProduct(String productName, int additionalQuantity);

    void deleteNonePromotionProduct(Order order);

    int calculateMembershipDiscount(OrderResult promotionResult);
}
