package com.flotas.asignacion_flotas.domain;

public class Vehicle {

    private Long id;
    private VehicleType vehicleType;
    private boolean specialEquipment;

    public Vehicle() {
    }

    // isLargestVehicle
    public boolean isLarge() {
        return this.vehicleType.getType().equalsIgnoreCase("Camión grande");
    }
    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public boolean isSpecialEquipment() {
        return specialEquipment;
    }

    public void setSpecialEquipment(boolean specialEquipment) {
        this.specialEquipment = specialEquipment;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}