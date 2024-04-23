package edu.ftcsushi.util;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

public class RoadRunner {
    public static PoseVelocity2d toPoseVelocity2d(double velocityLateral,
                                                  double velocityAxial,
                                                  double velocityAngular) {

        PoseVelocity2d pv = new PoseVelocity2d(new Vector2d(velocityLateral, velocityAxial),
                velocityAngular);

        return pv;
    }
}
