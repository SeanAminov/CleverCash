package application.model;

import java.time.LocalDate;

/**
 * Represents a financial transaction, including details about the account,
 * transaction type, date, description, and amounts involved.
 */
public class TransactionBean {
    private String account;
    private String transactionType;
    private LocalDate transactionDate;
    private String transactionDescription;
    private Double paymentAmount;
    private Double depositAmount;

    /**
     * Constructs a new TransactionBean with the specified details.
     *
     * @param account               the name of the account associated with this transaction.
     * @param transactionType       the type of transaction (e.g., "expense", "income").
     * @param transactionDate       the date when the transaction occurred.
     * @param transactionDescription a description of the transaction.
     * @param paymentAmount         the payment amount (for expense transactions).
     * @param depositAmount         the deposit amount (for income transactions).
     */
    public TransactionBean(String account, String transactionType, LocalDate transactionDate,
                           String transactionDescription, Double paymentAmount, Double depositAmount) {
        this.account = account;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
        this.paymentAmount = paymentAmount;
        this.depositAmount = depositAmount;
    }

    // Getters and setters

    /**
     * Gets the account name associated with this transaction.
     *
     * @return the account name.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Sets the account name for this transaction.
     *
     * @param account the account name to set.
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * Gets the type of this transaction.
     *
     * @return the transaction type.
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the type of this transaction.
     *
     * @param transactionType the transaction type to set.
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * Gets the date of this transaction.
     *
     * @return the transaction date.
     */
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the date of this transaction.
     *
     * @param transactionDate the transaction date to set.
     */
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * Gets the description of this transaction.
     *
     * @return the transaction description.
     */
    public String getTransactionDescription() {
        return transactionDescription;
    }

    /**
     * Sets the description of this transaction.
     *
     * @param transactionDescription the description to set for the transaction.
     */
    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    /**
     * Gets the payment amount of this transaction.
     *
     * @return the payment amount.
     */
    public Double getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the payment amount of this transaction.
     *
     * @param paymentAmount the payment amount to set.
     */
    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the deposit amount of this transaction.
     *
     * @return the deposit amount.
     */
    public Double getDepositAmount() {
        return depositAmount;
    }

    /**
     * Sets the deposit amount of this transaction.
     *
     * @param depositAmount the deposit amount to set.
     */
    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }
}
