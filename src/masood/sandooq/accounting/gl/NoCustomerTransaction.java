package masood.sandooq.accounting.gl;

import masood.sandooq.Transaction;

import java.util.ArrayList;
import java.util.List;

public class NoCustomerTransaction {
    private static NoCustomerTransaction instance;
    private final List<Transaction> karmozdTrs = new ArrayList<>();
    private final List<Transaction> interestTrs = new ArrayList<>();
    private final List<Transaction> neutralTrs = new ArrayList<>();
    private int karmozds = 0;
    private int interests = 0;
    private int neutral = 0;

    private NoCustomerTransaction() {
    }

    public static NoCustomerTransaction getInstance() {
        if (null == instance) {
            instance = new NoCustomerTransaction();
        }
        return instance;
    }

    public int getKarmozds() {
        return karmozds;
    }

    public List<Transaction> getKarmozdTrs() {
        return karmozdTrs;
    }

    public int getInterests() {
        return interests;
    }

    public List<Transaction> getInterestTrs() {
        return interestTrs;
    }

    public int getNeutral() {
        return neutral;
    }

    public List<Transaction> getNeutralTrs() {
        return neutralTrs;
    }

    public void addKarmozdTr(Transaction newKarmozdTr) {
        this.karmozdTrs.add(newKarmozdTr);
        this.karmozds += newKarmozdTr.getAmount();
    }

    public void addInterestTr(Transaction newInterestTr) {
        this.interestTrs.add(newInterestTr);
        this.interests += newInterestTr.getAmount();
    }

    public void addNeutralTr(Transaction newNeutralTr) {
        this.neutralTrs.add(newNeutralTr);
        this.neutral += newNeutralTr.getAmount();
    }
}
