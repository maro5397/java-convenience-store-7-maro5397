package store.service;

import java.util.List;
import store.domain.Orders;
import store.domain.Product;
import store.view.InputView;

public interface PurchaseService {
    List<Product> getStock();

    Orders makeOrders(String orderInput);

    boolean processAdditionalPromotionDiscount(Orders orders, InputView inputView);

    boolean processNonePromotionProductDelete(Orders orders, InputView inputView);
}
