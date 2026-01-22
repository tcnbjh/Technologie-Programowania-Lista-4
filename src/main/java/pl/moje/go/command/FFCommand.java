package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

/**
 * Komenda obsługująca poddanie się gracza (Forfeit).
 * Kończy grę natychmiastowo.
 */
public class FFCommand implements Command{

    @Override
    public void execute(ClientContext ctx){
        ctx.getSession().ff(ctx.getPlayer());
    }
}
