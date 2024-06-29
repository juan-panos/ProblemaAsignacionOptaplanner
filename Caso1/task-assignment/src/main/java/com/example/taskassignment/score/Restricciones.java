package com.example.taskassignment.score;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintCollectors;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import com.example.taskassignment.domain.Tarea;

public class Restricciones implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                habilidadesRequeridas(constraintFactory),
                trabajoEquitativo(constraintFactory)
        };
    }

    private Constraint habilidadesRequeridas(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Tarea.class)
                .filter(tarea -> !tieneHabilidad(tarea))
                .penalize("requiredSkillsConstraint",
                        HardSoftScore.ONE_HARD);
    }

    private boolean tieneHabilidad(Tarea tarea) {
        if (tarea.getEmpleado() == null) {
            return false;
        }
        int[] empleadoHabilidades = tarea.getEmpleado().getHabilidades();
        int[] tareaHabilidadesRequeridas = tarea.getHabilidadesRequeridas();

        for (int habilidadRequerida : tareaHabilidadesRequeridas) {
            boolean habilidadEncontrada = false;
            for (int habilidad : empleadoHabilidades) {
                if (habilidad == habilidadRequerida) {
                    habilidadEncontrada = true;
                    break;
                }
            }
            if (!habilidadEncontrada) {
                return false;
            }
        }
        return true;
    }

    private Constraint trabajoEquitativo(ConstraintFactory constraintFactory) {
        return constraintFactory.from(Tarea.class)
                .groupBy(Tarea::getEmpleado, ConstraintCollectors.count())
                .penalize("workloadBalanceConstraint",
                        HardSoftScore.ONE_SOFT,
                        (empleado, taskCount) -> taskCount * taskCount);
    }

}
