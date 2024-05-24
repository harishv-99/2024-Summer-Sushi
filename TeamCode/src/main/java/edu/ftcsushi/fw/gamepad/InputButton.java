package edu.ftcsushi.fw.gamepad;

public interface InputButton {
    boolean isDown();

    boolean wasJustPressed();

    boolean wasJustReleased();

    boolean hasChangedState();
}
