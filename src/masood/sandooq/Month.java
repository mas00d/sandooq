package masood.sandooq;

public class Month {
    private static final String[] monthNames = new String[]{
            "فروردين",
            "ارديبهشت",
            "خرداد",
            "تير",
            "مرداد",
            "شهريور",
            "مهر",
            "آبان",
            "آذر",
            "دي",
            "بهمن",
            "اسفند"};

    public static int getMonthNumber(String monthName) {
        monthName = monthName.trim();
        for (int i = 0; i < monthNames.length; i++) {
            if (monthNames[i].equals(monthName)) {
                return i + 1;
            }
        }
        //todo: maybe is better throw exception instead of retutn -1.
        return -1;
    }

    public static String nextMonth(int monthNumber, int nextCount) {
        //TODO: This try-catch is for test. please delete it as soon as possible
        try {
            return monthNames[(monthNumber - 1 + nextCount) % 12];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException();
        }
    }
}
