package application.database;

import application.model.AccountBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

/**
 * The AccountDatabase class handles all interactions with the SQLite database
 * for storing and retrieving account information.
 */
public class AccountDatabase {

    private static final String DB_URL = "jdbc:sqlite:accounts.db"; // SQLite database URL

    /**
     * Constructor to initialize the database. It creates the 'accounts' table if it doesn't exist.
     */
    public AccountDatabase() {
        createTableIfNotExists();
    }

    /**
     * Establishes a connection to the SQLite database.
     *
     * @return A {@link Connection} to the SQLite database.
     * @throws SQLException if a database access error occurs.
     */
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Creates the 'accounts' table if it doesn't exist.
     */
    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS accounts (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE,
                    openingDate TEXT NOT NULL,
                    openingBalance REAL NOT NULL
                );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to create accounts table.");
            e.printStackTrace();
        }
    }

    /**
     * Adds a new account to the database.
     *
     * @param account The {@link AccountBean} to be added.
     */
    public void addAccount(AccountBean account) {
        String sql = "INSERT INTO accounts (name, openingDate, openingBalance) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getName());
            pstmt.setString(2, account.getOpeningDate().toString());
            pstmt.setDouble(3, account.getOpeningBalance());

            pstmt.executeUpdate();
            System.out.println("Account added: " + account.getName());

        } catch (SQLException e) {
            System.err.println("Failed to add account: " + account.getName());
            e.printStackTrace();
        }
    }

    /**
     * Checks if an account with the given name already exists in the database.
     *
     * @param name The name of the account to check.
     * @return True if the account name exists, false otherwise.
     */
    public boolean accountNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all accounts from the database, sorted by opening date in descending order.
     *
     * @return An {@link ObservableList} of {@link AccountBean} containing all accounts.
     */
    public ObservableList<AccountBean> getAllAccounts() {
        ObservableList<AccountBean> accounts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM accounts ORDER BY openingDate DESC";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AccountBean account = new AccountBean(
                    rs.getString("name"),
                    LocalDate.parse(rs.getString("openingDate")),
                    rs.getDouble("openingBalance")
                );
                accounts.add(account);
            }

        } catch (SQLException e) {
            System.err.println("Failed to retrieve accounts.");
            e.printStackTrace();
        }

        return accounts;
    }

    /**
     * Deletes all accounts from the database. Useful for testing or resetting data.
     */
    public void deleteAllAccounts() {
        String sql = "DELETE FROM accounts";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("All accounts have been deleted.");

        } catch (SQLException e) {
            System.err.println("Failed to delete accounts.");
            e.printStackTrace();
        }
    }
}