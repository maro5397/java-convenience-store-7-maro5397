package store.domain;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;

public class Orders {
    private final List<Order> orders = new ArrayList<>();

    public void addOrder(Product product, Promotion promotion, int quantity) throws NoSuchObjectException {
        Order order = new Order(product, promotion, quantity);
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
                        order.getOrderResult().getPromotionApplyfreeItemCount() * order.getProduct().getPrice();
            }
        }
        return discountPrice;
    }

    public int getMembershipDiscount(boolean isMembership) {
        if (!isMembership) {
            return 0;
        }
        int discountPrice = 0;
        for (Order order : orders) {
            if (order.getOrderResult().getPromotionProductConsumeCount() != 0) {
                int noneDiscountPromotionStockCount = order.getOrderResult().getNoneDiscountPromotionStockCount();
                discountPrice += noneDiscountPromotionStockCount * order.getProduct().getPrice();
            }
            discountPrice += order.getOrderResult().getProductConsumeCount() * order.getProduct().getPrice();
        }
        return Math.min((int) (discountPrice * 0.3), 8000);
    }

    public void applyConsumeStock() {
        for (Order order : orders) {
            order.applyConsumeStock();
        }
    }
}
