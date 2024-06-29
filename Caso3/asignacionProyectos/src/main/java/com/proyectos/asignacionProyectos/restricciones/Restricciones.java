package com.proyectos.asignacionProyectos.restricciones;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

//import com.proyectos.asignacionProyectos.dominio.Asignacion;
import com.proyectos.asignacionProyectos.dominio.Estudiante;
import com.proyectos.asignacionProyectos.dominio.Habilidad;

public class Restricciones implements ConstraintProvider {

        @Override
        public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
                return new Constraint[] {
                                // unicidadDeProyecto(constraintFactory),
                                salarioMinimo(constraintFactory),
                                capacidadDeProyecto(constraintFactory),
                                habilidadRequerida(constraintFactory),
                                requisitosDeHabilidades(constraintFactory),
                                disponibilidadDeTiempo(constraintFactory),
                                disponibilidadDeTurno(constraintFactory),
                                maximoAjusteDeTiempo(constraintFactory),
                                diversidadDeGenero(constraintFactory),
                                trabajoEnEquipoPrevio(constraintFactory),
                                preferenciaProyecto(constraintFactory)
                };
        }

        // RESTRICCIONES DURAS
        // CADA ESTUDIANTE SOLO PUEDE TENER UN PROYECTO
        /*
         * 
         * 
         * private Constraint unicidadDeProyecto(ConstraintFactory constraintFactory) {
         * return constraintFactory.forEach(Asignacion.class)
         * .groupBy(Asignacion::getEstudiante, ConstraintCollectors.count())
         * .filter((estudiante, count) -> count > 1)
         * .penalize("Unicidad de Proyecto", HardSoftScore.ONE_HARD,
         * (estudiante, count) -> count - 1);
         * }
         */

        // CAPACIDAD DEL PROYECYO
        private Constraint capacidadDeProyecto(ConstraintFactory constraintFactory) {
                return constraintFactory.forEach(Estudiante.class)
                                .groupBy(Estudiante::getProyecto, ConstraintCollectors.count())
                                .filter((proyecto, count) -> count > proyecto.getCapacidad())
                                .penalize("Capacidad de Proyecto", HardSoftScore.ONE_HARD,
                                                (proyecto, count) -> count - proyecto.getCapacidad());
        }

        // SALARIO MÍNIMO
        private Constraint salarioMinimo(ConstraintFactory constraintFactory) {
                return constraintFactory.forEach(Estudiante.class)
                                .filter(estudiante -> estudiante.getSalarioMinimo() > estudiante
                                                .getProyecto()
                                                .getSueldo())
                                .penalize("Salario Mínimo", HardSoftScore.ONE_HARD);
        }

        // HABILIDAD REQUERIDA POR ESTUDIANTE
        private Constraint habilidadRequerida(ConstraintFactory constraintFactory) {
                return constraintFactory.forEach(Estudiante.class)
                                .filter(estudiante -> estudiante.getHabilidades().stream()
                                                .noneMatch(habilidad -> estudiante.getProyecto().getRequerimientos()
                                                                .contains(habilidad)))
                                .penalize("Habilidad Requerida No Presente", HardSoftScore.ONE_HARD);
        }

        // CUBRIMIENTO COMPLETO DE HABILIDADES POR EL GRUPO DE ESTUDIANTES
        private Constraint requisitosDeHabilidades(ConstraintFactory constraintFactory) {
                // Define la restricción que garantiza que todas las habilidades requeridas por
                // un proyecto estén cubiertas
                return constraintFactory.forEach(Estudiante.class)
                                .groupBy(Estudiante::getProyecto,
                                                ConstraintCollectors.toSet(Estudiante::getHabilidades))
                                .filter((proyecto, conjuntoHabilidadesEstudiantes) -> {
                                        Set<Habilidad> habilidadesRequeridas = new HashSet<>(
                                                        proyecto.getRequerimientos());
                                        Set<Habilidad> habilidadesCubiertas = conjuntoHabilidadesEstudiantes.stream()
                                                        .flatMap(Set::stream)
                                                        .collect(Collectors.toSet());
                                        return !habilidadesCubiertas.containsAll(habilidadesRequeridas);
                                })
                                .penalize("No Todas las Habilidades Cubiertas", HardSoftScore.ONE_HARD,
                                                (proyecto, conjuntoHabilidadesEstudiantes) -> {
                                                        Set<Habilidad> habilidadesRequeridas = new HashSet<>(
                                                                        proyecto.getRequerimientos());
                                                        habilidadesRequeridas.removeAll(
                                                                        conjuntoHabilidadesEstudiantes.stream()
                                                                                        .flatMap(Set::stream)
                                                                                        .collect(Collectors.toSet()));
                                                        return habilidadesRequeridas.size();
                                                });
        }

        // TIEMPO DENTRO DEL DESEADO
        private Constraint disponibilidadDeTiempo(ConstraintFactory constraintFactory) {
                return constraintFactory.forEach(Estudiante.class)
                                .filter(estudiante -> !estudiante.tieneDisponibilidadPara())
                                .penalize("Disponibilidad de Tiempo", HardSoftScore.ONE_HARD);
        }

        // TURNOS COMPATIBLES
        private Constraint disponibilidadDeTurno(ConstraintFactory constraintFactory) {
                return constraintFactory.forEach(Estudiante.class)
                                .filter(estudiante -> !estudiante.coincideTurno())
                                .penalize("Turno incompatible", HardSoftScore.ONE_HARD);
        }

        // RESTRICCIONES SUAVES
        // MAXIMO AJUSTE POSIBLE DE TIEMPO
        private Constraint maximoAjusteDeTiempo(ConstraintFactory constraintFactory) {
                return constraintFactory.forEach(Estudiante.class)
                                .penalize("Maximo Ajuste de Tiempo",
                                                HardSoftScore.ONE_SOFT,
                                                estudiante -> Math.max(0, estudiante.getDisponibilidad()
                                                                - estudiante.getProyecto().getJornada()));
        }

        // DIVERSIDAD DE GENERO
        private Constraint diversidadDeGenero(ConstraintFactory constraintFactory) {
                return constraintFactory.forEach(Estudiante.class)
                                .groupBy(Estudiante::getProyecto,
                                                ConstraintCollectors
                                                                .countDistinct(estudiante -> estudiante
                                                                                .getGenero().equals("M")),
                                                ConstraintCollectors
                                                                .countDistinct(estudiante -> estudiante
                                                                                .getGenero().equals("F")))
                                .filter((proyecto, countM, countF) -> proyecto.getCapacidad() > 1
                                                && (countM == 0 || countF == 0))
                                .penalize("Diversidad de Género",
                                                HardSoftScore.ONE_SOFT,
                                                (proyecto, countM, countF) -> 1);
        }

        // TRABAJO EN EQUIPO PREVIO
        private Constraint trabajoEnEquipoPrevio(ConstraintFactory constraintFactory) {
                return constraintFactory.forEach(Estudiante.class)
                                .join(Estudiante.class,
                                                Joiners.equal(Estudiante::getProyecto),
                                                Joiners.lessThan(Estudiante::getId, Estudiante::getId))
                                .filter((estudiante1, estudiante2) -> estudiante1.contains(estudiante2.getId()) ||
                                                estudiante2.contains(estudiante1.getId()))
                                .reward("Trabajo en Equipo Previo", HardSoftScore.ONE_SOFT);
        }

        // PREFERENCIA DE PROYECTO
        private Constraint preferenciaProyecto(ConstraintFactory constraintFactory) {
                return constraintFactory.forEach(Estudiante.class)
                                .filter(estudiante -> IntStream
                                                .of(estudiante.getPreferenciasProyectos())
                                                .anyMatch(idPreferido -> idPreferido == estudiante.getProyecto()
                                                                .getId()))
                                .reward("Preferencia de Proyecto", HardSoftScore.ONE_SOFT);
        }

}