package masood.sandooq;

import masood.sandooq.io.CustomersReader;
import masood.sandooq.model.Customer;
import masood.sandooq.model.Customers;

import java.util.ArrayList;
import java.util.List;

public class Main {

    List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        Main obj = new Main();
        CustomersReader.readCustomers();
        obj.transactions = TransactionsFileUtility.readAllFiles();
//        obj.process(statementDescription);

        AssetCalculator assetCalculator = new AssetCalculator(obj.transactions);
        for (Customer customer : Customers.getInstance().getCustomers()) {
            System.out.print(customer.getName() + ":");
            System.out.println(assetCalculator.getAsset(customer));
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//TODO: وقتی که می خواهم که یک خط در بین خطوط زیر اضافه کنم، باید همه اندیس ها را باز نویسی کنم. این خوب نیست
//        NextTransaction[] nextTransactions = new NextTransaction[12];
        List<NextTransaction> nextTransactions = NextTransactionFileUtility.readFile();

        for (NextTransaction nextTransaction : nextTransactions) {
            if (nextTransaction.count > 0) {
                for (Transaction transaction : obj.transactions) {
                    if ((transaction.getCustomer() != null) && (nextTransaction.customer == transaction.getCustomer())) {
                        if (transaction.getTransactionType() == nextTransaction.transactionType) {
                            for (int i = 1; i <= nextTransaction.count; i++) {
                                for (String str : NextTranCalculator.nextTransaction(transaction, i)) {
                                    System.out.println(str);
                                }
                                System.out.println();
                            }
                            break;
                        }
                    }
                }
                System.out.println();
                System.out.println("-------------------------------------------");
            }
        }
    }
}
