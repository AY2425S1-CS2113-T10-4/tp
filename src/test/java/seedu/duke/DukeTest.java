package seedu.duke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Map;

class ExpenseTrackerTest {

    private ExpenseTracker tracker;

    @BeforeEach
    public void setUp() {
        tracker = new ExpenseTracker();
    }

    @Test
    public void testSetBudgetLimit_NewCategory() {
        tracker.setBudgetLimit("groceries", 200.0);
        Map<String, Budget> budgets = tracker.getBudgets();
        Budget groceriesBudget = budgets.get("groceries");
        assertNotNull(groceriesBudget, "The budget for groceries should not be null.");
        assertEquals(200.0, groceriesBudget.getLimit(), 
                "The budget limit for groceries should be 200.0.");
    }

    @Test
    public void testSetBudgetLimit_UpdateCategory() {
        tracker.setBudgetLimit("groceries", 200.0);
        tracker.setBudgetLimit("groceries", 300.0);
        Map<String, Budget> budgets = tracker.getBudgets();
        Budget groceriesBudget = budgets.get("groceries");
        assertNotNull(groceriesBudget, "The budget for groceries should not be null.");
        assertEquals(300.0, groceriesBudget.getLimit(), 
                "The updated budget limit for groceries should be 300.0.");
    }

    @Test
    public void testAddExpense() {
        tracker.setBudgetLimit("groceries", 200.0);
        tracker.addExpense("Milk", 50.0, "groceries");
        List<Expense> expenses = tracker.getExpenses();
        assertEquals(1, expenses.size(), "There should be 1 expense recorded.");
        Expense milkExpense = expenses.get(0);
        assertEquals("Milk", milkExpense.getName(), "The expense name should be Milk.");
        assertEquals(50.0, milkExpense.getAmount(), "The expense amount should be 50.0.");
        assertEquals("groceries", milkExpense.getCategory(), 
                "The expense category should be groceries.");
    }

    @Test
    public void testExceedBudgetWarning() {
        tracker.setBudgetLimit("groceries", 100.0);
        tracker.addExpense("Eggs", 50.0, "groceries");
        tracker.addExpense("Cheese", 60.0, "groceries");
        double totalExpenses = tracker.getTotalExpenseForCategory("groceries");
        Budget groceriesBudget = tracker.getBudgets().get("groceries");
        assertTrue(totalExpenses > groceriesBudget.getLimit(),
                "The total expenses for groceries should exceed the budget limit.");
    }}

class ExpenseTest {
    @Test
    public void expenseConstructor() {
        Category category = new Category("Food");
        Expense expense = new Expense("Lunch", 12.50, category);
        assertEquals("Lunch", expense.getName());
        assertEquals(12.50, expense.getAmount());
        assertEquals(category, expense.getCategory());
    }

    @Test
    public void formatAmountWholeNumber() {
        Category category = new Category("Groceries");
        Expense expense = new Expense("Apple", 3.0, category);
        assertEquals("$3", expense.formatAmount());
    }

    @Test
    public void formatAmountDecimalNumber() {
        Category category = new Category("Groceries");
        Expense expense = new Expense("Banana", 2.75, category);
        assertEquals("$2.75", expense.formatAmount());
    }

    @Test
    public void inputWithCategory() {
        Category category = new Category("Entertainment");
        Expense expense = new Expense("Movie", 12.5, category);
        assertEquals(" Item: Movie, Amount: $12.50, Category: Entertainment", expense.toString());
    }

    @Test
    public void inputWithoutCategory() {
        Expense expense = new Expense("Movie", 12.5, null);
        assertEquals(" Item: Movie, Amount: $12.50, Category: null", expense.toString());
    }

    @Test
    public void setCategorySuccess() {
        Category category = new Category("Entertainment");
        Expense expense = new Expense("Movie", 12.5, null);
        expense.setCategory(category);
        assertEquals("Entertainment", expense.getCategory().getName());
    }
}

class CategoryTest {
    @Test
    public void categoryConstructorSuccess() {
        Category category = new Category("Entertainment");
        assertEquals("Entertainment", category.getName());
    }

    @Test
    public void categoryNameSuccess() {
        Category category = new Category("Groceries");
        assertEquals("Groceries", category.toString());
    }

    @Test
    public void getNameSuccess() {
        Category category = new Category("Transport");
        assertNotNull(category.getName());
        assertEquals("Transport", category.getName());

    }
}
