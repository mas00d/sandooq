package masood.sandooq.model;

import java.util.List;

public class Customers {
    private static Customers instance;
    private final Customer[] customers;

    public static void createInstance(List<String> customerFileLines) {
        if (instance == null) {
            instance = new Customers(customerFileLines);
        } else {
            throw new RuntimeException("The Customers instance was created ago");
        }
    }

    public static Customers getInstance() {
        if (instance == null) {
            throw new RuntimeException("The Customers instance must be created from file before call this method");
        } else {
            return instance;
        }

    }

    private Customers(List<String> customerFileLines) {
        this.customers = new Customer[customerFileLines.size()];

        for (int i = 0; i < customerFileLines.size(); i++) {
            String[] tokens = customerFileLines.get(i).split(",");
            if (isValidCustomerId(tokens[0], i) && isValidCustomerName(tokens[1], i)) {
                customers[i] = new Customer(tokens[0], tokens[1]);
            } else {
                throw new RuntimeException("Exception in Customer File data");
            }
        }
    }

    public Customer[] getCustomers() {
        return customers;
    }

    public Customer getCustomerFromName(String name) {
        name = name.trim();
        for (Customer customer : customers) {
            if (customer.getName().equals(name)) {
                return customer;
            }
        }
        return null;
    }

    public Customer getCustomerFromId(String id) {
        id = id.trim();
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }

    private boolean isValidCustomerName(String name, int customersCount) {
        for (int i = 0; i < customersCount; i++) {
            if (customers[i].getName().equals(name.trim())) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidCustomerId(String id, int customersCount) {
        for (int i = 0; i < customersCount; i++) {
            if (customers[i].getId().equals(id.trim())) {
                return false;
            }
        }
        return true;
    }

}
