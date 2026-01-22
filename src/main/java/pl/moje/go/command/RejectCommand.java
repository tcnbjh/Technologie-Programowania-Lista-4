package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

/**
 * Komenda odrzucenia propozycji martwych kamieni.
 * Przywraca grę do fazy aktywnej (wznawia rozgrywkę).
 */
public class RejectCommand implements Command {

    @Override
    public void execute(ClientContext ctx) {
        ctx.getSession().reject(ctx.getPlayer());
    }
}
