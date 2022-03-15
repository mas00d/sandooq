package masood.sandooq.model;

public class Customer {
    private final String id;
    private final String name;
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

    public void addToTotalMembershipFee(int newMembershipFee) {
        this.totalMembershipFee += newMembershipFee;
    }

    public int getTotalReceivedLoan() {
        return totalReceivedLoan;
    }

    public void addToTotalReceivedLoan(int newReceivedLoan) {
        this.totalReceivedLoan += newReceivedLoan;
    }

    public int getTotalInstallment() {
        return totalInstallment;
    }

    public void addToTotalInstallment(int newInstallment) {
        this.totalInstallment += newInstallment;
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
