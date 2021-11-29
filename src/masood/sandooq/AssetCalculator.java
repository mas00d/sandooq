package masood.sandooq;

import masood.sandooq.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class AssetCalculator {
    private List<Transaction> transactions = new ArrayList<>();

    public AssetCalculator(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getAsset(Customer customer) {
        int result = 0;
        for (Transaction tr : transactions) {
            if (customer.equals(tr.getCustomer())) {
                if (tr.getTransactionType() == TransactionType.MEMBERSHIP_FEE) {
                    result += tr.getAmount();
                }
            }
        }
        return result;
    }
}
