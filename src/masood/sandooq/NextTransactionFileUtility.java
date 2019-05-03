package masood.sandooq;

import masood.sandooq.model.Customers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NextTransactionFileUtility {

    public static List<NextTransaction> readFile() {
        List<NextTransaction> result = new ArrayList<>();

        File nextTransactionsFile = new File(TransactionsFileUtility.INPUT_FOLDER + "\\next.txt");

        Scanner in;

        try {
            in = new Scanner(nextTransactionsFile);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String str;
        while (in.hasNext()) {
            str = in.nextLine();
            String[] tokens = str.split(",");
            result.add(new NextTransaction(Customers.getInstance().getCustomerFromId(tokens[0]),
                    TransactionType.valueOf(tokens[1].trim()),
                    Integer.parseInt(tokens[2].trim())));
        }
        return result;
    }
}
