package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

public class FFCommand implements Command{

    @Override
    public void execute(ClientContext ctx){
        ctx.getSession().ff(ctx.getPlayer());
    }
}
