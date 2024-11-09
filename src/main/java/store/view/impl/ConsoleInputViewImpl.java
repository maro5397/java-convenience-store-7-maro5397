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
        return Console.readLine();
    }

    @Override
    public boolean getConfirmationMembershipDiscountInput() {
        System.out.println(MEMBERSHIP_DISCOUNT_MESSAGE);
        return checkAnswer(Console.readLine());
    }

    @Override
    public boolean getConfirmationNonePromotionInput(Order order) {
        System.out.printf(PROMOTION_CONFIRMATION_MESSAGE, order.getProduct().getName(),
                order.getOrderResult().getNoneDiscountPromotionStockCount());
        return checkAnswer(Console.readLine());
    }

    @Override
    public boolean getConfirmationFreeAdditionInput(Order order) {
        System.out.printf(CONFIRM_FREE_ADDITION_MESSAGE, order.getProduct().getName(), order.getPromotion().getGet());
        return checkAnswer(Console.readLine());
    }

    @Override
    public boolean getAdditionalPurchaseInput() {
        System.out.println(ADDITIONAL_PURCHASE_MESSAGE);
        return checkAnswer(Console.readLine());
    }

    private boolean checkAnswer(String answer) {
        if(answer.equals(YES) || answer.equals(NO)) {
            return answer.equals(YES);
        }
        throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
    }
}
