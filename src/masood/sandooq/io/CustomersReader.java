package masood.sandooq.io;

import masood.sandooq.TransactionsFileUtility;
import masood.sandooq.model.Customers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomersReader {
    public static void readCustomers() {
        List<String> customersFileLines = new ArrayList<>();

        File customersFile = new File(TransactionsFileUtility.INPUT_FOLDER + "\\customers.txt");

        Scanner in;

        try {
            in = new Scanner(customersFile);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
            e.printStackTrace();
            throw new RuntimeException("Can not read from customers file");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Can not read from customers file");
        }
        String str;
        while (in.hasNext()) {
            str = in.nextLine();
            customersFileLines.add(str);
        }
        Customers.createInstance(customersFileLines);
    }
}
