package me.ezzedine.mohammed.xpenser.core.account.opening;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OpenLoanAccountCommand extends OpenAccountCommand {
}
