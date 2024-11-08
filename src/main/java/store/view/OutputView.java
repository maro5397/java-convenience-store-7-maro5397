package store.view;

import java.util.List;
import store.domain.Orders;
import store.domain.Product;

public interface OutputView {
    void displayGreeting();

    void displayStockStatus(List<Product> stock);

    void displayReceipt(Orders orders, boolean isMembership);
}
