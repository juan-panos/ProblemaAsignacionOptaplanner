package com.flotas.asignacion_flotas.constraint;

import com.flotas.asignacion_flotas.domain.Route;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class VehicleRoutingConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                vehicleUniqueness(constraintFactory),
                specialEquipmentNeeded(constraintFactory),
                demandCapacity(constraintFactory),
                ecoRoute(constraintFactory),
                largeVehicleShortDistance(constraintFactory),
                minimizeCost(constraintFactory)
        };
    }

    private Constraint vehicleUniqueness(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Route.class)
                .join(Route.class, Joiners.equal(Route::getVehicle), Joiners.lessThan(Route::getId))
                .penalize("Vehicle uniqueness", HardSoftScore.ONE_HARD);
    }

    private Constraint demandCapacity(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Route.class)
                .filter(route -> route.getDemand() > route.getVehicle().getVehicleType().getCapacity())
                .penalize("Demand capacity", HardSoftScore.ONE_SOFT,
                        route -> route.getDemand() - route.getVehicle().getVehicleType().getCapacity());
    }

    private Constraint minimizeCost(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Route.class)
                .penalize("Minimize cost", HardSoftScore.ONE_SOFT, Route::getCost);
    }

    private Constraint specialEquipmentNeeded(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Route.class)
                .filter(route -> route.getSpecialEquipmentNeeded() && !route.getVehicle().isSpecialEquipment())
                .penalize("Special equipment needed", HardSoftScore.ONE_HARD);
    }

    private Constraint ecoRoute(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Route.class)
                .filter(route -> route.isEco() && !route.getVehicle().getVehicleType().isEcoFriendly())
                .penalize("Eco route", HardSoftScore.ofSoft(20));
    }

    private Constraint largeVehicleShortDistance(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Route.class)
                .filter(route -> route.getVehicle().isLarge() && route.getDistance() > 150)
                .reward("Large vehicle short distance", HardSoftScore.ofSoft(20));
    }
}
