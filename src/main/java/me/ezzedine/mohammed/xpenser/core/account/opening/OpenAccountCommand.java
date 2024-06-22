package me.ezzedine.mohammed.xpenser.core.account.opening;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public abstract class OpenAccountCommand {

    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private CurrencyCode currencyCode;

    @NonNull
    private BigDecimal budgetInitialAmount;

    @NonNull
    private Date timestamp;
}
