package pl.moje.go.serwer;

import java.io.PrintWriter;

/** Dane klienta: sesja gry, gracz i kanał do wysyłania odpowiedzi. */

public class ClientContext {

    private final GameSession session;
    private final Player player;
    private final PrintWriter out;

    public ClientContext(GameSession session, Player player, PrintWriter out){
        this.session = session;
        this.player = player;
        this.out = out;
    }

    public GameSession getSession() {
        return session;
    }

    public Player getPlayer() {
        return player;
    }

    public PrintWriter getOut() {
        return out;
    }
}
