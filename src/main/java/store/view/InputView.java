package store.view;

import store.domain.Order;

public interface InputView {
    String getPurchaseInput();

    boolean getConfirmationMembershipDiscountInput();

    boolean getConfirmationNonePromotionInput(Order order);

    boolean getConfirmationFreeAdditionInput(Order order);

    boolean getAdditionalPurchaseInput();
}
