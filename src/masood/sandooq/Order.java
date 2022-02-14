package masood.sandooq;

public class Order {
    private static final String[] ORDERS = new String[]{
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
            "سيزدهم",
            "چهاردهم",
            "پانزدهم",
    };

    public static int getOrder(String transactionDesc) {
        for (int i = ORDERS.length - 1; i >= 0; i--) {
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
        if (newOrder >= 15) {
            return "null";
        }
        return ORDERS[newOrder];
    }

    public static boolean isValidOrder(String maybeOrderName) {
        for (String order : ORDERS) {
            if (order.equals(maybeOrderName)) {
                return true;
            }
        }
        return false;
    }
}
