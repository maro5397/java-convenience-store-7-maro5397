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
        return executeWithRetry(() -> {
            return this.purchaseService.makeOrders(this.inputView.getPurchaseInput());
        });
    }

    private boolean findAdditionalPromotionDiscount(Orders orders) {
        return executeWithRetry(() -> {
            return this.purchaseService.processAdditionalPromotionDiscount(orders, this.inputView);
        });
    }

    private boolean findNonePromotionDiscount(Orders orders) {
        return executeWithRetry(() -> {
            return this.purchaseService.processNonePromotionProductDelete(orders, this.inputView);
        });
    }

    private void displayReceipt(Orders orders) {
        this.outputView.displayReceipt(orders, askMembershipDiscount());
    }

    private boolean askMembershipDiscount() {
        return executeWithRetry(() -> {
            return this.inputView.getConfirmationMembershipDiscountInput();
        });
    }

    private void applyConsumeStock(Orders orders) {
        orders.applyConsumeStock();
    }

    private boolean askAdditionalPurchase() {
        return executeWithRetry(() -> {
            return this.inputView.getAdditionalPurchaseInput();
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
