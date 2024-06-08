package me.ezzedine.mohammed.xpenser.core.account.budget;

import me.ezzedine.mohammed.xpenser.utils.CurrencyUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BudgetTest {

    @Test
    @DisplayName("when checking if can withdraw from a budget it should return true when the available amount is greater than the requested amount")
    void when_checking_if_can_withdraw_from_a_budget_it_should_return_true_when_the_available_amount_is_greater_than_the_requested_amount() {
        Budget budget = new Budget(CurrencyUtils.currency(), BigDecimal.valueOf(10));
        assertTrue(budget.canWithdraw(BigDecimal.valueOf(9.9)));
    }

    @Test
    @DisplayName("when checking if can withdraw from a budget it should return true when the available amount is equal to the requested amount")
    void when_checking_if_can_withdraw_from_a_budget_it_should_return_true_when_the_available_amount_is_equal_to_the_requested_amount() {
        Budget budget = new Budget(CurrencyUtils.currency(), BigDecimal.valueOf(10));
        assertTrue(budget.canWithdraw(BigDecimal.valueOf(10)));
    }

    @Test
    @DisplayName("when checking if can withdraw from a budget it should return false when the available amount is less than the requested amount")
    void when_checking_if_can_withdraw_from_a_budget_it_should_return_false_when_the_available_amount_is_less_than_the_requested_amount() {
        Budget budget = new Budget(CurrencyUtils.currency(), BigDecimal.valueOf(10));
        assertFalse(budget.canWithdraw(BigDecimal.valueOf(10.1)));
    }

}