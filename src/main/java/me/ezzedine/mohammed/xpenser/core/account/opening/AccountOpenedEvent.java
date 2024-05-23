package me.ezzedine.mohammed.xpenser.core.account.opening;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountOpenedEvent {
    private String id;
    private String name;
}