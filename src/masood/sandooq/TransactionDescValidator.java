package masood.sandooq;

import masood.sandooq.model.Customers;

import java.util.function.Predicate;

public class TransactionDescValidator {
    private static final String BORROW_FROM_THE_FUND = "قرض کوتاه مدت از صندوق";
    private static final String RETURN_BORROW_FROM_THE_FUND = "بازپرداخت قرض کوتاه مدت از صندوق";
    private static final String FROM = "از";
    private static final String BANK_INTEREST = "سود علي الحساب";
    public  static final String LOAN = "وام";
    private static final String LOAN_PAYMENT = "دريافت وام سري";
    private static final String FIRST_INSTALLMENT = "قسط اول";
    private static final String WRONG_CREDIT = "واريز اشتباه";
    private static final String REVERT_WRONG_CREDIT = "برگشت واريز اشتباه";
    private static final String OPEN_ACCOUNT_COST = "ساير - برگشت هزينه افتتاح سپرده";
    private static final String OPEN_ACCOUNT_COST_2 = "ساير - هزار تومان هزينه افتتاح سپرده - پنجاه هزار تومان ته مانده سپرده براي واريزي ها به صندوق";
    public static final int LAST_LOAN = 5;

    private static final Predicate<String> validDescriptions = ((Predicate<String>) BANK_INTEREST::equals)
            .or(WRONG_CREDIT::equals)
            .or(REVERT_WRONG_CREDIT::equals)
            .or(BORROW_FROM_THE_FUND::equals)
            .or(OPEN_ACCOUNT_COST::equals)
            .or(OPEN_ACCOUNT_COST_2::equals)
            .or(TransactionDescValidator::isReturnBorrowFromTheFund)
            .or(TransactionDescValidator::isInstallment)
            .or(TransactionDescValidator::isInstallmentAll)
            .or(TransactionDescValidator::isMembershipFee)
            .or(TransactionDescValidator::isLoanPaying);

    public static boolean isValid(String transactionDesc) {
        return validDescriptions.test(transactionDesc);
    }

