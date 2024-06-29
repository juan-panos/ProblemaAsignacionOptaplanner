package com.redhat.optaplannersbs.score;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.sum;
import com.redhat.optaplannersbs.domain.Turno;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

/**
 * Proveedor de restricciones para el problema de balanceo de carga en la nube.
 */
public class Restricciones implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        // Define las restricciones que OptaPlanner debe cumplir o optimizar.
        return new Constraint[] {
                turnoFactible(constraintFactory)
        };
    }

    // ************************************************************************
    // Restricciones duras
    // ************************************************************************
    private Constraint turnoFactible(ConstraintFactory constraintFactory) {
        // Verifica que el turno no tenga mas duracion que la jornada del trabajador
        return constraintFactory.from(Turno.class)
                .groupBy(Turno::getTrabajador, sum(Turno::getDuracion))
                .filter((trabajador, duracion) -> duracion > trabajador.getJornada())
                .penalize("exceso de turno",
                        HardSoftScore.ONE_HARD,
                        (trabajador, duracion) -> duracion
                                - trabajador.getJornada());
    }

}