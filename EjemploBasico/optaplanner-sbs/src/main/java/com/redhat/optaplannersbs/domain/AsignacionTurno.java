package com.redhat.optaplannersbs.domain;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class AsignacionTurno {

    // Lista de turnos a asignar, objetos de planificación.
    @PlanningEntityCollectionProperty
    private List<Turno> turnosList;

    // Datos del problema que no cambian durante la planificación.
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "trabajador") // Provee el rango de valores posibles para asignaciones.
    private List<Trabajador> trabajadoresList;

    // Campo que almacena la puntuación de la solución.
    @PlanningScore
    private HardSoftScore score;

    // Constructor vacío requerido por OptaPlanner.
    public AsignacionTurno() {
    }

    // Constructor que inicializa con listas de trabajadores y turnos.
    public AsignacionTurno(List<Turno> turnosList, List<Trabajador> trabajadoresList) {
        this.trabajadoresList = trabajadoresList;
        this.turnosList = turnosList;
    }

    public List<Turno> getTurnosList() {
        return turnosList;
    }

    public void setTurnosList(List<Turno> turnosList) {
        this.turnosList = turnosList;
    }

    public List<Trabajador> getTrabajadoresList() {
        return trabajadoresList;
    }

    public void setTrabajadoresList(List<Trabajador> trabajadoresList) {
        this.trabajadoresList = trabajadoresList;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

}