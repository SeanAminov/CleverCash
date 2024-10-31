package application.model;

/**
 * Represents a scheduled transaction with details such as schedule name, account,
 * transaction type, frequency, due date, and payment amount.
 */
public class ScheduledTransactionBean {
    private String scheduleName;
    private String account;
    private String transactionType;
    private String frequency;
    private int dueDate;
    private Double paymentAmount;

    /**
     * Constructs a new ScheduledTransactionBean with the specified details.
     *
     * @param scheduleName   the name of the schedule.
     * @param account        the name of the account associated with this scheduled transaction.
     * @param transactionType the type of transaction (e.g., "expense", "income").
     * @param frequency      the frequency of the scheduled transaction (e.g., "Monthly").
     * @param dueDate        the due date for the scheduled transaction (day of the month).
     * @param paymentAmount  the payment amount for the scheduled transaction.
     */
    public ScheduledTransactionBean(String scheduleName, String account, String transactionType, String frequency,
                                    int dueDate, Double paymentAmount) {
        this.scheduleName = scheduleName;
        this.account = account;
        this.transactionType = transactionType;
        this.frequency = frequency;
        this.dueDate = dueDate;
        this.paymentAmount = paymentAmount;
    }

    // Getters and setters

    /**
     * Gets the name of the schedule.
     *
     * @return the schedule name.
     */
    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * Sets the name of the schedule.
     *
     * @param scheduleName the schedule name to set.
     */
    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    /**
     * Gets the account name associated with this scheduled transaction.
     *
     * @return the account name.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Sets the account name for this scheduled transaction.
     *
     * @param account the account name to set.
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * Gets the type of this scheduled transaction.
     *
     * @return the transaction type.
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the type of this scheduled transaction.
     *
     * @param transactionType the transaction type to set.
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * Gets the frequency of this scheduled transaction.
     *
     * @return the frequency.
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets the frequency of this scheduled transaction.
     *
     * @param frequency the frequency to set.
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * Gets the due date of this scheduled transaction (day of the month).
     *
     * @return the due date.
     */
    public int getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date of this scheduled transaction (day of the month).
     *
     * @param dueDate the due date to set.
     */
    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Gets the payment amount for this scheduled transaction.
     *
     * @return the payment amount.
     */
    public Double getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the payment amount for this scheduled transaction.
     *
     * @param paymentAmount the payment amount to set.
     */
    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
