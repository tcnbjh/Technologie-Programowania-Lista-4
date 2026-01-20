package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

public class RejectCommand implements Command {

    @Override
    public void execute(ClientContext ctx) {
        ctx.getSession().reject(ctx.getPlayer());
    }
}
