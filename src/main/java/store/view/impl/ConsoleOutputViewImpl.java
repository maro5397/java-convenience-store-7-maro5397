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
    private static final String PRODUCT_NAME_MESSAGE = "상품명";
    private static final String QUANTITY_MESSAGE = "수량";
    private static final String PRICE_MESSAGE = "금액";
    private static final String TOTAL_PRICE_MESSAGE = "총구매액";
    private static final String PROMOTION_DISCOUNT_MESSAGE = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT_MESSAGE = "멤버십할인";
    private static final String PURCHASE_AMOUNT_MESSAGE = "내실돈";
    private static final String RECEIPT_ORDERS_HEADER_TOP_MESSAGE = "\n==============W 편의점================";
    private static final String RECEIPT_PROMOTION_HEADER_MESSAGE = "=============증  정===============";
    private static final String RECEIPT_FOOTER_TOP_MESSAGE = "====================================";
    private static final String STOCK_STATUS_FORMAT = "- %s %,d원 %,d개 %s\n";
    private static final String NONE_STOCK_STATUS_FORMAT = "- %s %,d원 재고 없음 %s\n";
    private static final String RECEIPT_ORDERS_HEADER_BOTTOM_FORMAT = "%-10s %10s %10s\n";
    private static final String RECEIPT_ORDERS_FORMAT = "%-10s %,10d %,10d\n";
    private static final String RECEIPT_PROMOTION_FORMAT = "%-10s %,10d\n";
    private static final String TOTAL_PRICE_FORMAT = "%-10s %10d %,10d\n";
    private static final String PROMOTION_DISCOUNT_FORMAT = "%-20s %,10d\n";
    private static final String MEMBERSHIP_DISCOUNT_FORMAT = "%-20s %,10d\n";
    private static final String PURCHASE_AMOUNT_FORMAT = "%-20s %,10d\n";
    private static final String NONE_PROMOTION_PRODUCT = "";

    @Override
    public void displayGreeting() {
        System.out.println(GREETING_MESSAGE);
    }

    @Override
    public void displayStockStatus(List<Product> products) {
        products.forEach(product -> {
            displayPromotionStockStatus(product);
            displayStockStatus(product);
        });
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
        System.out.printf(STOCK_STATUS_FORMAT, product.getName(), product.getPrice(), stock, promotion);
    }

    private void displayNoneStockMessage(Product product, String promotion) {
        System.out.printf(NONE_STOCK_STATUS_FORMAT, product.getName(), product.getPrice(), promotion);
    }

    private void printReceiptHeader() {
        System.out.println(RECEIPT_ORDERS_HEADER_TOP_MESSAGE);
        System.out.printf(RECEIPT_ORDERS_HEADER_BOTTOM_FORMAT, PRODUCT_NAME_MESSAGE, QUANTITY_MESSAGE, PRICE_MESSAGE);
    }

    private void printOrderDetails(Orders orders) {
        orders.getOrders().forEach(this::printOrderDetail);
    }

    private void printOrderDetail(Order order) {
        int totalPrice = order.getProduct().getPrice() * order.getQuantity();
        System.out.printf(RECEIPT_ORDERS_FORMAT, order.getProduct().getName(), order.getQuantity(), totalPrice);
    }

    private void printPromotionDetails(Orders orders) {
        System.out.println(RECEIPT_PROMOTION_HEADER_MESSAGE);
        orders.getOrders().forEach(this::printPromotionDetail);
    }

    private void printPromotionDetail(Order order) {
        if (order.getOrderResult().getPromotionApplyFreeItemQuantity() != 0) {
            System.out.printf(
                    RECEIPT_PROMOTION_FORMAT,
                    order.getProduct().getName(),
                    order.getOrderResult().getPromotionApplyFreeItemQuantity()
            );
        }
    }

    private void printReceiptFooter(Orders orders, boolean isMembership) {
        int totalPromotionDiscount = orders.getTotalPromotionDiscount() * -1;
        int membershipDiscount = orders.getMembershipDiscount(isMembership) * -1;
        int finalPrice = orders.getTotalPrice() - orders.getTotalPromotionDiscount() - membershipDiscount;
        System.out.println(RECEIPT_FOOTER_TOP_MESSAGE);
        System.out.printf(TOTAL_PRICE_FORMAT, TOTAL_PRICE_MESSAGE, orders.getTotalQuantity(), orders.getTotalPrice());
        System.out.printf(PROMOTION_DISCOUNT_FORMAT, PROMOTION_DISCOUNT_MESSAGE, totalPromotionDiscount);
        System.out.printf(MEMBERSHIP_DISCOUNT_FORMAT, MEMBERSHIP_DISCOUNT_MESSAGE, membershipDiscount);
        System.out.printf(PURCHASE_AMOUNT_FORMAT, PURCHASE_AMOUNT_MESSAGE, finalPrice);
    }
}
