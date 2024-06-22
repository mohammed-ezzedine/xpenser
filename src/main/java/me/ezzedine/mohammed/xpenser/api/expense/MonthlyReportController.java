package me.ezzedine.mohammed.xpenser.api.expense;

import lombok.RequiredArgsConstructor;
import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReportStorage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@RestController
@RequestMapping("expenses/reports")
@RequiredArgsConstructor
public class MonthlyReportController {

    private final MonthlyReportStorage storage;
    private final MonthlyReportApiMapper mapper;

    @GetMapping
    public Mono<MonthlyReportApiResponse> fetchMonthlyReport(@RequestParam YearMonth month) {
        return storage.fetch(month)
                .zipWith(storage.fetchFirstMonthReport())
                .zipWith(storage.fetchLastMonthReport())
                .map(result -> MonthlyReportApiResponse.builder()
                        .report(mapper.map(result.getT1().getT1()))
                        .first(result.getT1().getT2().getMonth().toString())
                        .last(result.getT2().getMonth().toString())
                        .build());
    }

    @GetMapping("latest")
    public Mono<MonthlyReportApiResponse> fetchLatestMonthlyReport() {
        return storage.fetchLastMonthReport()
                .zipWith(storage.fetchFirstMonthReport())
                .map(result -> MonthlyReportApiResponse.builder()
                        .report(mapper.map(result.getT1()))
                        .first(result.getT2().getMonth().toString())
                        .last(result.getT1().getMonth().toString())
                        .build());
    }
}
