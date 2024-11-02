package application.database;

import application.model.TransactionBean;
import application.model.ScheduledTransactionBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * TransactionDatabase manages database operations for transactions and scheduled transactions.
 * It includes methods to add, retrieve, and delete transactions from the SQLite database.
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
     * Constructor that initializes the database by creating the transactions,
     * scheduled transactions, and transaction types tables if they do not exist.
     */
    public TransactionDatabase() {
        createTransactionTable();
        createScheduledTransactionTable();
        createTransactionTypeTable();
        // Do not clear transaction types here
    }

    /**
     * Creates the 'transactions' table if it does not exist.
     */
    private void createTransactionTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS transactions (
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
     * Creates the 'transaction_types' table if it does not exist.
     */
    private void createTransactionTypeTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS transaction_types (
                type TEXT UNIQUE NOT NULL
            );
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            // No default transaction types are inserted
            // Do not clear transaction types here
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new transaction type to the 'transaction_types' table.
     * @param type the name of the transaction type to add.
     */
    public void addTransactionType(String type) {
        String sql = "INSERT INTO transaction_types (type) VALUES (?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a transaction type exists in the 'transaction_types' table.
     * @param type the transaction type to check.
     * @return true if the transaction type exists, false otherwise.
     */
    public boolean transactionTypeExists(String type) {
        String sql = "SELECT COUNT(*) FROM transaction_types WHERE type = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all transaction types from the 'transaction_types' table.
     * @return an ObservableList of transaction type names.
     */
    public ObservableList<String> getAllTransactionTypes() {
        ObservableList<String> types = FXCollections.observableArrayList();
        String sql = "SELECT type FROM transaction_types ORDER BY type ASC";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                types.add(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    /**
     * Retrieves a map of transaction types to the total amount spent for each type.
     * @return a Map with transaction type names as keys and total amounts as values.
     */
    public Map<String, Double> getTransactionTypeAmounts() {
        Map<String, Double> dataMap = new HashMap<>();
        String sql = "SELECT transactionType, SUM(paymentAmount) as totalAmount FROM transactions GROUP BY transactionType";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String type = rs.getString("transactionType");
                double amount = rs.getDouble("totalAmount");
                dataMap.put(type, amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataMap;
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
     * If a duplicate schedule name is detected, it throws an SQLException.
     *
     * @param scheduledTransaction the ScheduledTransactionBean object containing scheduled transaction details.
     * @throws SQLException if a database access error occurs or a duplicate schedule name exists.
     */
    public void addScheduledTransaction(ScheduledTransactionBean scheduledTransaction) throws SQLException {
        String sql = "INSERT INTO scheduled_transactions (scheduleName, account, transactionType, frequency, dueDate, paymentAmount) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, scheduledTransaction.getScheduleName());
            pstmt.setString(2, scheduledTransaction.getAccount());
            pstmt.setString(3, scheduledTransaction.getTransactionType());
            pstmt.setString(4, scheduledTransaction.getFrequency());
            pstmt.setInt(5, scheduledTransaction.getDueDate());
            pstmt.setDouble(6, scheduledTransaction.getPaymentAmount());

            // Execute the insertion
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // Re-throw the exception to be handled by the caller
            throw e;
        }
    }

    /**
     * Retrieves all transactions from the 'transactions' table, sorted by transactionDate in descending order.
     * @return an ObservableList of TransactionBean objects representing each transaction.
     */
    public ObservableList<TransactionBean> getAllTransactions() {
        ObservableList<TransactionBean> transactions = FXCollections.observableArrayList();
        String sql = "SELECT * FROM transactions ORDER BY transactionDate DESC"; // Sorting by date in descending order

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
     * Retrieves all scheduled transactions from the 'scheduled_transactions' table, sorted by ROWID in descending order.
     * @return an ObservableList of ScheduledTransactionBean objects representing each scheduled transaction.
     */
    public ObservableList<ScheduledTransactionBean> getAllScheduledTransactions() {
        ObservableList<ScheduledTransactionBean> scheduledTransactions = FXCollections.observableArrayList();
        String sql = "SELECT * FROM scheduled_transactions ORDER BY ROWID DESC"; // Sorting by the latest addition at the top

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
     * Deletes a transaction from the 'transactions' table based on specific fields.
     * @param transaction the TransactionBean to be deleted.
     */
    public void deleteTransaction(TransactionBean transaction) {
        String sql = "DELETE FROM transactions WHERE account = ? AND transactionType = ? AND transactionDate = ? AND transactionDescription = ? AND paymentAmount = ? AND depositAmount = ?";

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
     * Deletes a scheduled transaction from the 'scheduled_transactions' table based on schedule name.
     * @param scheduledTransaction the ScheduledTransactionBean to be deleted.
     */
    public void deleteScheduledTransaction(ScheduledTransactionBean scheduledTransaction) {
        String sql = "DELETE FROM scheduled_transactions WHERE scheduleName = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, scheduledTransaction.getScheduleName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
