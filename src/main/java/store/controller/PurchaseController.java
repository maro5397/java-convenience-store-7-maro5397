package store.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import store.domain.Order;
import store.domain.OrderResult;
import store.domain.Orders;
import store.domain.Product;
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
        boolean retry = false;
        do {
            this.outputView.displayGreeting();
            List<Product> stock = this.purchaseService.getStock();
            this.outputView.displayStockStatus(stock);
            String purchaseInput = this.inputView.getPurchaseInput();
            Orders orders = this.purchaseService.makeOrders(purchaseInput);
            for (Order order : orders.getOrders()) {
                this.purchaseService.applyPromotionProduct(order);
                if (order.getCanApplyAdditionalPromotion()) {
                    boolean confirmationFreeAdditionInput = this.inputView.getConfirmationFreeAdditionInput(order);
                    if (confirmationFreeAdditionInput) {
                        order.applyAdditionalPromotion();
                    }
                }
            }
            for (Order order : orders.getOrders()) {
                OrderResult orderResult = this.purchaseService.applyPromotionProduct(order);
                if (order.getPromotion() != null && orderResult.getNoneDiscountPromotionStockCount() != 0) {
                    boolean confirmationNonePromotionInput = this.inputView.getConfirmationNonePromotionInput(order);
                    if (!confirmationNonePromotionInput) {
                        order.deleteNonePromotionAppliedProductCount();
                    }
                }
                this.purchaseService.applyPromotionProduct(order);
            }
            boolean confirmationMembershipDiscountInput = this.inputView.getConfirmationMembershipDiscountInput();
            this.outputView.displayReceipt(orders, confirmationMembershipDiscountInput);
            orders.applyConsumeStock();
            retry = this.inputView.getAdditionalPurchaseInput();
        } while (retry);
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
