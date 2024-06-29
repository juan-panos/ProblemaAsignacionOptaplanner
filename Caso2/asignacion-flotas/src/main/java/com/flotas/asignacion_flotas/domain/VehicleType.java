package com.flotas.asignacion_flotas.domain;

public class VehicleType {

    private String type;
    private int operationCost;
    private int capacity;
    private boolean ecoFriendly;

    public VehicleType() {
    }

    public VehicleType(String type, int operationCost, int capacity) {
        this.type = type;
        this.operationCost = operationCost;
        this.capacity = capacity;
    }

    // Getters y Setters
    public boolean isEcoFriendly() {
        return ecoFriendly;
    }

    public void setEcoFriendly(boolean ecoFriendly) {
        this.ecoFriendly = ecoFriendly;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOperationCost() {
        return operationCost;
    }

    public void setOperationCost(int operationCost) {
        this.operationCost = operationCost;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}
