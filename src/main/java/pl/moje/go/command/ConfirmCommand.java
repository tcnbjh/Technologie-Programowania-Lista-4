package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

/**
 * Komenda potwierdzenia (akceptacji) proponowanego układu martwych kamieni.
 * Używana w fazie końcowej liczenia punktów.
 */
public class ConfirmCommand implements Command {

    @Override
    public void execute(ClientContext ctx) {
        ctx.getSession().confirm(ctx.getPlayer());
    }
}
