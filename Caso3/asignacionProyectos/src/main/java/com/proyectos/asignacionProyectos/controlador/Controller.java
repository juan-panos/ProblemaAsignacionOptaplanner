package com.proyectos.asignacionProyectos.controlador;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyectos.asignacionProyectos.dominio.AsignacionProyecto;

@RestController
@RequestMapping("/asignacionProyectos")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    @Autowired
    private SolverManager<AsignacionProyecto, UUID> solverManager;
    private AtomicReference<AsignacionProyecto> currentProblem = new AtomicReference<>();

    @PostMapping("/solve")
    public AsignacionProyecto solve(@RequestBody AsignacionProyecto problem) {
        UUID problemId = UUID.randomUUID();
        currentProblem.set(problem);
        long startTime = System.currentTimeMillis();
        // Submit the problem to start solving
        SolverJob<AsignacionProyecto, UUID> solverJob = solverManager.solve(problemId, problem,
                bestSolution -> {
                    long timeTaken = System.currentTimeMillis() - startTime;
                    logger.info("Time taken to find better solution: " + timeTaken + "ms");
                    logger.info("New best solution found:");
                    /*
                     * for (Route route : bestSolution.getRouteList()) {
                     * logger.info(
                     * "Route " + route.getId() + " assigned to " + route.getVehicle().getId() +
                     * " with cost "
                     * + route.getCost());
                     * }
                     */
                    currentProblem.set(bestSolution);
                });
        AsignacionProyecto solution;
        try {
            // Wait until the solving ends
            solution = solverJob.getFinalBestSolution();
            currentProblem.set(solution);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }
        return solution;
    }

    @PostMapping("/update")
    public AsignacionProyecto solveUpdateAsignacion(@RequestBody AsignacionProyecto update) {
        AsignacionProyecto current = currentProblem.get();
        if (current == null) {
            throw new IllegalStateException("Initial problem not solved yet.");
        }

        // Merge new tasks and employees with the current problem
        current.getEstudiantes().addAll(update.getEstudiantes());
        current.getProyectos().addAll(update.getProyectos());

        long startTime = System.currentTimeMillis(); // Declare the startTime variable
        UUID problemId = UUID.randomUUID();
        SolverJob<AsignacionProyecto, UUID> solverJob = solverManager.solve(problemId, current, bestSolution -> {
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.println("Time taken to find better solution: " + timeTaken + "ms");
            currentProblem.set(bestSolution);
        });
        AsignacionProyecto solution;
        try {
            solution = solverJob.getFinalBestSolution();
            currentProblem.set(solution);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }
        return solution;
    }

}
