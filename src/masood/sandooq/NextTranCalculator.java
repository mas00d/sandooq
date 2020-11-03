package masood.sandooq;

public class NextTranCalculator {
    private static final String AMOUNT_CURRENCY = " تومان";

    public static String[] nextTransaction(Transaction transaction, int i) {
        String newDescription = "";
        String amount = "";
        int month;

        switch (transaction.getTransactionType()) {
            case MEMBERSHIP_FEE:
                newDescription = transaction.getTransactionDesc();
                month = transaction.getMonthNumber();
                //TODO: موحدی شامل زیر رشته دی است. برای همین برای استخراج ماه دی به مشکل می خورم. راه زیر به صورت موقت نوشته شده است. با راه درست جایگزین کنید.
                newDescription = newDescription.replace(
                        " " + Month.nextMonth(month, 0) + " ",
                        " " + Month.nextMonth(month, i) + " ");
                if (month + i > 12) {
                    newDescription = newDescription.replace(Integer.toString(transaction.getYear()), Integer.toString(transaction.getYear() + 1));
                }
                amount = "20" + AMOUNT_CURRENCY;
                break;

            case INSTALLMENT:
                newDescription = transaction.getTransactionDesc();
                month = transaction.getMonthNumber();
                newDescription = newDescription.replace(
                        " " + Month.nextMonth(month, 0) + " ",
                        " " + Month.nextMonth(month, i) + " ");
                if (month + i > 12) {
                    newDescription = newDescription.replace(Integer.toString(transaction.getYear()), Integer.toString(transaction.getYear() + 1));
                }

                int installmentCount = 10;
                switch (transaction.getLoanPayingOrder()) {
                    case 4:
                        installmentCount = 15;
                        amount = "200" + AMOUNT_CURRENCY;
                        break;
                    case 3:
                        installmentCount = 10;
                        amount = "140" + AMOUNT_CURRENCY;
                        break;
                }
                if (transaction.getInstallmentOrder() + i <= installmentCount) {
                    newDescription = newDescription.replace(Order.nextOrder(transaction.getInstallmentOrder(), 0),
                            Order.nextOrder(transaction.getInstallmentOrder(), i));
                } else {
                    return new String[]{"Loan is zero"};
                }
        }
        return new String[]{newDescription.trim(), amount};
    }
}
