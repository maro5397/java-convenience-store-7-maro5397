package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    private final List<Order> orders = new ArrayList<>();

    public void addOrder(Product product, int quantity) {
        Order order = new Order(product, quantity);
        order.applyPromotionDiscount();
        orders.add(new Order(product, quantity));
    }

    public int getTotalPrice() {
        int total = 0;
        for (Order order : orders) {
            total += (order.getPromotionResult().getFreeItemCount() + order.getPromotionResult().getPaidItemCount()) * order.getProduct().getPromotionProduct().getPrice();
            total += order.getPromotionResult().getNoneDiscountItemCount() * order.getProduct().getPrice();
        }
        return total;
    }

    public int getPromotionDiscount() {
        int discountPrice = 0;
        for (Order order : orders) {
            int freeItemCount = order.getPromotionResult().getFreeItemCount();
            discountPrice += freeItemCount * order.getProduct().getPromotionProduct().getPrice();
        }
        return discountPrice;
    }

    public int getMembershipDiscount() {
        int discountPrice = 0;
        for (Order order : orders) {
            int noneDiscountItemCount = order.getPromotionResult().getNoneDiscountItemCount();
            discountPrice += noneDiscountItemCount * order.getProduct().getPrice();
        }
        return Math.min((int) (discountPrice * 0.3), 8000);
    }

    public Order getOrderByProductName(String name) {
        for (Order order : orders) {
            if(order.getProduct().getName().equals(name)) {
                return order;
            }
        }
        throw new RuntimeException("[ERROR] 찾는 주문이 없습니다.");
    }
}
