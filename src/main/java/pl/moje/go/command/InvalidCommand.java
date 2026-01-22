package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

/**
 * Obiekt typu Null Object / Error Handler dla nierozpoznanych lub błędnych komend.
 * Wysyła komunikat błędu zwrotnego do klienta.
 */
public class InvalidCommand implements Command {

    private final String message;

    public InvalidCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute(ClientContext ctx){
        ctx.getOut().println("ERROR: " + message);
    }
}
