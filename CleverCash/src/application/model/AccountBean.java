package application.model;

import java.time.LocalDate;

public class AccountBean {
    private String name;
    private LocalDate openingDate;
    private double openingBalance;

    public AccountBean(String name, LocalDate openingDate, double openingBalance) {
        this.name = name;
        this.openingDate = openingDate;
        this.openingBalance = openingBalance;
    }

    public String getName() {
        return name;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public double getOpeningBalance() {
        return openingBalance;
    }
}
