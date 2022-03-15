package masood.sandooq.accounting;

import masood.sandooq.Transaction;
import masood.sandooq.accounting.gl.NoCustomerTransaction;
import masood.sandooq.model.Customer;
import masood.sandooq.model.Customers;

import java.util.List;

public class Accounting {
    private final Transaction[] transactions;
    private final int INITIAL_BALANCE;

    public Accounting(List<Transaction> transactionsList) {
        transactions = transactionsList.toArray(new Transaction[0]);
        INITIAL_BALANCE = transactions[0].getBalance() - transactions[0].getAmount();
    }

    public boolean checkBalanceOfAllTransactions() {
        int balance = INITIAL_BALANCE;
        for (Transaction transaction : transactions) {
            if (transaction.getBalance() == balance + transaction.getAmount()) {
                balance = transaction.getBalance();
            } else {
                return false;
            }
        }
        return true;
    }

    public void iterateAllTransactions() {
        Customer customer;
        for (Transaction tr : transactions) {
            customer = tr.getCustomer();
            switch (tr.getTransactionType()) {
                case MEMBERSHIP_FEE -> customer.addToTotalMembershipFee(tr.getAmount());
                case LOAN_PAYING -> customer.addToTotalReceivedLoan(tr.getAmount());
                case INSTALLMENT -> customer.addToTotalInstallment(tr.getAmount());
                case KARMOZD, TEST_APP -> NoCustomerTransaction.getInstance().addKarmozdTr(tr);
                case INTEREST -> NoCustomerTransaction.getInstance().addInterestTr(tr);
                case NEUTRAL -> NoCustomerTransaction.getInstance().addNeutralTr(tr);
                default -> throw new RuntimeException("unknown type");
            }
        }
    }

    public void printSum() {
        int balance = INITIAL_BALANCE;
        for (Customer customer : Customers.getInstance().getCustomers()) {
            System.out.println(customer.getName() + ":");
            System.out.println("Membership :  " + String.format("%,d", customer.getTotalMembershipFee()));
            System.out.println("Loan       : " + String.format("%,d", customer.getTotalReceivedLoan()));
            System.out.println("Installment:  " + String.format("%,d", customer.getTotalInstallment()));
            int customerBal = customer.getTotalMembershipFee() + customer.getTotalReceivedLoan() + customer.getTotalInstallment();
            System.out.println("customerBal:  " + String.format("%,d", customerBal));
            System.out.println("-----------------------------------");
            balance += customerBal;
        }
        System.out.println("Karmozd  : " + NoCustomerTransaction.getInstance().getKarmozds());
        System.out.println("Interest : " + NoCustomerTransaction.getInstance().getInterests());
        System.out.println("Neutral  : " + NoCustomerTransaction.getInstance().getNeutral());
        System.out.println("-----------------------------------");
        balance += NoCustomerTransaction.getInstance().getKarmozds();
        balance += NoCustomerTransaction.getInstance().getInterests();
        balance += NoCustomerTransaction.getInstance().getNeutral();
        System.out.println("Balance : " + balance);
        System.out.println("Real    : " + transactions[transactions.length - 1].getBalance());
    }
}
