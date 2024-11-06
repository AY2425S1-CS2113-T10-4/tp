//@@author glenda-1506
package seedu.duke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseManager {

    //@@author kq2003
    public void addExpenseRequest(String input, ExpenseManager expenseManager, TrackerData trackerData) {
        try {
            String[] parts = input.split(" ");
            String name = null;
            double amount = 0;
            String category = null;

            for (String part : parts) {
                if (part.startsWith("n/")) {
                    name = part.substring(2).trim();
                } else if (part.startsWith("a/")) {
                    amount = Double.parseDouble(part.substring(2).trim());
                } else if (part.startsWith("c/")) {
                    category = part.substring(2).trim();
                }
            }

            if (name == null || amount == 0) {
                System.out.println("Invalid input! Please provide name, amount, and category.");
                return;
            }

            expenseManager.addExpense(trackerData, name, amount, category);
        } catch (Exception e) {
            System.out.println("Error parsing the input. Please use the correct format for add-expense commands.");
        }
    }

    /**
     * Adds a new expense with the specified name, amount, and category.
     *
     * It checks if the specified category exists in the tracker. If it does not exist,
     * the method creates a new category with the specified name and adds it to the list of categories.
     * Then, it creates a new expense with the given name, amount, and category and adds it to the list of expenses.
     * A message is displayed to confirm the addition of the expense and, if applicable, the new category.
     *
     * @param name         The name of the expense item.
     * @param amount       The amount of the expense.
     * @param categoryName The name of the category to which the expense belongs.
     */
    public void addExpense(TrackerData trackerData, String name, double amount, String categoryName) {
        List<Expense> expenses = trackerData.getExpenses();
        List<Category> categories = trackerData.getCategories();

        String formattedCategoryName = Format.formatInput(categoryName.trim());
        Category existingCategory = null;
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(formattedCategoryName)) {
                existingCategory = category;
                break;
            }
        }
        if (existingCategory == null) {
            existingCategory = new Category(formattedCategoryName);
            categories.add(existingCategory);
            System.out.println("Category '" + formattedCategoryName + "' added successfully.");
        }
        Expense newExpense = new Expense(name, amount, existingCategory);
        expenses.add(newExpense);

        // update categories and expenses
        trackerData.setExpenses(expenses);
        trackerData.setCategories(categories);
        System.out.println("Added" + newExpense);
    }

    //@@author AdiMangalam
    public void deleteExpenseRequest(String input, ExpenseManager expenseManager, TrackerData trackerData) {
        try {
            String[] parts = input.split(" ");
            if (parts.length < 2 || !parts[1].startsWith("e/")) {
                System.out.println("Invalid input! Please provide an expense index to delete.");
                return;
            }
            int expenseIndex = Integer.parseInt(parts[1].substring(2).trim()) - 1; // 1-based index
            expenseManager.deleteExpense(trackerData, expenseIndex);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing the expense index. Please use the correct format.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Deletes an expense at the specified index in the expense list.
     *
     * This method checks if the provided index is within the valid range of the
     * expense list. If the index is invalid (out of bounds), an error message is displayed.
     * If the index is valid, it removes the expense at the specified index and
     * displays a message confirming the deletion.
     *
     * @param expenseIndex the 0-based index of the expense to delete
     */
    public void deleteExpense(TrackerData trackerData, int expenseIndex) {
        List<Expense> expenses = trackerData.getExpenses();

        if (expenseIndex < 0 || expenseIndex >= expenses.size()) {
            System.out.println("Invalid index. Unable to delete expense.");
            return;
        }
        Expense removedExpense = expenses.remove(expenseIndex);
        System.out.println("Deleted expense: " + removedExpense);
        trackerData.setExpenses(expenses);
    }

    //@@author MayfairMI6
    /**
     * Displays all expenses grouped by their respective categories.
     *
     * This method first checks if there are any expenses in the tracker. If no
     * expenses are present, it informs the user. If expenses exist, they are grouped
     * by their category and displayed in a formatted manner under each category.
     *
     * Each category is displayed once with its associated expenses listed under it.
     *
     */
    public void viewExpensesByCategory(TrackerData trackerData) {
        List<Expense> expenses = trackerData.getExpenses();

        if (expenses.isEmpty()) {
            System.out.println("No expenses to display.");
            return;
        }
        System.out.println("Expenses grouped by categories:");
        // Create a map to group expenses by their category
        Map<Category, List<Expense>> expensesByCategory = new HashMap<>();
        // Populate the map
        for (Expense expense : expenses) {
            Category category = expense.getCategory();
            if (!expensesByCategory.containsKey(category)) {
                expensesByCategory.put(category, new ArrayList<>());
            }
            expensesByCategory.get(category).add(expense);
        }
        // Display the expenses grouped by category
        for (Category category : expensesByCategory.keySet()) {
            System.out.println("Category: " + category);
            List<Expense> categoryExpenses = expensesByCategory.get(category);
            for (Expense expense : categoryExpenses) {
                System.out.println("  " + expense);
            }
        }
    }

    //@@author glenda-1506
    public void tagExpense(TrackerData trackerData, String input) {
        try {
            String[] parts = input.split(" ");
            int expenseIndex = -1;
            String category = null;
            for (String part : parts) {
                if (part.startsWith("e/")) {
                    expenseIndex = Integer.parseInt(part.substring(2).trim()) - 1; // 1-based index
                } else if (part.startsWith("c/")) {
                    category = part.substring(2).trim();
                }
            }
            if (expenseIndex < 0 || category == null) {
                System.out.println("Invalid input! Please provide an expense index and category.");
                return;
            }
            tagExpenseHelper(trackerData, expenseIndex, category);
        } catch (Exception e) {
            System.out.println("Error parsing the input. Please use correct format for tag expense commands.");
        }
    }

    private void tagExpenseHelper(TrackerData trackerData, int expenseIndex, String categoryName) {
        List<Expense> expenses = trackerData.getExpenses();
        List<Category> categories = trackerData.getCategories();

        String formattedCategoryName = Format.formatInput(categoryName.trim());
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(formattedCategoryName)) {
                Expense expense = expenses.get(expenseIndex);
                expense.setCategory(category);
                System.out.println("Tagged expense: " + expense);
                return;
            }
        }

        trackerData.setExpenses(expenses);
        System.out.println("Category '" + formattedCategoryName + "' does not exist.");
    }
}


