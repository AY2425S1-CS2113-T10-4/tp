package seedu.duke.Parser;

import seedu.duke.Command.BudgetManager;
import seedu.duke.Command.CategoryManager;
import seedu.duke.Command.ExpenseManager;
import seedu.duke.Command.TrackerData;
import seedu.duke.UI;

public class Parser {
    private final ExpenseManager expenseManager;
    private final CategoryManager categoryManager;
    private final BudgetManager budgetManager;
    private final UI ui;

    public Parser(ExpenseManager expenseManager, CategoryManager categoryManager, BudgetManager budgetManager, UI ui) {
        this.expenseManager = expenseManager;
        this.categoryManager = categoryManager;
        this.budgetManager = budgetManager;
        this.ui = ui;
    }

    public boolean parseCommand(String input, TrackerData trackerData) {
        input = input.trim();

        if (input.startsWith("add-expense")) {
            expenseManager.addExpenseRequest(input, expenseManager, trackerData);
        } else if (input.startsWith("add-category")) {
            categoryManager.addCategory(trackerData, input);
        } else if (input.startsWith("delete-expense")) {
            expenseManager.deleteExpenseRequest(input, expenseManager, trackerData);
        } else if (input.startsWith("tag-expense")) {
            expenseManager.tagExpense(trackerData, input);
        } else if (input.equalsIgnoreCase("view-budget")) {
            budgetManager.viewBudget(trackerData);
        } else if (input.startsWith("set-budget")) {
            budgetManager.setBudgetLimitRequest(input, budgetManager, trackerData);
        } else if (input.startsWith("view-expenses")) {
            expenseManager.viewExpensesByCategory(trackerData);
        } else if (input.equalsIgnoreCase("toggle-reset")) {
            budgetManager.toggleAutoReset();
        } else if (input.equalsIgnoreCase("bye")) {
            ui.printExitMessage();
            return true;
        } else {
            ui.printParserInvalidInput();
        }

        return false;
    }
}