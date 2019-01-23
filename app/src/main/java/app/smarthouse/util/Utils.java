package app.smarthouse.util;

import java.util.ArrayList;

public class Utils {
    public static ArrayList<String> getArrayOfCommands() {
        ArrayList<String> allComands = new ArrayList<>();
        allComands.add("living room light on");
        allComands.add("living room light off");
        allComands.add("bedroom light on");
        allComands.add("bedroom light off");
        allComands.add("kitchen light on");
        allComands.add("kitchen light off");
        allComands.add("bathroom light on");
        allComands.add("bathroom light off");
        allComands.add("garage light on");
        allComands.add("garage light off");
        allComands.add("entrance light on");
        allComands.add("entrance light off");
        allComands.add("garage light on");
        allComands.add("garage light off");
        allComands.add("garage door open");
        allComands.add("garage door close");
        allComands.add("all lights on");
        allComands.add("all lights off");

        return allComands;
    }
}
