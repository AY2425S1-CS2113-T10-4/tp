//@@author glenda-1506
package seedu.spendswift;

import seedu.spendswift.command.Budget;
import seedu.spendswift.command.Category;
import seedu.spendswift.command.Expense;
import seedu.spendswift.command.TrackerData;
import seedu.spendswift.CurrencyConverter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public void saveData(TrackerData trackerData) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Budgets\n");
            for (Map.Entry<Category, Budget> entry : trackerData.getBudgets().entrySet()) {
                String categoryName = entry.getKey().getName();
                double budgetLimit = entry.getValue().getLimit();
                String homeCurrency= entry.getKey().gethomeCurrency();
                writer.write(categoryName + ", " + budgetLimit + "\n");
            }

            writer.write("Expenses\n");
            for (Expense expense : trackerData.getExpenses()) {
                String expenseName = expense.getName();
                double amount = expense.getAmount();
                String categoryName = expense.getCategory().getName();
                String originalCurrency = expense.getOriginalCurrency();
                String gethomeCurrency = expense.gethomeCurrency();
                String getConvertedAmount = expense.getConvertedAmount();
                writer.write(expenseName + ", " + amount + ", " + categoryName + "\n");
            }
        }
    }

    private Category loadCategory(TrackerData trackerData, String categoryName) {
        for (Category category : trackerData.getCategories()) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        for (Category category : trackerData.getBudgets().keySet()) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }

        Category newCategory = new Category(categoryName);
        trackerData.getCategories().add(newCategory);
        return newCategory;
    }

    public void loadData(TrackerData trackerData) throws IOException {
        UI ui = new UI();
        File file = new File(filePath);
        if (!file.exists()) {
            ui.printFileNotFound();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isBudgetSection = true;

            while ((line = reader.readLine()) != null) {
                if (line.equals("Budgets")) {
                    isBudgetSection = true;
                    continue;
                } else if (line.equals("Expenses")) {
                    isBudgetSection = false;
                    continue;
                }

                String[] parts = line.split(", ");
                if (isBudgetSection) {
                    String categoryName = parts[0];
                    double limit = Double.parseDouble(parts[1]);
                    Category category = new Category(categoryName);
                    Budget budget = new Budget(category, limit);
                    String homeCurrency=parts[2];
                    trackerData.getBudgets().put(category, budget,homeCurrency);
                } else {
                    String expenseName = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String categoryName = parts[2];
                    String originalCurrency = parts[3];
                    String homeCurrency = parts[4];
                    double convertedAmount  = CurrencyConverter.convert(amount,originalCurrency,homeCurrency);
                    Category category = loadCategory(trackerData, categoryName);
                    Expense expense = new Expense(expenseName, amount, category,originalCurrency, homeCurrency,convertedAmount);
                    trackerData.getExpenses().add(expense);
                }
            }
        }
        ui.printDataLoaded();
    }
}
