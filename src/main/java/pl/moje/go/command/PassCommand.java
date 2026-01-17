package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

public class PassCommand implements Command{

    @Override
    public void execute(ClientContext ctx){
        ctx.getSession().pass(ctx.getPlayer());
    }
}
