package store.view.impl;

import java.util.List;
import store.domain.Order;
import store.domain.Orders;
import store.domain.Product;
import store.view.OutputView;

public class ConsoleOutputViewImpl implements OutputView {
    private static final String GREETING_MESSAGE = """
            안녕하세요. W편의점입니다.
            현재 보유하고 있는 상품입니다.
            """;
    private static final String STOCK_STATUS_MESSAGE = "- %s %,d원 %,d개 %s\n";
    private static final String NONE_STOCK_STATUS_MESSAGE = "- %s %,d원 재고 없음 %s\n";
    private static final String RECEIPT_ORDERS_HEADER_MESSAGE = "\n===========W 편의점=============\n상품명\t\t수량\t금액";
    private static final String RECEIPT_ORDERS_MESSAGE = "%s\t\t%,d \t%,d\n";
    private static final String RECEIPT_PROMOTION_HEADER_MESSAGE = "===========증\t정=============";
    private static final String RECEIPT_PROMOTION_MESSAGE = "%s\t\t%,d\n";
    private static final String RECEIPT_FOOTER_MESSAGE = """
            ==============================
            총구매액\t\t%,d\t%,d
            행사할인\t\t\t%,d
            멤버십할인\t\t\t%,d
            내실돈\t\t\t %,d
            \
            """;

    @Override
    public void displayGreeting() {
        System.out.println(GREETING_MESSAGE);
    }

    @Override
    public void displayStockStatus(List<Product> products) {
        for (Product product : products) {
            if (!product.getPromotion().isEmpty()) {
                if (product.getPromotionStock() != 0) {
                    System.out.printf(STOCK_STATUS_MESSAGE, product.getName(), product.getPrice(), product.getPromotionStock(),
                            product.getPromotion());
                }
                if (product.getPromotionStock() == 0) {
                    System.out.printf(NONE_STOCK_STATUS_MESSAGE, product.getName(), product.getPrice(),
                            product.getPromotion());
                }
            }
            if (product.getStock() != 0) {
                System.out.printf(STOCK_STATUS_MESSAGE, product.getName(), product.getPrice(), product.getStock(), "");
            }
            if (product.getStock() == 0) {
                System.out.printf(NONE_STOCK_STATUS_MESSAGE, product.getName(), product.getPrice(), "");
            }
        }
    }

    @Override
    public void displayReceipt(Orders orders, boolean isMembership) {
        System.out.println(RECEIPT_ORDERS_HEADER_MESSAGE);
        for (Order order : orders.getOrders()) {
            System.out.printf(RECEIPT_ORDERS_MESSAGE, order.getProduct().getName(), order.getQuantity(),
                    order.getProduct().getPrice() * order.getQuantity());
        }
        System.out.println(RECEIPT_PROMOTION_HEADER_MESSAGE);
        for (Order order : orders.getOrders()) {
            if (order.getOrderResult().getPromotionApplyfreeItemCount() != 0) {
                System.out.printf(RECEIPT_PROMOTION_MESSAGE, order.getProduct().getName(),
                        order.getOrderResult().getPromotionApplyfreeItemCount());
            }
        }
        System.out.printf(RECEIPT_FOOTER_MESSAGE, orders.getTotalQuantity(), orders.getTotalPrice(),
                orders.getPromotionDiscount() * -1,
                orders.getMembershipDiscount(isMembership) * -1,
                orders.getTotalPrice() - orders.getPromotionDiscount() - orders.getMembershipDiscount(isMembership));
    }
}
