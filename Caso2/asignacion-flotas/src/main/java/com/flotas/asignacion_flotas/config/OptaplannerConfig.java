package com.flotas.asignacion_flotas.config;

import java.util.UUID;

import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.solver.SolverConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.flotas.asignacion_flotas.domain.VehicleRouteAssignment;

@Configuration
public class OptaplannerConfig {

    @Bean
    public SolverManager<VehicleRouteAssignment, UUID> solverManager() {
        SolverConfig solverConfig = SolverConfig.createFromXmlResource("solverConfig.xml");
        return SolverManager.create(solverConfig);
    }
}
