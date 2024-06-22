package me.ezzedine.mohammed.xpenser.api.account.opening;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.ezzedine.mohammed.xpenser.core.currency.CurrencyCode;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class OpenAccountApiRequest {

    @NotNull
    private String name;

    @NotNull
    private CurrencyCode currency;

    @NotNull
    private BigDecimal initialAmount;
}
