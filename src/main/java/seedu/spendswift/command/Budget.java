package seedu.spendswift.command;

import seedu.spendswift.Format;
import java.io.IOException;
import seedu.spendswift.currency;


//@@author MayFairMI6
public class Budget {
    private String homeCurrency;
    private CurrencyConverter currencyConverter;
    private Category category; // Private to prevent unauthorized access or changes
    private double limit; // Private to control modifications to the budget
    private TrackerData trackerData;
    public Budget(Category category, double limit,String homeCurrency, TrackerData trackerData) {
        this.category = category;
        this.limit = limit;
        this.trackerData= trackerData;
        this.homeCurrency= homeCurrency;
        try {
            this.currencyConverter = new CurrencyConverter(homeCurrency);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Category getCategory() {
        return category;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Budget limit cannot be negative.");
        }
        this.limit = limit;
    }

    public double getRemainingLimit() {
        double totalExpenses = trackerData.getExpenses().stream()
            .filter(e -> e.getCategory().equals(category))
            .mapToDouble(Expense::getAmount)
            .sum();
        return limit - totalExpenses;
    }

    @Override
 public String toString() {
        return "Budget for category '" + category + "' is " + Format.formatAmount(limit) + " " + homeCurrency;
    }
}
