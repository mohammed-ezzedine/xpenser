package me.ezzedine.mohammed.xpenser.core.account.opening;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAccountCommand {
    @Nonnull
    private String id;
    @Nonnull
    private String name;
}
