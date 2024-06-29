package com.redhat.optaplannersbs.domain;

public class Trabajador {

    private int id;
    private String nombre;
    private int jornada;

    // Constructores, getters y setters
    public Trabajador() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getJornada() {
        return jornada;
    }

    public void setJornada(int jornada) {
        this.jornada = jornada;
    }

}
