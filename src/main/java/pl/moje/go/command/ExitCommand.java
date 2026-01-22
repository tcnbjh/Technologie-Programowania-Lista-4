package pl.moje.go.command;

import pl.moje.go.common.Protocol;
import pl.moje.go.serwer.ClientContext;

/**
 * Komenda obsługująca rozłączenie się klienta.
 * Sygnalizuje pętli serwera konieczność zakończenia wątku.
 */
public class ExitCommand implements Command {

    @Override
    public void execute(ClientContext ctx){
        ctx.getOut().println(Protocol.MSG_BYE);
    }

    @Override
    public boolean shouldTerminate() {
        return true;
    }
}
