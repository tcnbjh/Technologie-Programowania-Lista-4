package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

public class ConfirmCommand implements Command {

    @Override
    public void execute(ClientContext ctx) {
        ctx.getSession().confirm(ctx.getPlayer());
    }
}
