package store.domain;

public class Receipt {
    int freeItemCount;
    int paidItemCount;
    int noneDiscountItemCount;

    public Receipt(int freeItemCount, int paidItemCount, int noneDiscountItemCount) {
        this.freeItemCount = freeItemCount;
        this.paidItemCount = paidItemCount;
        this.noneDiscountItemCount = noneDiscountItemCount;
    }

    public int getFreeItemCount() {
        return freeItemCount;
    }

    public int getPaidItemCount() {
        return paidItemCount;
    }

    public int getNoneDiscountItemCount() {
        return noneDiscountItemCount;
    }
}
