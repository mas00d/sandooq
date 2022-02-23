package masood.sandooq;

public class Month {
    private static final String[][] monthNames = new String[][]{
            {"فروردين"},
            {"ارديبهشت"},
            {"خرداد"},
            {"تير"},
            {"مرداد"},
            {"شهريور"},
            {"مهر"},
            {"آبان", "آّبان"},
            {"آذر"},
            {"دي"},
            {"بهمن"},
            {"اسفند"}};

    public static int getMonthNumber(String monthName) {
        monthName = monthName.trim();
        for (int i = 0; i < monthNames.length; i++) {
            for (int j = 0; j < monthNames[i].length; j++) {
                if (monthNames[i][j].equals(monthName)) {
                    return i + 1;
                }
            }
        }
        throw new RuntimeException(monthName + "is invalid");
    }

    public static String nextMonth(int monthNumber, int nextCount) {
        //TODO: This try-catch is for test. please delete it as soon as possible
        try {
            return monthNames[(monthNumber - 1 + nextCount) % 12][0];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException();
        }
    }

    public static boolean isValid(String maybeMonthName) {
        for (String[] monthName : monthNames) {
            for (String month : monthName) {
                if (month.equals(maybeMonthName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
