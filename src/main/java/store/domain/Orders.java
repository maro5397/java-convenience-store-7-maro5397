package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    private final List<Order> orders = new ArrayList<>();

    private Orders() {
    }

    public static Orders create() {
        return new Orders();
    }

    public void addOrder(Product product, Promotion promotion, int quantity) {
        Order order = Order.create(product, promotion, quantity);
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getTotalQuantity() {
        int total = 0;
        for (Order order : orders) {
            total += order.getQuantity();
        }
        return total;
    }

    public int getTotalPrice() {
        int total = 0;
        for (Order order : orders) {
            if (order.getOrderResult().getPromotionProductConsumeCount() != 0) {
                total += order.getOrderResult().getPromotionProductConsumeCount() * order.getProduct().getPrice();
            }
            total += order.getOrderResult().getProductConsumeCount() * order.getProduct().getPrice();
        }
        return total;
    }

    public int getPromotionDiscount() {
        int discountPrice = 0;
        for (Order order : orders) {
            if (order.getOrderResult().getPromotionProductConsumeCount() != 0) {
                discountPrice +=
                        order.getOrderResult().getPromotionApplyFreeItemCount() * order.getProduct().getPrice();
            }
        }
        return discountPrice;
    }

    public int getMembershipDiscount(boolean isMembership) {
        if (!isMembership) {
            return 0;
        }
        int discountPrice = calculateDiscountPrice();
        return applyDiscountLimit(discountPrice);
    }

    private int calculateDiscountPrice() {
        int discountPrice = 0;
        for (Order order : orders) {
            discountPrice += calculateOrderDiscount(order);
        }
        return discountPrice;
    }

    private int calculateOrderDiscount(Order order) {
        int orderDiscount = 0;
        if (order.getOrderResult().getPromotionProductConsumeCount() != 0) {
            orderDiscount += calculateNonDiscountPromotionPrice(order);
        }
        orderDiscount += calculateProductPrice(order);
        return orderDiscount;
    }

    private int calculateNonDiscountPromotionPrice(Order order) {
        int noneDiscountPromotionStockCount = order.getOrderResult().getNoneDiscountPromotionStockCount();
        return noneDiscountPromotionStockCount * order.getProduct().getPrice();
    }

    private int calculateProductPrice(Order order) {
        return order.getOrderResult().getProductConsumeCount() * order.getProduct().getPrice();
    }

    private int applyDiscountLimit(int discountPrice) {
        return Math.min((int) (discountPrice * MEMBERSHIP_DISCOUNT_RATE), MAX_MEMBERSHIP_DISCOUNT);
    }

    public void applyConsumeStock() {
        for (Order order : orders) {
            order.applyConsumeStock();
        }
    }
}
