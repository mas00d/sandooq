package masood.sandooq;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TransactionsFileUtility {
    public static final String INPUT_FOLDER = "e:\\Masood\\Projects\\Sandooq\\Program\\input";


    public static List<Transaction> readAllFiles() {
        List<Transaction> result = new ArrayList<>();

        File[] inputFiles = getInputFiles();


        for (int i = inputFiles.length - 1; i >= 0; i--) {
            List<Transaction> partTransactions = readTransactions(inputFiles[i]);
            int deleteIndex = 0;
            assert partTransactions != null;
            for (; deleteIndex < partTransactions.size(); deleteIndex++) {
                if (!result.contains(partTransactions.get(deleteIndex))) {
                    break;
                }
            }
            result.addAll(partTransactions.subList(deleteIndex, partTransactions.size()));
        }

        return result;
    }


    /**
     * Oldest file must be named 01.txt, next 2.txt and so on.
     *
     * @return Sorted input files from oldest to newest.
     */
    private static File[] getInputFiles() {
        File[] result;
        File folder = new File(INPUT_FOLDER);

        result = folder.listFiles((dir, name) -> name.matches("\\d+\\.txt"));

        if (result != null) {
            Arrays.sort(result);
        }
        return result;
    }


    private static List<Transaction> readTransactions(File inputFile) {
        List<Transaction> result = new ArrayList<>();
        Scanner in;

        try {
            in = new Scanner(inputFile);
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
            result.add(new Transaction(str));
        }
        return result;
    }
}
