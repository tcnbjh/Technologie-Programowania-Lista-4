package pl.moje.go.command;

import pl.moje.go.common.Protocol;
import pl.moje.go.serwer.ClientContext;

public class MoveCommand implements Command {

    private final int x;
    private final int y;

    public MoveCommand(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute(ClientContext ctx){
        boolean ok = ctx.getSession().makeMove(x, y, ctx.getPlayer());
        if (ok){
            ctx.getOut().println(Protocol.MSG_MOVE_OK);
        } else {
            ctx.getOut().println(Protocol.MSG_MOVE_INVALID);
        }
    }
}
