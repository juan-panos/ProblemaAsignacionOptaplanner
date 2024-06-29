package com.flotas.asignacion_flotas.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Route {

    private Long id;
    private int demand;
    private int distance;
    private boolean specialEquipmentNeeded;
    private boolean isEco;

    @PlanningVariable(valueRangeProviderRefs = "vehicle")
    private Vehicle vehicle;

    public Route() {
    }

    public boolean isEco() {
        return isEco;
    }

    public void setEco(boolean isEco) {
        this.isEco = isEco;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public int getCost() {
        if (vehicle == null) {
            return 0;
        } else {
            return vehicle.getVehicleType().getOperationCost() * this.distance;
        }
    }

    public boolean getSpecialEquipmentNeeded() {
        return specialEquipmentNeeded;
    }

    public void setSpecialEquipmentNeeded(boolean specialEquipmentNeeded) {
        this.specialEquipmentNeeded = specialEquipmentNeeded;
    }
}