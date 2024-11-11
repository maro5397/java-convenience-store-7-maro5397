package store.common.constant;

public enum ErrorMessage {
    FILE_READ_ERROR("[ERROR] 파일을 읽는 중 오류가 발생했습니다:"),
    ORDER_OUT_OF_STOCK("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    PRODUCT_NAME_MISSING("[ERROR] 상품 이름이 없습니다."),
    NAME_CANNOT_BE_EMPTY("[ERROR] 이름은 공백 문자열이 될 수 없습니다."),
    PRODUCT_NAME_TOO_LONG("[ERROR] 상품 이름은 100자 이하여야 합니다."),
    PRICE_CANNOT_BE_NEGATIVE("[ERROR] 가격은 음수나 0이 될 수 없습니다."),
    STOCK_CANNOT_BE_NEGATIVE("[ERROR] 재고는 음수가 될 수 없습니다."),
    INSUFFICIENT_STOCK("[ERROR] 재고 수량이 충분하지 않습니다."),
    PRODUCT_NOT_FOUND("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."),
    INVALID_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_INPUT("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}