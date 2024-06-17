package me.ezzedine.mohammed.xpenser.api.expense;

import me.ezzedine.mohammed.xpenser.api.account.ResourceUtils;
import me.ezzedine.mohammed.xpenser.api.serialization.SerializationConfiguration;
import me.ezzedine.mohammed.xpenser.api.serialization.YearMonthSerializer;
import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReport;
import me.ezzedine.mohammed.xpenser.core.expense.MonthlyReportStorage;
import me.ezzedine.mohammed.xpenser.utils.MonthlyReportUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Month;
import java.time.YearMonth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebFluxTest(MonthlyReportController.class)
@ContextConfiguration(classes = {
    YearMonthSerializer.class,
    SerializationConfiguration.class,
    MonthlyReportController.class,
    MonthlyReportApiMapperImpl.class
})
class MonthlyReportControllerIntegrationTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private MonthlyReportStorage storage;

    @Test
    @DisplayName("it should return the report correctly")
    void it_should_return_the_report_correctly() {
        when(storage.fetch(MonthlyReportUtils.MONTH)).thenReturn(Mono.just(MonthlyReportUtils.monthlyReport().build()));
        MonthlyReport firstMonthReport = mock(MonthlyReport.class);
        MonthlyReport lastMonthReport = mock(MonthlyReport.class);
        when(storage.fetchFirstMonthReport()).thenReturn(Mono.just(firstMonthReport));
        when(storage.fetchLastMonthReport()).thenReturn(Mono.just(lastMonthReport));
        when(firstMonthReport.getMonth()).thenReturn(YearMonth.of(2024, Month.JANUARY));
        when(lastMonthReport.getMonth()).thenReturn(YearMonth.of(2024, Month.AUGUST));

        client.get()
                .uri("/expenses/reports?month=%s".formatted(MonthlyReportUtils.MONTH))
                .exchange()
                .expectBody()
                .json(ResourceUtils.resourceAsString("expenses/report/api/report_response.json")
                        .replace("{FIRST_MONTH}", YearMonth.of(2024, Month.JANUARY).toString())
                        .replace("{LAST_MONTH}", YearMonth.of(2024, Month.AUGUST).toString())
                        .replace("{REPORT_MONTH}", MonthlyReportUtils.MONTH.toString())
                        .replace("\"{REPORT_INCOMING}\"", MonthlyReportUtils.INCOMING.toString())
                        .replace("\"{REPORT_EXPENSES}\"", MonthlyReportUtils.EXPENSES.toString())
                        .replace("\"{REPORT_TARGET}\"", MonthlyReportUtils.TARGET.toString())
                );
    }

}