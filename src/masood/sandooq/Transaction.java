package masood.sandooq;


import masood.sandooq.model.Customer;
import masood.sandooq.model.Customers;

import java.util.Objects;

public class Transaction {

    public static final String MEMBERSHIP_FEE = "حق عضويت";
    public static final String INSTALLMENT = "قسط";
    public static final String LOAN_PAYING = "دريافت وام";


    private final String transactionRaw;
    private final String transactionDesc;
    private final TransactionType transactionType;
    private final Customer customer;
    private int monthNumber;
    private int year;
    private int installmentOrder;
    private int loanPayingOrder;

    public Transaction(String transactionRaw) {
        this.transactionRaw = transactionRaw;
        this.transactionDesc = calculateTransactionDesc(transactionRaw);

        if (!TransactionDescValidator.isValid(transactionDesc)) {
            throw new RuntimeException();
        }
        this.customer = evaluateCustomer(transactionDesc);

        if (this.customer == null) {
            this.transactionType = TransactionType.OTHER;
        } else {
            this.transactionType = evaluateType(transactionDesc);
        }
    }


    private String calculateTransactionDesc(String transactionRaw) {
        String[] tokens = transactionRaw.split(",");
        return tokens[tokens.length - 1].trim();
    }


/*
    private boolean isOtherTransaction(String transactionDesc) {
        return transactionDesc.contains("-");
    }
*/


    private TransactionType evaluateType(String transactionDesc) {
        if (transactionDesc.contains(MEMBERSHIP_FEE)) {
            processMemberShipTransaction(transactionDesc);
            return TransactionType.MEMBERSHIP_FEE;
        } else if (transactionDesc.contains(LOAN_PAYING)) {
            return TransactionType.LOAN_PAYING;
        } else if (transactionDesc.contains(INSTALLMENT)) {
            //todo: مثال نقض: بازپرداخت قرض کوتاه مدت از صندوق قسط 2 از 2
            processInstallmentTransaction(transactionDesc);
            return TransactionType.INSTALLMENT;
        } else {
            throw new RuntimeException("unknown type");
        }
    }

    private Customer evaluateCustomer(String transactionDesc) {
        int dashIndex = transactionDesc.indexOf("-");
        if (dashIndex < 0) {
            return null;
        }
        String name = transactionDesc.substring(0, dashIndex).trim();
        System.out.println("customer name = " + name);
        return Customers.getInstance().getCustomerFromName(name);
    }


    private void processMemberShipTransaction(String transactionDesc) {
        //todo: check correctness
        String[] tokens = transactionDesc.split(" ");
        String monthName = tokens[tokens.length - 2];

        this.monthNumber = Month.getMonthNumber(monthName);
        this.year = Integer.parseInt(tokens[tokens.length - 1]);
    }


    private void processInstallmentTransaction(String transactionDesc) {
        //todo: check correctness
        String[] tokens = transactionDesc.split(" ");
        String monthName = tokens[tokens.length - 2];

        this.monthNumber = Month.getMonthNumber(monthName);
        this.year = Integer.parseInt(tokens[tokens.length - 1]);

        this.installmentOrder = Order.getOrder(transactionDesc);

        int ind = transactionDesc.indexOf(TransactionDescValidator.LOAN);
        String digitChar = transactionDesc.substring(ind + 4, ind + 5);
        try {
            loanPayingOrder = Integer.parseInt(digitChar);
        } catch (NumberFormatException e) {
            loanPayingOrder = -1;
        }
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public String getTransactionRaw() {
        return transactionRaw;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public int getYear() {
        return year;
    }

    public int getInstallmentOrder() {
        return installmentOrder;
    }

    public int getLoanPayingOrder() {
        return loanPayingOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        //I use contains instead of equals, because some unicode unexpected characters added to transactionRaw!!!
        return transactionRaw.contains(that.transactionRaw) || that.transactionRaw.contains(transactionRaw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionRaw);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionDesc='" + transactionDesc + '\'' +
                '}';
    }
}
