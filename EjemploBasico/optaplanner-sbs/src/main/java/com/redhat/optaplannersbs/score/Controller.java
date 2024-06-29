package com.redhat.optaplannersbs.score;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.redhat.optaplannersbs.domain.AsignacionTurno;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CloudBalancingController
 */
// Marca la clase como un controlador REST.
@RestController
// Mapea las solicitudes a /cloud-balancing para este controlador.
@RequestMapping("/asignacion-turno")
public class Controller {

    // Inyecta automáticamente la instancia de SolverManager.
    @Autowired
    private SolverManager<AsignacionTurno, UUID> solverManager;

    // Define que este método responde a POST en /solve.
    @PostMapping("/solve")
    public AsignacionTurno solve(@RequestBody AsignacionTurno problem) {
        UUID problemId = UUID.randomUUID(); // Genera un ID único para el problema.
        // Envía el problema al solver y comienza la resolución.
        SolverJob<AsignacionTurno, UUID> solverJob = solverManager.solve(problemId, problem);
        AsignacionTurno solution;
        try {
            // Espera a que termine la solución y recupera el resultado.
            solution = solverJob.getFinalBestSolution();
        } catch (InterruptedException | ExecutionException e) {
            // Maneja las excepciones lanzadas durante la resolución.
            throw new IllegalStateException("Solving failed.", e);
        }
        return solution;
    }

}
