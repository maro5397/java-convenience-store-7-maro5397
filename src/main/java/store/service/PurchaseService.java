package store.service;

import java.rmi.NoSuchObjectException;
import java.util.List;
import store.domain.Order;
import store.domain.Orders;
import store.domain.Product;

public interface PurchaseService {
    List<Product> getStock();

    Orders makeOrders(String orderInput) throws NoSuchObjectException;

    void applyAdditionalPromotionProduct(Order order);

    void deleteNonePromotionProduct(Order order);
}
