package seedu.duke;

public class Category extends ExpenseTracker {
    private String name;

    public Category(String name) {
        this.name = name;
    }
//@@ author glenda-1506
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
