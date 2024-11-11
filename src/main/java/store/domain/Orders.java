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
            if (order.getOrderResult().getPromotionProductConsumeQuantity() != 0) {
                total += order.getOrderResult().getPromotionProductConsumeQuantity() * order.getProduct().getPrice();
            }
            total += order.getOrderResult().getProductConsumeQuantity() * order.getProduct().getPrice();
        }
        return total;
    }

    public int getTotalPromotionDiscount() {
        int discountPrice = 0;
        for (Order order : orders) {
            if (order.getOrderResult().getPromotionProductConsumeQuantity() != 0) {
                discountPrice +=
                        order.getOrderResult().getPromotionApplyFreeItemQuantity() * order.getProduct().getPrice();
            }
        }
        return discountPrice;
    }

    public int getMembershipDiscount(boolean isMembership) {
        if (!isMembership) {
            return 0;
        }
        return applyMaxDiscountLimit(calculateMembershipDiscountPrice());
    }

    public void applyConsumeStock() {
        orders.forEach(Order::consumeStockForOrder);
    }

    private int calculateMembershipDiscountPrice() {
        return orders.stream().mapToInt(this::calculatePriceForNonDiscountedQuantity).sum();
    }

    private int calculatePriceForNonDiscountedQuantity(Order order) {
        return order.getOrderResult().getNonDiscountedOrderQuantity() * order.getProduct().getPrice();
    }

    private int applyMaxDiscountLimit(int discountPrice) {
        return Math.min((int) (discountPrice * MEMBERSHIP_DISCOUNT_RATE), MAX_MEMBERSHIP_DISCOUNT);
    }
}
