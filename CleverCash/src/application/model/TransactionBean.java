package application.model;

import java.util.Date;

public class TransactionBean {
    // Instance variables
    private String scheduleName;          // Name of the schedule
    private String transactionType;       // Type of transaction (e.g., credit, debit)
    private String frequency;             // Frequency of the transaction (e.g., weekly, monthly)
    private Date dueDate;                 // Due date for the transaction
    private double paymentAmount;          // Amount to be paid
    private String transactionDescription; // Description of the transaction
    private double depositAmount;          // Amount to be deposited

    
    // Default constructor
    public TransactionBean() {
    }
    
    // Constructor
    public TransactionBean(String scheduleName, String transactionType, String frequency, Date dueDate, double paymentAmount, String transactionDescription, double depositAmount) {
        this.scheduleName = scheduleName;
        this.transactionType = transactionType;
        this.frequency = frequency;
        this.dueDate = dueDate;
        this.paymentAmount = paymentAmount;
        this.transactionDescription = transactionDescription;
        this.depositAmount = depositAmount;
    }

    // Getters and Setters
    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

}
