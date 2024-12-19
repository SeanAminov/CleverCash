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
     * @param scheduleName    the name of the schedule.
     * @param account         the name of the account associated with this scheduled transaction.
     * @param transactionType the type of transaction (e.g., "expense", "income").
     * @param frequency       the frequency of the scheduled transaction (e.g., "Monthly").
     * @param dueDate         the due date for the scheduled transaction (day of the month).
     * @param paymentAmount   the payment amount for the scheduled transaction.
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

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
