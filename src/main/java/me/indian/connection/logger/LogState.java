package me.indian.connection.logger;

import me.indian.connection.util.ConsoleColors;

public enum LogState {
    NONE(ConsoleColors.RESET),
    INFO(ConsoleColors.BLUE),
    ALERT(ConsoleColors.BRIGHT_RED ),
    CRITICAL(ConsoleColors.DARK_RED),
    ERROR(ConsoleColors.DARK_GRAY),
    WARNING(ConsoleColors.BRIGHT_WHITE);

    private final String colorCode;

    LogState(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}
