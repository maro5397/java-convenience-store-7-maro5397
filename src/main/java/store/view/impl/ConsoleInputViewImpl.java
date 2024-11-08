package store.view.impl;

import camp.nextstep.edu.missionutils.Console;
import store.domain.Order;
import store.view.InputView;

public class ConsoleInputViewImpl implements InputView {
    private static final String PURCHASE_INPUT_MESSAGE = "\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String MEMBERSHIP_DISCOUNT_MESSAGE = "\n멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String PROMOTION_CONFIRMATION_MESSAGE = "\n현재 %s %,d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n";
    private static final String CONFIRM_FREE_ADDITION_MESSAGE = "\n현재 %s은(는) %,d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n";
    private static final String ADDITIONAL_PURCHASE_MESSAGE = "\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";
    private static final String YES = "Y";
    private static final String NO = "N";

    @Override
    public String getPurchaseInput() {
        System.out.println(PURCHASE_INPUT_MESSAGE);
        String orderInput = Console.readLine();
        return orderInput;
    }

    @Override
    public boolean getConfirmationMembershipDiscountInput() {
        System.out.println(MEMBERSHIP_DISCOUNT_MESSAGE);
        String answer = Console.readLine();
        return answer.equals(YES);
    }

    @Override
    public boolean getConfirmationNonePromotionInput(Order order) {
        System.out.printf(PROMOTION_CONFIRMATION_MESSAGE, order.getProduct().getName(),
                order.getOrderResult().getNoneDiscountPromotionStockCount());
        String answer = Console.readLine();
        return answer.equals(YES);
    }

    @Override
    public boolean getConfirmationFreeAdditionInput(Order order) {
        System.out.printf(CONFIRM_FREE_ADDITION_MESSAGE, order.getProduct().getName(), order.getPromotion().getGet());
        String answer = Console.readLine();
        return answer.equals(YES);
    }

    @Override
    public boolean getAdditionalPurchaseInput() {
        System.out.println(ADDITIONAL_PURCHASE_MESSAGE);
        String answer = Console.readLine();
        return answer.equals(YES);
    }
}
