package com.example.restapi.performance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.restapi.service.DeustoStreamService;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class RendimientoApiTest {
    @Autowired
    private DeustoStreamService deustoStreamService;

    // Configuración para generar un reporte en HTML
    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/perf-report.html"))
            .build();

     @BeforeEach
    void setup() {
        // Configuraciones previas opcionales antes de cada test si se requiere
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, percentiles = "95:300ms")
    void testGetAllPeliculasPerformance() {
        deustoStreamService.getAllPeliculas();
    }
  
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, percentiles = "95:300ms")
    void testGetAllSeriesPerformance() {
        deustoStreamService.getAllSeries();
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 10000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 50, percentiles = "95:300ms")
    void testGetAllUsuariosPerformance() {
        deustoStreamService.getAllUsuarios();
    }


}

