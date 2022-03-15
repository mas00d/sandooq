package masood.sandooq;


import masood.sandooq.model.Customer;
import masood.sandooq.model.Customers;

import java.util.Objects;

public class Transaction implements Comparable<Transaction> {

    public static final String MEMBERSHIP_FEE = "حق عضويت";
    public static final String MEMBERSHIP_FEE_2 = "کمک به صندوق";
    public static final String INSTALLMENT = "قسط";
    public static final String LOAN_PAYING = "دريافت وام";
    public static final String[] KARMOZD = {"کارمزد", "آبونمان"};
    public static final String[] TEST_APP = {"فرشاد"};
    public static final String INSTALLMENT_All = "بازپرداخت کامل";

    public static final int KARMOZD_BOUNDARY = -50000;


    private final String transactionRaw;
    private final String transactionDesc;
    private final TransactionType transactionType;
    private final int amount;
    private final int balance;
    private final Customer customer;
    private final String descDate;
    private final String descTime;
    private int monthNumber;
    private int year;
    private int installmentOrder;
    private int loanPayingOrder;

    public Transaction(String transactionRaw) {
        this.transactionRaw = transactionRaw;

        String[] tokens = transactionRaw.split(",");
        transactionDesc = tokens[tokens.length - 1].trim();

        descDate = tokens[1].trim();
        if (!descDate.matches("\\d{4}/\\d{2}/\\d{2}")) {
            throw new RuntimeException("date bad format");
        }
        descTime = tokens[2].trim();
        if (!descTime.matches("\\d{2}:\\d{2}:\\d{2}")) {
            throw new RuntimeException("time bad format");
        }
        int creditAmount = Integer.parseInt(tokens[3].trim());
        if (0 != creditAmount) {
            amount = creditAmount;
        } else {
            amount = Integer.parseInt(tokens[4].trim()) * -1;
        }

        balance = Integer.parseInt(tokens[5].trim());

        if (!TransactionDescValidator.isValid(transactionDesc)) {
            //TODO: add KARMOZD transaction to TransactionDescValidator.isValid method instead of here
            if (amount < 0 && amount >= KARMOZD_BOUNDARY) {
                for (String karmozd : KARMOZD) {
                    if (transactionRaw.contains(karmozd)) {
                        this.customer = null;
                        this.transactionType = TransactionType.KARMOZD;
                        return;
                    }
                }
                for (String testAppStr : TEST_APP) {
                    if (transactionRaw.contains(testAppStr)) {
                        this.customer = null;
                        this.transactionType = TransactionType.TEST_APP;
                        return;
                    }
                }
            }
            throw new RuntimeException();
        }
        this.customer = evaluateCustomer(transactionDesc);

        if (this.customer == null) {
            this.transactionType = TransactionDescValidator.evaluateTransactionType(transactionDesc);
        } else {
            this.transactionType = evaluateType(transactionDesc);
        }
    }


/*
    private boolean isOtherTransaction(String transactionDesc) {
        return transactionDesc.contains("-");
    }
*/


    private TransactionType evaluateType(String transactionDesc) {
        if (transactionDesc.contains(MEMBERSHIP_FEE) || transactionDesc.contains(MEMBERSHIP_FEE_2)) {
            processMemberShipTransaction(transactionDesc);
            return TransactionType.MEMBERSHIP_FEE;
        } else if (transactionDesc.contains(LOAN_PAYING)) {
            return TransactionType.LOAN_PAYING;
        } else if (transactionDesc.contains(INSTALLMENT)) {
            //todo: مثال نقض: بازپرداخت قرض کوتاه مدت از صندوق قسط 2 از 2
            processInstallmentTransaction(transactionDesc);
            return TransactionType.INSTALLMENT;
        } else if (transactionDesc.contains(INSTALLMENT_All)) {
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

    public int getAmount() {
        return amount;
    }

    public int getBalance() {
        return balance;
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
    public int compareTo(Transaction o) {
        if (this.equals(o)) {
            return 0;
        }
        if (this.descDate.equals(o.descDate)) {
            if (this.descTime.equals(o.descTime)) {
                if (this.amount + o.balance == this.balance) {
                    return 1;
                } else if (o.amount + this.balance == o.balance) {
                    return -1;
                } else {
                    throw new RuntimeException("can not define order");
                }
            } else {
                return this.descTime.compareTo(o.descTime);
            }
        } else {
            return this.descDate.compareTo(o.descDate);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return this.transactionType == that.transactionType
                && this.amount == that.amount
                && this.balance == that.balance
                && (Objects.equals(this.customer, that.customer))
                && this.monthNumber == that.monthNumber
                && this.year == that.year
                && this.installmentOrder == that.installmentOrder
                && this.loanPayingOrder == that.loanPayingOrder
                && this.descDate.equals(that.descDate)
                && this.descTime.equals(that.descTime);
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
