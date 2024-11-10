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
        orders.add(Order.create(product, promotion, quantity));
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getTotalQuantity() {
        return orders.parallelStream().mapToInt(Order::getQuantity).sum();
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
        return applyDiscountLimit(calculateDiscountPrice());
    }

    private int calculateDiscountPrice() {
        return orders.stream().mapToInt(this::calculateOrderDiscount).sum();
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
        return order.getOrderResult().getNoneDiscountPromotionStockCount() * order.getProduct().getPrice();
    }

    private int calculateProductPrice(Order order) {
        return order.getOrderResult().getProductConsumeCount() * order.getProduct().getPrice();
    }

    private int applyDiscountLimit(int discountPrice) {
        return Math.min((int) (discountPrice * MEMBERSHIP_DISCOUNT_RATE), MAX_MEMBERSHIP_DISCOUNT);
    }

    public void applyConsumeStock() {
        orders.forEach(Order::applyConsumeStock);
    }
}
