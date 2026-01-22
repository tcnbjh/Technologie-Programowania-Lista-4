package pl.moje.go.command;

import pl.moje.go.common.Protocol;

/**
 * Fabryka tworząca obiekty komend na podstawie tekstu.
 * <p>
 * Analizuje pierwszą linię przesłaną przez klienta (zgodnie z protokołem),
 * parsuje parametry i zwraca odpowiednią instancję implementującą {@link Command}.
 */
public class CommandFactory {

    /**
     * Tworzy obiekt komendy na podstawie linii tekstu.
     *
     * @param line surowy ciąg znaków odebrany z sieci (np. "MOVE 3 4")
     * @return odpowiedni obiekt Command lub InvalidCommand w przypadku błędu
     */
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
            case Protocol.CMD_CONFIRM ->  new ConfirmCommand();
            case Protocol.CMD_REJECT ->  new RejectCommand();
            default -> new InvalidCommand("Nieznana komenda");
        };
    }
}
