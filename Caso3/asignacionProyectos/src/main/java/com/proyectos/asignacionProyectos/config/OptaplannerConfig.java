package com.proyectos.asignacionProyectos.config;

import java.util.UUID;

import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.solver.SolverConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.proyectos.asignacionProyectos.dominio.AsignacionProyecto;

@Configuration
public class OptaplannerConfig {

    @Bean
    public SolverManager<AsignacionProyecto, UUID> solverManager() {
        SolverConfig solverConfig = SolverConfig.createFromXmlResource("solverConfig.xml");
        return SolverManager.create(solverConfig);
    }
}
