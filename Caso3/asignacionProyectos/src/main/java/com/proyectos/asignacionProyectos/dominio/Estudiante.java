package com.proyectos.asignacionProyectos.dominio;

import java.util.Set;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Estudiante {
    private int id;
    private Set<Habilidad> habilidades;
    private int[] preferenciasProyectos;
    private int disponibilidad;
    private String turno;
    private int salarioMinimo;
    private String genero;
    private int[] cooperacionPrevia;

    @PlanningVariable(valueRangeProviderRefs = "proyectoRange")
    private Proyecto proyecto;

    // Constructor, getters y setters
    public Estudiante() {
    }

    public boolean contains(int id) {
        for (int i = 0; i < cooperacionPrevia.length; i++) {
            if (cooperacionPrevia[i] == id) {
                return true;
            }
        }
        return false;
    }

    public String getGenero() {
        return genero;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean tieneDisponibilidadPara() {
        return this.disponibilidad >= this.proyecto.getJornada();
    }

    public boolean coincideTurno() {
        return this.turno.equalsIgnoreCase("AMBOS") || this.proyecto.getTurno().equalsIgnoreCase("AMBOS")
                || this.turno.equalsIgnoreCase(this.proyecto.getTurno());
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
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

    public Set<Habilidad> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(Set<Habilidad> habilidades) {
        this.habilidades = habilidades;
    }

    public int[] getPreferenciasProyectos() {
        return preferenciasProyectos;
    }

    public void setPreferenciasProyectos(int[] preferenciasProyectos) {
        this.preferenciasProyectos = preferenciasProyectos;
    }

    public int getSalarioMinimo() {
        return salarioMinimo;
    }

    public void setSalarioMinimo(int salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }

    public int[] getCooperacionPrevia() {
        return cooperacionPrevia;
    }

    public void setCooperacionPrevia(int[] cooperacionPrevia) {
        this.cooperacionPrevia = cooperacionPrevia;
    }
}
