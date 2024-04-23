package edu.ftcsushi;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;

import edu.ftcsushi.gamepad.GamepadController;
import edu.ftcsushi.util.ElapsedTimeMillis;
import edu.ftcsushi.util.PeriodicRunner;

/**
 * The main robot class should be derived from this, and this has structured methods
 * like init..(), onPeriodic...(), and exit() that should be implemented by the
 * derived class.
 * <p>
 * The derived class should have:
 * 1) Instances of all the subsystems
 * 2) Instances of the teleop and autonomous controllers
 * 3) State information that the controllers modify.
 */
public abstract class RobotBase {
    private final int INTERVAL_MS_PERIODIC = 20;

    private final ElapsedTimeMillis timeStartOfGame = new ElapsedTimeMillis();
    private final ElapsedTimeMillis timeLastPeriodic = new ElapsedTimeMillis();
    private boolean hasInitializedTimeStartOfGame = false;


    // These variables will be set when {@link RobotBase#runOpMode(LinearOpMode, OpModeType)}
    //    is called.
    private GamepadController gamepad1;
    private GamepadController gamepad2;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    private final FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    private void saveTimeStartOfGame() {
        hasInitializedTimeStartOfGame = true;
        timeStartOfGame.reset();
    }

    private void resetTimePeriodic() {
        timeLastPeriodic.reset();
    }

    /**
     * Get the time remaining (in milliseconds) until the next periodic loop.  The
     * periodic methods run every interval as defined by {@link #INTERVAL_MS_PERIODIC}.
     *
     * @return Milliseconds till next periodic loop.
     */
    private long getMillisToRunPeriodicAgain() {
        long timeRemaining = INTERVAL_MS_PERIODIC - timeLastPeriodic.getElapsedMilliseconds();
        if (timeRemaining > 0)
            return timeRemaining;
        return 0;
    }

    public GamepadController getGamepad1() {
        return gamepad1;
    }

    public GamepadController getGamepad2() {
        return gamepad2;
    }

    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public double getSecondsSinceStartOfGame() {
        // If we have not initialized the timer yet, just say no time has elapsed
        //    since the start of game.
        if (!hasInitializedTimeStartOfGame)
            return 0;

        // Return the time elapsed since start of game
        return timeStartOfGame.getElapsedSeconds();
    }

    /**
     * Call this method from runOpMode() in the main {@link LinearOpMode} robot file. This will in
     * turn call the appropriate init(), onPeriodic(), and exit() methods for the robot and
     * specified op modes.
     *
     * @param robot      The {@link LinearOpMode} robot making the call -- i.e. pass "this"
     * @param opModeType The op mode of the robot making the call.
     */
    public void runOpMode(LinearOpMode robot, OpModeType opModeType) {

        // Save the gamepad, hardware map, and telemetry
        hardwareMap = robot.hardwareMap;
        gamepad1 = new GamepadController(robot.gamepad1);
        gamepad2 = new GamepadController(robot.gamepad2);
        telemetry = robot.telemetry;


        // Run the init() functions
        initRobot();
        switch (opModeType) {
            case TELEOP:
                initTeleOp();
                break;
            case AUTONOMOUS:
                initAutonomous();
                break;
        }

        // Wait for the game to start (driver presses PLAY)
        robot.waitForStart();

        // Begin the start-of-game timer and start the periodic timer.
        saveTimeStartOfGame();

        // Stop execution if a stop was pushed on driver hub.
        if (robot.isStopRequested()) return;

        // Execute the periodic commands as long as the mode is active.
        while (robot.opModeIsActive()) {
            // Remember when the periodic run occurred.
            resetTimePeriodic();

            // Run the onPeriodic() methods
            // ...Execute the methods registered with the PeriodicRunner
            PeriodicRunner.getInstance().runAllPeriodicRunnables();

            // ...Execute the op-mode-specific periodic method.
            switch (opModeType) {
                case TELEOP:
                    onPeriodicTeleOp();

                    // Execute any running actions that have been queued by the onPeriodicTeleOp()
                    //    method.
                    executeTeleOpActions();
                    break;
                case AUTONOMOUS:
                    onPeriodicAutonomous();
                    break;
            }

            // ...Execute the general robot's periodic method.
            onPeriodicRobot();

            // Ensure that sufficient time has elapsed before the next periodic run.
            long timeRemaining = getMillisToRunPeriodicAgain();
            if (timeRemaining > 0)
                robot.sleep(timeRemaining);
        }

        //  all the exit() functions
        switch (opModeType) {
            case TELEOP:
                exitTeleOp();
                break;
            case AUTONOMOUS:
                exitAutonomous();
                break;
        }
        exitRobot();
    }

    /**
     * This is called when the robot is started.  Use to initialize anything common to
     * both op modes.
     */
    protected abstract void initRobot();

    /**
     * This is called periodically regardless of op mode, but only after the corresponding
     * op mode function is called.
     */
    protected abstract void onPeriodicRobot();

    /**
     * This is called when exiting the robot, after the exit of the specific op mode.
     */
    protected abstract void exitRobot();

    /**
     * Initialize the robot for autonomous mode.  This is called after {@link #initRobot()} is
     * called.
     */
    protected abstract void initAutonomous();

    /**
     * This is called periodically during autonomous opmode.
     */
    protected abstract void onPeriodicAutonomous();

    /**
     * This is called when exiting autonomous mode.
     */
    protected abstract void exitAutonomous();

    /**
     * Initialize the robot for teleop mode.  This is called after {@link #initRobot()} is
     * called.
     */
    protected abstract void initTeleOp();

    /**
     * This is called periodically during teleop mode.
     */
    protected abstract void onPeriodicTeleOp();

    /**
     * This is called when exiting autonomous mode.
     */
    protected abstract void exitTeleOp();

    /**
     * Enqueue {@link Action}s during tele-op mode ({@link #onPeriodicTeleOp()})
     * that should be executed.
     *
     * @param action Action to enqueue.
     * @see #executeTeleOpActions()
     */
    public void addTeleOpAction(Action action) {
        runningActions.add(action);
    }

    /**
     * Execute {@link Action}s that are queued during {@link #onPeriodicTeleOp()}.
     *
     * @see #addTeleOpAction(Action)
     */
    private void executeTeleOpActions() {
        TelemetryPacket packet = new TelemetryPacket();

        // update running actions
        List<Action> newActions = new ArrayList<>();
        for (Action action : runningActions) {
            action.preview(packet.fieldOverlay());
            if (action.run(packet)) {
                newActions.add(action);
            }
        }
        runningActions = newActions;

        dash.sendTelemetryPacket(packet);
    }

    /**
     * The different op modes in FTC.
     */
    public enum OpModeType {
        TELEOP,
        AUTONOMOUS
    }
}
