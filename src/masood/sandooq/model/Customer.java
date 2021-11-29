package masood.sandooq.model;

public class Customer {
    private final String id;
    private final String name;

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
}
