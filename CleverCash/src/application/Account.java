package application;

import java.time.LocalDate;

/**
 * The Account class represents a financial account with a name, opening date, 
 * and an opening balance. It provides access to account details through getter methods.
 */
public class Account {

    /** The name of the account. */
    private String name;

    /** The date when the account was opened. */
    private LocalDate openingDate;

    /** The opening balance of the account. */
    private double openingBalance;

    /**
     * Constructs an Account with the specified name, opening date, and opening balance.
     *
     * @param name           The name of the account.
     * @param openingDate    The date the account was opened.
     * @param openingBalance The initial balance of the account.
     */
    public Account(String name, LocalDate openingDate, double openingBalance) {
        this.name = name;
        this.openingDate = openingDate;
        this.openingBalance = openingBalance;
    }

    /**
     * Returns the name of the account.
     *
     * @return The name of the account.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the opening date of the account.
     *
     * @return The opening date of the account.
     */
    public LocalDate getOpeningDate() {
        return openingDate;
    }

    /**
     * Returns the opening balance of the account.
     *
     * @return The opening balance of the account.
     */
    public double getOpeningBalance() {
        return openingBalance;
    }
}
