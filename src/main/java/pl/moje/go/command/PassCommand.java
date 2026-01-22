package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

/**
 * Komenda obsługująca spasowanie przez gracza.
 * Może prowadzić do fazy oznaczania martwych kamieni.
 */
public class PassCommand implements Command{

    @Override
    public void execute(ClientContext ctx){
        ctx.getSession().pass(ctx.getPlayer());
    }
}
