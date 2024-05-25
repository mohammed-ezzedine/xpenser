package me.ezzedine.mohammed.xpenser.core.account.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummary {
    private String id;
    private String name;
}
