package masood.sandooq;

import masood.sandooq.io.CustomersReader;
import masood.sandooq.model.Customers;

import java.util.ArrayList;
import java.util.List;

public class Main {


    //    ArrayList[] peopleTransactions = new ArrayList[20];
    List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        Main obj = new Main();
        CustomersReader.readCustomers();
        obj.transactions = TransactionsFileUtility.readAllFiles();
//        obj.process(statementDescription);

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//TODO: وقتی که می خواهم که یک خط در بین خطوط زیر اضافه کنم، باید همه اندیس ها را باز نویسی کنم. این خوب نیست
//        NextTransaction[] nextTransactions = new NextTransaction[12];
        List<NextTransaction> nextTransactions = NextTransactionFileUtility.readFile();

        for (NextTransaction nextTransaction : nextTransactions) {
            System.out.println(nextTransaction.toString());

            for (Transaction transaction : obj.transactions) {
                if ((transaction.getCustomer() != null) && (nextTransaction.customer == transaction.getCustomer())) {
//                    System.out.println("customer match");
                    if (transaction.getTransactionType() == nextTransaction.transactionType) {
//                        System.out.println("tt match");
                        for (int i = 1; i <= nextTransaction.count; i++) {
//                            System.out.println("hurra");
                            System.out.println(obj.nextTransaction(transaction, i));
                        }
                        break;
                    }
                } else {
//                    System.out.println(transaction.getCustomerFromName());
                }
            }
            System.out.println();
            System.out.println("-------------------------------------------");
        }
    }

    private String nextTransaction(Transaction transaction, int i) {
        String newDescription;
        int month;

        switch (transaction.getTransactionType()) {
            case MEMBERSHIP_FEE:
                newDescription = transaction.getTransactionDesc();
                month = transaction.getMonthNumber();
                //TODO: موحدی شامل زیر رشته دی است. برای همین برای استخراج ماه دی به مشکل می خورم. راه زیر به صورت موقت نوشته شده است. با راه درست جایگزین کنید.
                newDescription = newDescription.replace(
                        " " + Month.nextMonth(month, 0) + " ",
                        " " + Month.nextMonth(month, i) + " ");
                if (month + i > 12) {
                    newDescription = newDescription.replace(Integer.toString(transaction.getYear()), Integer.toString(transaction.getYear() + 1));
                }
                return newDescription.trim();

            case INSTALLMENT:
                newDescription = transaction.getTransactionDesc();
                month = transaction.getMonthNumber();
                newDescription = newDescription.replace(
                        " " + Month.nextMonth(month, 0) + " ",
                        " " + Month.nextMonth(month, i) + " ");
                if (month + i > 12) {
                    newDescription = newDescription.replace(Integer.toString(transaction.getYear()), Integer.toString(transaction.getYear() + 1));
                }
                if (transaction.getInstallmentOrder() + i <= 10) {
                    newDescription = newDescription.replace(Order.nextOrder(transaction.getInstallmentOrder(), 0),
                            Order.nextOrder(transaction.getInstallmentOrder(), i));
                } else {
                    return "Loan is zero";
                }
                return newDescription.trim();
        }
        return "Invalid transaction";
    }

}
