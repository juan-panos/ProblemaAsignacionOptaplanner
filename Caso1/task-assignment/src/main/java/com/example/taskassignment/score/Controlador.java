package com.example.taskassignment.score;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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

import com.example.taskassignment.domain.Asignacion;
import com.example.taskassignment.domain.Tarea;

@RestController
@RequestMapping("/asignacionTarea")
public class Controlador {
    private static final Logger logger = LoggerFactory.getLogger(Controlador.class);
    @Autowired
    private SolverManager<Asignacion, UUID> solverManager;
    private AtomicReference<Asignacion> currentProblem = new AtomicReference<>();

    @PostMapping("/solve")
    public Asignacion solve(@RequestBody Asignacion problem) {
        UUID problemId = UUID.randomUUID();
        currentProblem.set(problem);
        // Get the current time
        long startTime = System.currentTimeMillis();
        // Submit the problem to start solving
        SolverJob<Asignacion, UUID> solverJob = solverManager.solve(problemId, problem,
                bestSolution -> {
                    long timeTaken = System.currentTimeMillis() - startTime;
                    logger.info("Time taken to find better solution: " + timeTaken + "ms");
                    logger.info("New best solution found:");
                    /*
                     * for (Tarea tarea : bestSolution.getListaTareas()) {
                     * logger.info("Tarea " + tarea.getId() + " assigned to " +
                     * tarea.getEmpleado().getNombre());
                     * }
                     */
                    currentProblem.set(bestSolution);
                });
        Asignacion solution;
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
    public Asignacion solveUpdateAsignacion(@RequestBody Asignacion update) {
        Asignacion current = currentProblem.get();
        if (current == null) {
            throw new IllegalStateException("Initial problem not solved yet.");
        }

        // Merge new tasks and employees with the current problem
        current.getListaTareas().addAll(update.getListaTareas());
        current.getListaEmpleados().addAll(update.getListaEmpleados());

        long startTime = System.currentTimeMillis(); // Declare the startTime variable
        UUID problemId = UUID.randomUUID();
        SolverJob<Asignacion, UUID> solverJob = solverManager.solve(problemId, current, bestSolution -> {
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.println("Time taken to find better solution: " + timeTaken + "ms");
            currentProblem.set(bestSolution);
        });
        Asignacion solution;
        try {
            solution = solverJob.getFinalBestSolution();
            currentProblem.set(solution);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }
        return solution;
    }

    /*
     * igjfig
     * ifjgifgj
     */
    @PostMapping("/updateIncremental")
    public Asignacion solveUpdateAsignacionIncremental(@RequestBody Asignacion update)
            throws InterruptedException, ExecutionException {
        Asignacion current = currentProblem.get();
        if (current == null) {
            throw new IllegalStateException("Initial problem not solved yet.");
        }

        // Merge new tasks and employees with the current problem
        current.getListaTareas().addAll(update.getListaTareas());
        current.getListaEmpleados().addAll(update.getListaEmpleados());

        UUID problemId = UUID.randomUUID();
        CompletableFuture<Asignacion> future = new CompletableFuture<>();
        solverManager.solveAndListen(problemId, (problemId_) -> current,
                (bestSolution) -> {
                    // Update the current problem with the new best solution
                    currentProblem.set(bestSolution);
                    future.complete(bestSolution);
                });

        // Wait for the solution to be ready and return it
        return future.get();
    }

}
