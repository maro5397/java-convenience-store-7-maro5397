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
    private static final String NONE_PROMOTION_PRODUCT = "";

    @Override
    public void displayGreeting() {
        System.out.println(GREETING_MESSAGE);
    }

    @Override
    public void displayStockStatus(List<Product> products) {
        for (Product product : products) {
            displayPromotionStockStatus(product);
            displayStockStatus(product);
        }
    }

    @Override
    public void displayReceipt(Orders orders, boolean isMembership) {
        printReceiptHeader();
        printOrderDetails(orders);
        printPromotionDetails(orders);
        printReceiptFooter(orders, isMembership);
    }

    private void displayPromotionStockStatus(Product product) {
        if (!product.getPromotion().isEmpty()) {
            if (product.getPromotionStock() != 0) {
                displayStockMessage(product, product.getPromotionStock(), product.getPromotion());
                return;
            }
            displayNoneStockMessage(product, product.getPromotion());
        }
    }

    private void displayStockStatus(Product product) {
        if (product.getStock() != 0) {
            displayStockMessage(product, product.getStock(), NONE_PROMOTION_PRODUCT);
            return;
        }
        displayNoneStockMessage(product, NONE_PROMOTION_PRODUCT);
    }

    private void displayStockMessage(Product product, int stock, String promotion) {
        System.out.printf(STOCK_STATUS_MESSAGE, product.getName(), product.getPrice(), stock, promotion);
    }

    private void displayNoneStockMessage(Product product, String promotion) {
        System.out.printf(NONE_STOCK_STATUS_MESSAGE, product.getName(), product.getPrice(), promotion);
    }

    private void printReceiptHeader() {
        System.out.println(RECEIPT_ORDERS_HEADER_MESSAGE);
    }

    private void printOrderDetails(Orders orders) {
        for (Order order : orders.getOrders()) {
            printOrderDetail(order);
        }
    }

    private void printOrderDetail(Order order) {
        int totalPrice = order.getProduct().getPrice() * order.getQuantity();
        System.out.printf(RECEIPT_ORDERS_MESSAGE, order.getProduct().getName(), order.getQuantity(), totalPrice);
    }

    private void printPromotionDetails(Orders orders) {
        System.out.println(RECEIPT_PROMOTION_HEADER_MESSAGE);
        for (Order order : orders.getOrders()) {
            printPromotionDetail(order);
        }
    }

    private void printPromotionDetail(Order order) {
        if (order.getOrderResult().getPromotionApplyfreeItemCount() != 0) {
            System.out.printf(RECEIPT_PROMOTION_MESSAGE, order.getProduct().getName(),
                    order.getOrderResult().getPromotionApplyfreeItemCount());
        }
    }

    private void printReceiptFooter(Orders orders, boolean isMembership) {
        int totalDiscount = orders.getPromotionDiscount() * -1;
        int membershipDiscount = orders.getMembershipDiscount(isMembership) * -1;
        int finalPrice = orders.getTotalPrice() - orders.getPromotionDiscount() - membershipDiscount;
        System.out.printf(RECEIPT_FOOTER_MESSAGE, orders.getTotalQuantity(), orders.getTotalPrice(),
                totalDiscount, membershipDiscount, finalPrice);
    }
}
