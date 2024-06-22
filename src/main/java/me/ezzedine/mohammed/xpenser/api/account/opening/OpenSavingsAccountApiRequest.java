package me.ezzedine.mohammed.xpenser.api.account.opening;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OpenSavingsAccountApiRequest extends OpenAccountApiRequest {
}
