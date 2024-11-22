package application.model;

import java.time.LocalDate;

/**
 * Represents a financial transaction, including details about the account,
 * transaction type, date, description, and amounts involved.
 */
public class TransactionBean {
    private int id; // New field to store the unique identifier
    private String account;
    private String transactionType;
    private LocalDate transactionDate;
    private String transactionDescription;
    private Double paymentAmount;
    private Double depositAmount;

    /**
     * Constructs a new TransactionBean with the specified details.
     *
     * @param id                     the unique identifier of the transaction.
     * @param account                the name of the account associated with this transaction.
     * @param transactionType        the type of transaction (e.g., "expense", "income").
     * @param transactionDate        the date when the transaction occurred.
     * @param transactionDescription a description of the transaction.
     * @param paymentAmount          the payment amount (for expense transactions).
     * @param depositAmount          the deposit amount (for income transactions).
     */
    public TransactionBean(int id, String account, String transactionType, LocalDate transactionDate,
                           String transactionDescription, Double paymentAmount, Double depositAmount) {
        this.id = id;
        this.account = account;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
        this.paymentAmount = paymentAmount;
        this.depositAmount = depositAmount;
    }

    /**
     * Constructs a new TransactionBean without id (for adding new transactions).
     *
     * @param account                the name of the account associated with this transaction.
     * @param transactionType        the type of transaction (e.g., "expense", "income").
     * @param transactionDate        the date when the transaction occurred.
     * @param transactionDescription a description of the transaction.
     * @param paymentAmount          the payment amount (for expense transactions).
     * @param depositAmount          the deposit amount (for income transactions).
     */
    public TransactionBean(String account, String transactionType, LocalDate transactionDate,
                           String transactionDescription, Double paymentAmount, Double depositAmount) {
        this(-1, account, transactionType, transactionDate, transactionDescription, paymentAmount, depositAmount);
    }

    // Getters and setters

    public int getId() {
        return id;
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

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }
}
