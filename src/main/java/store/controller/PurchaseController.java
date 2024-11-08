package store.controller;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import store.domain.Order;
import store.domain.Orders;
import store.service.PurchaseService;
import store.view.InputView;
import store.view.OutputView;

public class PurchaseController {
    PurchaseService purchaseService;
    InputView inputView;
    OutputView outputView;

    public PurchaseController(PurchaseService purchaseService, InputView inputView, OutputView outputView) {
        this.purchaseService = purchaseService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void start() {
        do {
            displayStockStatus();
            Orders orders = getOrderFromCustomer();
            findAdditionalPromotionDiscount(orders);
            findNonePromotionDiscount(orders);
            displayReceipt(orders);
            applyConsumeStock(orders);
        } while (askAdditionalPurchase());
    }

    private void displayStockStatus() {
        this.outputView.displayGreeting();
        this.outputView.displayStockStatus(this.purchaseService.getStock());
    }

    private Orders getOrderFromCustomer() {
        return this.purchaseService.makeOrders(this.inputView.getPurchaseInput());
    }

    private void findAdditionalPromotionDiscount(Orders orders) {
        for (Order order : orders.getOrders()) {
            if (order.getCanApplyAdditionalPromotion()) {
                suggestAdditionalPromotionDiscount(order);
            }
        }
    }

    private void suggestAdditionalPromotionDiscount(Order order) {
        boolean confirmationFreeAdditionInput = this.inputView.getConfirmationFreeAdditionInput(order);
        if (confirmationFreeAdditionInput) {
            this.purchaseService.applyAdditionalPromotionProduct(order);
        }
    }

    private void findNonePromotionDiscount(Orders orders) {
        for (Order order : orders.getOrders()) {
            if (order.getPromotion() != null && order.getOrderResult().getNoneDiscountPromotionStockCount() != 0) {
                suggestNonePromotionDiscount(order);
            }
        }
    }

    private void suggestNonePromotionDiscount(Order order) {
        boolean confirmationNonePromotionInput = this.inputView.getConfirmationNonePromotionInput(order);
        if (!confirmationNonePromotionInput) {
            this.purchaseService.deleteNonePromotionProduct(order);
        }
    }

    private boolean askMembershipDiscount() {
        return this.inputView.getConfirmationMembershipDiscountInput();
    }

    private void displayReceipt(Orders orders) {
        this.outputView.displayReceipt(orders, askMembershipDiscount());
    }

    private boolean askAdditionalPurchase() {
        return this.inputView.getAdditionalPurchaseInput();
    }

    private void applyConsumeStock(Orders orders) {
        orders.applyConsumeStock();
    }

    private <T> T executeWithRetry(Callable<T> callable) {
        while (true) {
            try {
                return callable.call();
            } catch (NoSuchElementException error) {
                throw error;
            } catch (Exception error) {
                System.out.println(error.getMessage());
            }
        }
    }
}
