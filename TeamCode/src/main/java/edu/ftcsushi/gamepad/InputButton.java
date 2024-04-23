package edu.ftcsushi.gamepad;

public interface InputButton {
    boolean isDown();

    boolean wasJustPressed();

    boolean wasJustReleased();

    boolean hasChangedState();
}
