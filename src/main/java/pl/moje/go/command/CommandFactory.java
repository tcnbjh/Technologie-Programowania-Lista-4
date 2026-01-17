package pl.moje.go.command;

import pl.moje.go.common.Protocol;

public class CommandFactory {

    public static Command fromLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0].toUpperCase();

        return switch (cmd){
            case Protocol.CMD_MOVE -> {
                if (parts.length != 3){
                    yield new InvalidCommand("MOVE X Y");
                }
                try {
                    yield new MoveCommand(
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2])
                    );
                } catch (NumberFormatException e) {
                    yield new InvalidCommand("MOVE X Y (liczby)");
                }
            }
            case Protocol.CMD_EXIT -> new ExitCommand();
            case Protocol.CMD_PASS -> new PassCommand();
            case Protocol.CMD_FF -> new FFCommand();
            default -> new InvalidCommand("Nieznana komenda");
        };
    }
}
