package edu.ftcsushi.fsm;

public class States {
    public static boolean isNull(FsmState state) {
        return (state == null || state == NullState.getInstance());
    }
}
