package com.flotas.asignacion_flotas.domain;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class VehicleRouteAssignment {

    @PlanningEntityCollectionProperty
    private List<Route> routeList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "vehicle")
    private List<Vehicle> vehicleList;

    @PlanningScore
    private HardSoftScore score;

    public VehicleRouteAssignment() {
    }

    public VehicleRouteAssignment(List<Route> routeList, List<Vehicle> vehicleList) {
        this.routeList = routeList;
        this.vehicleList = vehicleList;
    }

    // Getters y Setters
    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

}
