package store.controller;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
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
            confirmAdditionoalPromotionalDiscount(orders);
            confirmOrderWithoutPromotionDiscount(orders);
            displayReceipt(orders);
            consumeStockByOrders(orders);
        } while (confirmAdditionalPurchase());
    }

    private void displayStockStatus() {
        this.outputView.displayGreeting();
        this.outputView.displayStockStatus(this.purchaseService.getStock());
    }

    private Orders getOrderFromCustomer() {
        return executeWithRetry(() -> {
            return this.purchaseService.makeOrders(this.inputView.getPurchaseInput());
        });
    }

    private boolean confirmAdditionoalPromotionalDiscount(Orders orders) {
        return executeWithRetry(() -> {
            return this.purchaseService.processAdditionalPromotionDiscount(orders, this.inputView);
        });
    }

    private boolean confirmOrderWithoutPromotionDiscount(Orders orders) {
        return executeWithRetry(() -> {
            return this.purchaseService.processNonePromotionProductDelete(orders, this.inputView);
        });
    }

    private void displayReceipt(Orders orders) {
        this.outputView.displayReceipt(orders, confirmMembershipDiscount());
    }

    private boolean confirmMembershipDiscount() {
        return executeWithRetry(() -> {
            return this.inputView.getConfirmationMembershipDiscountInput();
        });
    }

    private void consumeStockByOrders(Orders orders) {
        orders.applyConsumeStock();
    }

    private boolean confirmAdditionalPurchase() {
        return executeWithRetry(() -> {
            return this.inputView.getConfirmationAdditionalPurchaseInput();
        });
    }

    private <T> T executeWithRetry(Callable<T> callable) {
        while (true) {
            try {
                return callable.call();
            } catch (NoSuchElementException exception) {
                throw exception;
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
