package masood.sandooq;

public class Order {
    private static final String[] ORDERS = new String[] {
            "اول",
            "دوم",
            "سوم",
            "چهارم",
            "پنجم",
            "ششم",
            "هفتم",
            "هشتم",
            "نهم",
            "دهم",
            "يازدهم",
            "دوازدهم",
    };

    public static int getOrder(String transactionDesc) {
        for (int i = 0; i < ORDERS.length; i++) {
            if (transactionDesc.contains(ORDERS[i])) {
                return i + 1;
            }
        }
        return -1;
    }

    public static String getOrder(int i) {
        return ORDERS[i];
    }

    public static String nextOrder(int order, int nextCount) {
        int newOrder = order - 1 + nextCount;
        if (newOrder >= 10) {
            return "null";
        }
        return ORDERS[newOrder];
    }

}
