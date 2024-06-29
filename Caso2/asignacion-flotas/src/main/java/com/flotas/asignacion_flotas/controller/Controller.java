package com.flotas.asignacion_flotas.controller;

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

import com.flotas.asignacion_flotas.domain.VehicleRouteAssignment;
import com.flotas.asignacion_flotas.domain.Route;

@RestController
@RequestMapping("/routeAssignment")
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    @Autowired
    private SolverManager<VehicleRouteAssignment, UUID> solverManager;
    private AtomicReference<VehicleRouteAssignment> currentProblem = new AtomicReference<>();

    @PostMapping("/solve")
    public VehicleRouteAssignment solve(@RequestBody VehicleRouteAssignment problem) {
        UUID problemId = UUID.randomUUID();
        currentProblem.set(problem);
        long startTime = System.currentTimeMillis();
        // Iniciar la resolución del problema
        SolverJob<VehicleRouteAssignment, UUID> solverJob = solverManager.solve(problemId, problem,
                bestSolution -> {
                    long timeTaken = System.currentTimeMillis() - startTime;
                    logger.info("Time taken to find better solution: " + timeTaken + "ms");
                    logger.info("New best solution found:");
                    // CODIGO PARA IMPRIMIR LAS RUTAS ASIGNADAS A LOS VEHICULOS
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
        VehicleRouteAssignment solution;
        try {
            // Esperar a que se resuelva el problema
            solution = solverJob.getFinalBestSolution();
            currentProblem.set(solution);
            // CODIGO AÑADIDO
            // Se imprime por pantalla cada asignacion de ruta a vehiculo
            // que rompa una restriccion dura
            if (solution.getScore().getHardScore() < 0) {
                logger.warn("Hard constraints broken in the solution:");
                for (Route route1 : solution.getRouteList()) {
                    if (route1.getSpecialEquipmentNeeded() && !route1.getVehicle().isSpecialEquipment()) {
                        logger.warn("Route " + route1.getId() + " assigned to " + route1.getVehicle().getId()
                                + " needs special equipment");
                    }
                    for (Route route2 : solution.getRouteList()) {
                        if (route1.getId() < route2.getId() && route1.getVehicle().equals(route2.getVehicle())) {
                            logger.warn("Vehicle " + route1.getVehicle().getId() + " assigned to both " + route1.getId()
                                    + " and " + route2.getId());
                        }
                    }
                }
            }
            // FIN CODIGO AÑADIDO
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }
        return solution;
    }

    @PostMapping("/update")
    public VehicleRouteAssignment solveUpdateAsignacion(@RequestBody VehicleRouteAssignment update) {
        VehicleRouteAssignment current = currentProblem.get();
        if (current == null) {
            throw new IllegalStateException("Initial problem not solved yet.");
        }

        // Se añaden las nuevas rutas y vehículos al problema
        current.getRouteList().addAll(update.getRouteList());
        current.getVehicleList().addAll(update.getVehicleList());

        long startTime = System.currentTimeMillis(); // Declare the startTime variable
        UUID problemId = UUID.randomUUID();
        SolverJob<VehicleRouteAssignment, UUID> solverJob = solverManager.solve(problemId, current, bestSolution -> {
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.println("Time taken to find better solution: " + timeTaken + "ms");
            currentProblem.set(bestSolution);
        });
        VehicleRouteAssignment solution;
        try {
            solution = solverJob.getFinalBestSolution();
            currentProblem.set(solution);
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }
        return solution;
    }

}