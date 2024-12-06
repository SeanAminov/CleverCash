package application.services;

import application.database.TransactionDatabase;
import application.model.ScheduledTransactionBean;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.util.List;

/**
 * The Alerts class checks for scheduled transactions due on the current day and notifies the user if any are found.
 */
public class Alerts {

    private final TransactionDatabase transactionDatabase = new TransactionDatabase();

    /**
     * Checks for scheduled transactions due today and shows an alert if any are found.
     * Call this method after the primary stage is shown (e.g., at the end of Main.start()).
     */
    public void showDueTodayNotifications() {
        LocalDate today = LocalDate.now();
        List<ScheduledTransactionBean> allScheduled = transactionDatabase.getAllScheduledTransactions();

        StringBuilder dueTodayList = new StringBuilder();
        for (ScheduledTransactionBean st : allScheduled) {
            if (st.getDueDate() == today.getDayOfMonth()) {
                dueTodayList.append("- ").append(st.getScheduleName())
                            .append(" (").append(st.getTransactionType()).append(", $")
                            .append(String.format("%.2f", st.getPaymentAmount())).append(")\n");
            }
        }

        if (dueTodayList.length() > 0) {
            // Show alert of scheduled transactions due today
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Scheduled Transactions Due Today");
                alert.setHeaderText("The following scheduled transactions are due today:");
                alert.setContentText(dueTodayList.toString());
                alert.showAndWait();
            });
        }
    }
}
