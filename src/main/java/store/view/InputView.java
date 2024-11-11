package store.view;

import store.domain.Order;

public interface InputView {
    String getPurchaseInput();

    boolean getConfirmationMembershipDiscountInput();

    boolean getConfirmationNonDiscountedOrderInput(Order order);

    boolean getConfirmationAdditionalPromotionInput(Order order);

    boolean getConfirmationAdditionalPurchaseInput();
}
