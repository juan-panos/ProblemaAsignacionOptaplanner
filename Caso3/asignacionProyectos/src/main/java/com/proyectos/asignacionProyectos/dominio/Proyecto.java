package com.proyectos.asignacionProyectos.dominio;

import java.util.Set;

public class Proyecto {
    private int id;
    private Set<Habilidad> requerimientos;
    private int capacidad;
    private int jornada;
    private String turno;
    private int sueldo;

    public Proyecto() {
    }

    public int getJornada() {
        return jornada;
    }

    public void setJornada(int jornada) {
        this.jornada = jornada;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Habilidad> getRequerimientos() {
        return requerimientos;
    }

    public void setRequerimientos(Set<Habilidad> requerimientos) {
        this.requerimientos = requerimientos;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getSueldo() {
        return sueldo;
    }

    public void setSueldo(int sueldo) {
        this.sueldo = sueldo;
    }
}
