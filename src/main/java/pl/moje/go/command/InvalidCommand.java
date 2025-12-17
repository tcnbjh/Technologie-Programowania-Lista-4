package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

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
