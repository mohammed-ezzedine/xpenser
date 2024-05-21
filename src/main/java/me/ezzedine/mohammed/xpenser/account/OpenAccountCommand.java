package me.ezzedine.mohammed.xpenser.account;

import lombok.*;

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
