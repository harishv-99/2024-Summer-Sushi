package edu.ftcsushi.robots.sushi_old;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadReader {

    /**
     * The buttons in the gamepad
     */
    public enum Button {
        A,
        B,
        X,
        Y
    }

    public Gamepad gamepad;

    // Keep track of button values for current turn and prior turn.
    final private int BUTTON_VALUE_PRIOR = 0;
    final private int BUTTON_VALUE_CURRENT = 1;
    private boolean[] buttonValues_A = new boolean[2];
    private boolean[] buttonValues_B = new boolean[2];
    private boolean[] buttonValues_X = new boolean[2];
    private boolean[] buttonValues_Y = new boolean[2];

    private void saveButtonValue(boolean[] buttonValues, Button buttonId) {
        // Save the old value to the 1st index
        buttonValues[BUTTON_VALUE_PRIOR] = buttonValues[BUTTON_VALUE_CURRENT];

        // Get the new value for the 2nd index
        switch(buttonId) {
            case A:
                buttonValues[BUTTON_VALUE_CURRENT] = gamepad.a;
                break;

            case B:
                buttonValues[BUTTON_VALUE_CURRENT] = gamepad.b;
                break;

            case X:
                buttonValues[BUTTON_VALUE_CURRENT] = gamepad.x;
                break;

            case Y:
                buttonValues[BUTTON_VALUE_CURRENT] = gamepad.y;
                break;
        }
    }


    private boolean hasReleasedButton(boolean[] buttonValues) {
        // The button was pressed prior turn and released now
        return buttonValues[BUTTON_VALUE_PRIOR] & ! buttonValues[BUTTON_VALUE_CURRENT];
    }


    private boolean isPressingButton(boolean[] buttonValues) {
        // The button was pressed prior turn and released now
        return buttonValues[BUTTON_VALUE_CURRENT];
    }

    /**
     * Create a Gamepad wrapper to intelligently process inputs
     * @param gamepad The gamepad object to wrap.
     */
    public GamepadReader(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public boolean isPressingButton(Button buttonId) {
        switch (buttonId) {
            case A:
                return isPressingButton(buttonValues_A);
            case B:
                return isPressingButton(buttonValues_B);
            case X:
                return isPressingButton(buttonValues_X);
            case Y:
                return isPressingButton(buttonValues_Y);
            default:
                throw new IllegalStateException("Unexpected buttonId");
        }
    }
    public boolean hasReleasedButton(Button buttonId) {
        switch (buttonId) {
            case A:
                return hasReleasedButton(buttonValues_A);
            case B:
                return hasReleasedButton(buttonValues_B);
            case X:
                return hasReleasedButton(buttonValues_X);
            case Y:
                return hasReleasedButton(buttonValues_Y);
            default:
                throw new IllegalStateException("Unexpected buttonId");
        }
    }

    /**
     * Process events each time the TeleOp loop runs.  This has to be run
     * before any of the other functions can be used for that loop.
     */
    public void processLoop() {
        saveButtonValue(buttonValues_A, Button.A);
        saveButtonValue(buttonValues_B, Button.B);
        saveButtonValue(buttonValues_X, Button.X);
        saveButtonValue(buttonValues_Y, Button.Y);
    }
}
