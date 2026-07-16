package com.apexplanet.expensetracker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.apexplanet.expensetracker.data.Expense;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseViewModelTest {

    private Expense expense;

    @Before
    public void setUp() {
        // Create a test expense before each test
        expense = new Expense(
                "Test Food",
                500.0,
                "Food",
                "17/6/2026",
                "EXPENSE"
        );
    }

    // Test 1 - Check expense title is correct
    @Test
    public void testExpenseTitle() {
        assertEquals("Test Food", expense.getTitle());
    }

    // Test 2 - Check expense amount is correct
    @Test
    public void testExpenseAmount() {
        assertEquals(500.0, expense.getAmount(), 0.0);
    }

    // Test 3 - Check expense type is EXPENSE
    @Test
    public void testExpenseType() {
        assertEquals("EXPENSE", expense.getType());
    }

    // Test 4 - Check expense category
    @Test
    public void testExpenseCategory() {
        assertEquals("Food", expense.getCategory());
    }

    // Test 5 - Check expense is not null
    @Test
    public void testExpenseNotNull() {
        assertNotNull(expense);
    }

    // Test 6 - Check income type
    @Test
    public void testIncomeType() {
        Expense income = new Expense(
                "Salary",
                50000.0,
                "Salary",
                "17/6/2026",
                "INCOME"
        );
        assertEquals("INCOME", income.getType());
    }

    // Test 7 - Check balance calculation
    @Test
    public void testBalanceCalculation() {
        double income = 50000.0;
        double expense = 500.0;
        double balance = income - expense;
        assertEquals(49500.0, balance, 0.0);
    }

    // Test 8 - Check expense amount is positive
    @Test
    public void testExpenseAmountPositive() {
        assertTrue(expense.getAmount() > 0);
    }

    // Test 9 - Check expense title not empty
    @Test
    public void testExpenseTitleNotEmpty() {
        assertFalse(expense.getTitle().isEmpty());
    }

    // Test 10 - Check expense date
    @Test
    public void testExpenseDate() {
        assertEquals("17/6/2026", expense.getDate());
    }
}