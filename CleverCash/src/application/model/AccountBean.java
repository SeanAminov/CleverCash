package application.model;

import java.time.LocalDate;

/**
 * Represents a bank account with details such as account name, opening date, and balance.
 */
public class AccountBean {
    private String name;
    private LocalDate openingDate;
    private double openingBalance;

    /**
     * Constructs a new AccountBean with the specified details.
     *
     * @param name           the name of the account.
     * @param openingDate    the date the account was opened.
     * @param openingBalance the initial balance of the account.
     */
    public AccountBean(String name, LocalDate openingDate, double openingBalance) {
        this.name = name;
        this.openingDate = openingDate;
        this.openingBalance = openingBalance;
    }

    /**
     * Gets the name of the account.
     *
     * @return the account name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the date when the account was opened.
     *
     * @return the opening date.
     */
    public LocalDate getOpeningDate() {
        return openingDate;
    }

    /**
     * Gets the initial balance of the account.
     *
     * @return the opening balance.
     */
    public double getOpeningBalance() {
        return openingBalance;
    }
}
