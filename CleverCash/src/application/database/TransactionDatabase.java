package application.database;

import application.model.TransactionBean;
import application.model.ScheduledTransactionBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

/**
 * TransactionDatabase manages database operations for transactions and scheduled transactions.
 * It includes methods to add, retrieve, and clear transactions from the SQLite database.
 */
public class TransactionDatabase {
    // Path to the SQLite database file
    private static final String DB_URL = "jdbc:sqlite:src/application/database/transactions.db";

    /**
     * Establishes a connection to the SQLite database.
     * @return a Connection object to the database.
     * @throws SQLException if a database access error occurs.
     */
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Constructor that initializes the database by creating the transactions and
     * scheduled transactions tables if they do not exist.
     */
    public TransactionDatabase() {
        createTransactionTable();
        createScheduledTransactionTable();
    }

    /**
     * Creates the 'transactions' table if it does not exist.
     */
    private void createTransactionTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                account TEXT NOT NULL,
                transactionType TEXT NOT NULL,
                transactionDate TEXT NOT NULL,
                transactionDescription TEXT,
                paymentAmount REAL,
                depositAmount REAL
            );
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the 'scheduled_transactions' table if it does not exist.
     */
    private void createScheduledTransactionTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS scheduled_transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                scheduleName TEXT UNIQUE NOT NULL,
                account TEXT NOT NULL,
                transactionType TEXT NOT NULL,
                frequency TEXT NOT NULL,
                dueDate INTEGER NOT NULL,
                paymentAmount REAL NOT NULL
            );
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new transaction to the 'transactions' table.
     * @param transaction the TransactionBean object containing transaction details.
     */
    public void addTransaction(TransactionBean transaction) {
        String sql = "INSERT INTO transactions (account, transactionType, transactionDate, transactionDescription, paymentAmount, depositAmount) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getAccount());
            pstmt.setString(2, transaction.getTransactionType());
            pstmt.setString(3, transaction.getTransactionDate().toString());
            pstmt.setString(4, transaction.getTransactionDescription());
            pstmt.setDouble(5, transaction.getPaymentAmount() != null ? transaction.getPaymentAmount() : 0);
            pstmt.setDouble(6, transaction.getDepositAmount() != null ? transaction.getDepositAmount() : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new scheduled transaction to the 'scheduled_transactions' table.
     * @param scheduledTransaction the ScheduledTransactionBean object containing scheduled transaction details.
     */
    public void addScheduledTransaction(ScheduledTransactionBean scheduledTransaction) {
        String sql = "INSERT INTO scheduled_transactions (scheduleName, account, transactionType, frequency, dueDate, paymentAmount) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, scheduledTransaction.getScheduleName());
            pstmt.setString(2, scheduledTransaction.getAccount());
            pstmt.setString(3, scheduledTransaction.getTransactionType());
            pstmt.setString(4, scheduledTransaction.getFrequency());
            pstmt.setInt(5, scheduledTransaction.getDueDate());
            pstmt.setDouble(6, scheduledTransaction.getPaymentAmount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all transactions from the 'transactions' table.
     * @return an ObservableList of TransactionBean objects representing each transaction.
     */
    public ObservableList<TransactionBean> getAllTransactions() {
        ObservableList<TransactionBean> transactions = FXCollections.observableArrayList();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TransactionBean transaction = new TransactionBean(
                    rs.getString("account"),
                    rs.getString("transactionType"),
                    LocalDate.parse(rs.getString("transactionDate")),
                    rs.getString("transactionDescription"),
                    rs.getDouble("paymentAmount"),
                    rs.getDouble("depositAmount")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Retrieves all scheduled transactions from the 'scheduled_transactions' table.
     * @return an ObservableList of ScheduledTransactionBean objects representing each scheduled transaction.
     */
    public ObservableList<ScheduledTransactionBean> getAllScheduledTransactions() {
        ObservableList<ScheduledTransactionBean> scheduledTransactions = FXCollections.observableArrayList();
        String sql = "SELECT * FROM scheduled_transactions";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ScheduledTransactionBean scheduledTransaction = new ScheduledTransactionBean(
                    rs.getString("scheduleName"),
                    rs.getString("account"),
                    rs.getString("transactionType"),
                    rs.getString("frequency"),
                    rs.getInt("dueDate"),
                    rs.getDouble("paymentAmount")
                );
                scheduledTransactions.add(scheduledTransaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scheduledTransactions;
    }

    /**
     * Clears all records from the 'transactions' table.
     */
    public void clearAllTransactions() {
        String sql = "DELETE FROM transactions";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears all records from the 'scheduled_transactions' table.
     */
    public void clearAllScheduledTransactions() {
        String sql = "DELETE FROM scheduled_transactions";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a scheduled transaction with the given schedule name exists in the database.
     * @param scheduleName the name of the scheduled transaction to check.
     * @return true if a scheduled transaction with the given name exists, false otherwise.
     */
    public boolean scheduledTransactionNameExists(String scheduleName) {
        String sql = "SELECT COUNT(*) FROM scheduled_transactions WHERE scheduleName = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, scheduleName);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