    private static boolean isReturnBorrowFromTheFund(String transactionDesc) {
        if (!transactionDesc.startsWith(RETURN_BORROW_FROM_THE_FUND)) {
            return false;
        }
        transactionDesc = transactionDesc.replace(RETURN_BORROW_FROM_THE_FUND, "").trim();
        if (transactionDesc.isEmpty()) {
            return true;
        }
        String[] tokens = transactionDesc.split(" ");
        if (tokens.length != 4) {
            return false;
        }
        if (!Transaction.INSTALLMENT.equals(tokens[0].trim())) {
            return false;
        }
        if (!FROM.equals(tokens[2].trim())) {
            return false;
        }
        int total;
        try {
            total = Integer.parseInt(tokens[3].trim());
            if ((total < 1) || (total > 2)) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        try {
            int cnt = Integer.parseInt(tokens[1].trim());
            if ((cnt < 1) || (cnt > total)) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isInstallment(String transactionDesc) {
        if (!startsWithCustomer(transactionDesc)) {
            return false;
        }
        transactionDesc = removeCustomerFromTransactionDesc(transactionDesc);

        String[] tokens = transactionDesc.split(" ");

        if (!Transaction.INSTALLMENT.equals(tokens[0].trim())) {
            return false;
        }
        if (!Order.isValidOrder(tokens[1].trim())) {
            return false;
        }
        String[] loanNameTokens = tokens[2].split("[-:]");
        if ((loanNameTokens.length > 2) && !loanNameTokens[2].isEmpty()) {
            return false;
        }
        if (!LOAN.equals(loanNameTokens[0])) {
            return false;
        }
        if ((loanNameTokens.length > 1) && !loanNameTokens[1].isEmpty()) {
            try {
                int loanOrder = Integer.parseInt(loanNameTokens[1]);
                if ((loanOrder < 1) || (loanOrder > 4)) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return isMonthAndYear(tokens[3].trim(), tokens[4].trim());
    }

    private static boolean isInstallmentAll(String transactionDesc) {
        if (!startsWithCustomer(transactionDesc)) {
            return false;
        }
        transactionDesc = removeCustomerFromTransactionDesc(transactionDesc);

        String[] tokens = transactionDesc.split(" ");

        if (!Transaction.INSTALLMENT_All.equals(tokens[0].trim() + " " + tokens[1].trim())) {
            return false;
        }
        String[] loanNameTokens = tokens[2].split("[-:]");
        if ((loanNameTokens.length > 2) && !loanNameTokens[2].isEmpty()) {
            return false;
        }
        if (!LOAN.equals(loanNameTokens[0])) {
            return false;
        }
        if ((loanNameTokens.length > 1) && !loanNameTokens[1].isEmpty()) {
            try {
                int loanOrder = Integer.parseInt(loanNameTokens[1]);
                if ((loanOrder < 1) || (loanOrder > 4)) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return isMonthAndYear(tokens[3].trim(), tokens[4].trim());
    }

    private static boolean isMembershipFee(String transactionDesc) {
        if (!startsWithCustomer(transactionDesc)) {
            return false;
        }
        transactionDesc = removeCustomerFromTransactionDesc(transactionDesc);

        if (!transactionDesc.startsWith(Transaction.MEMBERSHIP_FEE)) {
            return false;
        }
        transactionDesc = transactionDesc.replace(Transaction.MEMBERSHIP_FEE, "").trim();

        String[] tokens = transactionDesc.split(" ");
        return isMonthAndYear(tokens[0], tokens[1]);
    }

    private static boolean isLoanPaying(String transactionDesc) {
        if (!startsWithCustomer(transactionDesc)) {
            return false;
        }
        transactionDesc = removeCustomerFromTransactionDesc(transactionDesc);

        if (!transactionDesc.startsWith(LOAN_PAYMENT)) {
            return false;
        }
        transactionDesc = transactionDesc.replace(LOAN_PAYMENT, "").trim();

        String[] tokens = transactionDesc.split("-");

        int loanOrder = Order.getOrder(tokens[0]);
        if ((loanOrder < 1) || (loanOrder > LAST_LOAN)) {
            return false;
        }
        if (!isDate(tokens[1].trim())) {
            return false;
        }
        if (!tokens[2].trim().startsWith(FIRST_INSTALLMENT)) {
            return false;
        }
        tokens[2] = tokens[2].replace(FIRST_INSTALLMENT, "").trim();
        return isDate(tokens[2]);
    }

    private static boolean startsWithCustomer(String transactionDesc) {
        int dashIndex = transactionDesc.indexOf("-");
        if (dashIndex < 0) {
            return false;
        }
        String maybeCustomerName = transactionDesc.substring(0, dashIndex).trim();
        return Customers.getInstance().getCustomerFromName(maybeCustomerName) != null;
    }

    private static String removeCustomerFromTransactionDesc(String transactionDesc) {
        int dashIndex = transactionDesc.indexOf("-");
        return transactionDesc.substring(dashIndex + 1).trim();
    }

    private static boolean isDate(String maybeDate) {
        String[] tokens = maybeDate.split(" +");
        if (tokens.length != 3) {
            return false;
        }
        try {
            int day = Integer.parseInt(tokens[0]);
            if ((day < 1) || (day > 31)) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return isMonthAndYear(tokens[1], tokens[2]);
    }
    private static boolean isMonthAndYear(String maybeMonth, String maybeYear) {
        if (!Month.isValid(maybeMonth)) {
            maybeYear = Month.extractYear(maybeMonth);
            if (null == maybeYear) {
                return false;
            }
        }
        try {
            int year = Integer.parseInt(maybeYear);
            if ((year < 1395) || (year > 1400)) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
