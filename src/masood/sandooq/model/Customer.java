package masood.sandooq.model;

import masood.sandooq.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String id;
    private final String name;
    private List<Transaction> membershipTrs = new ArrayList<>();
    private List<Transaction> receivedLoanTrs = new ArrayList<>();
    private List<Transaction> installmentTrs = new ArrayList<>();
    private int totalMembershipFee = 0;
    private int totalReceivedLoan = 0;
    private int totalInstallment = 0;

    Customer(String id, String name) {
        this.id = convertToAscii(id.trim());
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }

    private String convertToAscii(String input) {
        return input.replaceAll("[^A-Za-z_\\-]+", "");
    }

    public int getTotalMembershipFee() {
        return totalMembershipFee;
    }

    public void addMembershipFeeTr(Transaction tr) {
        membershipTrs.add(tr);
        this.totalMembershipFee += tr.getAmount();
    }

    public int getTotalReceivedLoan() {
        return totalReceivedLoan;
    }

    public void addReceivedLoanTr(Transaction tr) {
        receivedLoanTrs.add(tr);
        this.totalReceivedLoan += tr.getAmount();
    }

    public int getTotalInstallment() {
        return totalInstallment;
    }

    public void addInstallmentTr(Transaction tr) {
        this.installmentTrs.add(tr);
        this.totalInstallment += tr.getAmount();
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;

        Customer customer = (Customer) o;

        return getId().equals(customer.getId());
    }

    public boolean equalsIgnoreWhitespace(String o) {
        if (this.getName().equalsIgnoreCase(o)) {
            return true;
        }
        String that = o.replaceAll("\\s+", "").replaceAll("\u200C", "");
        return this.getName().replaceAll("\\s+", "").equals(that);
    }
}
