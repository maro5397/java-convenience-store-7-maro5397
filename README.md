# java-convenience-store-precourse

---

# 결제 시스템 기능 명세

## UseFlow

1. 재고를 나열한다.
2. 주문을 입력받아 주문 목록을 만든다.
3. 주문 목록에서 추가 프로모션을 적용할 수 있는 주문을 찾아 사용자에게 안내한다.
4. 조건에 따라 추가 프로모션을 적용한다. 
5. 주문 목록에서 프로모션 재고 부족으로 프로모션 할인을 받지 못하는 재고에 대해 지불할 것인지 안내한다.
6. 조건에 따라 프로모션 미적용 재고를 지불 상품에서 제외한다.
7. 맴버쉽 할인을 받을 것인지 안내한다.
8. 조건에 따라 맴버쉽 할인을 적용한다.
9. 추가로 상품을 구매할 것인지 안내한다.
10. 조건에 따라 추가로 상품을 구매하기 위해 1단계로 돌아간다.

## 서비스 객체 기능 명세

### PurchaseService
- **재고 확인** 
  - `getStock()`: 파일에 존재하는 데이터를 List로 반환
- **주문 생성**
  - `makeOrders(String orderInput)`: 입력받은 주문을 Orders객체로 반환
- **프로모션 할인 적용**
  - `applyPromotionProduct(Order order)`: 각 주문에 프로모션 할인 적용 후 OrderResult 반환
- **프로모션 추가 적용**
  - `addApplyPromotionProduct(String productName, int additionalQuantity)`: 추가 수량 만큼 프로모션을 적용
- **프로모션 미적용 상품 제외**
  - `deleteNonePromotionProduct(Order order)`: 프로모션 수량 부족으로 프로모션 미적용 상품 제외
- **맴버쉽 할인 적용**
  - `calculateMembershipDiscount(OrderResult promotionResult)`: 맴버쉽 할인 적용

## 도메인 객체 기능 명세

### OrderResult
- **프로모션 정책을 적용했을때 결과**
    - `promotionApplyfreeItemCount`: 프로모션 적용 상품 중 무료로 받을 수 있는 상품 개수
    - `promotionApplypaidItemCount`: 프로모션 적용 상품 중 유료로 받을 수 있는 상품 개수
    - `promotionProductConsumeCount`: 프로모션 재고 차감 수
    - `productConsumeCount`: 일반 재고 차감 수
    - `getNoneDiscountPromotionStockCount()`: 프로모션 적용을 받지 않는 상품 개수

### Promotion
- **프로모션 정책 관리**
    - `name`: 프로모션 유형 (1+1, 2+1 등)
    - `promotionStrategy`: 프로모션 시작일(`startDate`)과 종료일(`endDate`) 관리
    - `buy`: `get`만큼 무료로 받기 위해 구매해야하는 프로모션 상품 개수
    - `get`: `buy`만큼 샀을 때 무료로 받을 수 있는 프로모션 상품 개수
    - `calculatePromotionDiscount(int promotionStock, int quantity)`: 프로모션 조건에 따라 할인을 적용할 수 있는 수량을 계산
    - `canApplyPromotion(int quantity)`: 프로모션 추가 적용이 가능한지 유무를 확인
    - `isWithinPromotionPeriod(LocalDateTime now)`: 현재 날짜가 프로모션 기간 내에 있는지 확인

### Product
- **상품의 기본 정보 관리**
    - `name`: 상품명
      - 상품명은 null이 될 수 없다.
      - 상품명은 공백으로 이루어진 문자열이 될 수 없다.
      - 상품명의 길이는 최대 100자 이하이다.
    - `price`: 상품의 단가
      - 상품의 단가는 0원 또는 음수가 될 수 없다.
      - 상품의 단가는 어떤 범위의 숫자든 상관 없어야 한다.
    - `stock`: 현재 재고 수량
      - 재고의 수량은 음수가 될 수 없다.
      - 재고의 수량은 어떤 범위의 숫자든 상관 없어야 한다.
    - `promotionStock`: 프로모션용 상품 재고 수량
      - 재고의 수량은 음수가 될 수 없다.
      - 재고의 수량은 어떤 범위의 숫자든 상관 없어야 한다.
    - `promotion`: 적용되는 프로모션 이름
- **재고 확인 및 감소**
    - `hasSufficientStock(int quantity)`: 구매 수량이 재고 수량을 초과하지 않는지 확인
      - 재고 수량을 구매 수량이 초과할 경우 예외 발생
    - `hasSufficientPromotionStock(int quantity)`: 구매 수량이 프로모션 재고 수량을 초과하지 않는지 확인
      - 프로모션 재고 수량을 구매 수량이 초과할 경우 예외 발생
    - `decrementStock(int quantity)`: 구매된 수량만큼 재고를 차감
    - `decrementPromotionStock(int quantity)`: 구매된 수량 만큼 프로모션 재고 차감

### Orders
- **구매 요청 관리**
    - `orders`: 고객이 구매한 상품과 수량 목록
    - `addOrder(Product product, Promotion promotion, int quantity)`: 고객이 구매 요청한 상품을 목록에 추가
    - `getTotalQuantity()`: 구매 상품의 총 개수
    - `getTotalPrice()`: 구매한 상품의 가격과 수량을 곱해 총구매액을 계산
- **프로모션 할인 적용**
    - `getPromotionDiscount()`: 프로모션 할인 조건을 충족하는 상품에 대해 무료 증정 혜택을 적용하고 할인 금액을 계산
- **멤버십 할인 적용**
    - `getMembershipDiscount(boolean isMembership)`: 멤버십 할인을 적용하여 프로모션 미적용 금액의 30%를 할인하고, 최대 8,000원 한도를 고려해 최종 결제 금액 산출
- **주문 반환**
    - `applyConsumeStock()`: 각 주문에 해당하는 상품 개수 차감

### Order
- **주문 관리**
    - `product`: 고객이 구매할 상품
    - `promotion`: 주문에 적용된 프로모션
    - `canApplyAdditionalPromotion`: 프로모션 해택 적용 결과
    - `orderResult`: 각 주문 요구사항 적용결과
    - `quantity`: 고객이 구매할 상품개수
      - 상품 개수는 0이 될 수 없음
- **프로모션 적용**
    - `canGetAdditionalProductByPromotion()`: 추가로 프로모션 상품을 받을 수 있는지 기록
    - `applyAdditionalPromotion()`: 추가로 프로모션 상품 받기 적용
    - `applyConsumeStock()`: 모든 요구 조건을 만족한 OrderResult에 따라 재고 소모
    - `consumePromotionProduct()`: 프로모션 적용 후 결과를 OrderResult 필드에 기록

---
### 실행 예시
```
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-3],[에너지바-5]

멤버십 할인을 받으시겠습니까? (Y/N)
Y 

===========W 편의점=============
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
===========증	정=============
콜라		1
==============================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 7개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-10]

현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
N

===========W 편의점=============
상품명		수량	금액
콜라		10 	10,000
===========증	정=============
콜라		2
==============================
총구매액		10	10,000
행사할인			-2,000
멤버십할인			-0
내실돈			 8,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 재고 없음 탄산2+1
- 콜라 1,000원 7개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[오렌지주스-1]

현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
Y

===========W 편의점=============
상품명		수량	금액
오렌지주스		2 	3,600
===========증	정=============
오렌지주스		1
==============================
총구매액		2	3,600
행사할인			-1,800
멤버십할인			-0
내실돈			 1,800

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
N
```