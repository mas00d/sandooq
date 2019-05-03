package masood.sandooq;

import masood.sandooq.model.Customer;

public class NextTransaction {
    public final Customer customer;
    public final TransactionType transactionType;
    public final int count;

    public NextTransaction(Customer customer, TransactionType transactionType, int count) {
        this.customer = customer;
        this.transactionType = transactionType;
        this.count = count;
    }

    @Override
    public String toString() {
        return customer.toString() + "  " + transactionType.toString() + "  " + count;
    }
}
