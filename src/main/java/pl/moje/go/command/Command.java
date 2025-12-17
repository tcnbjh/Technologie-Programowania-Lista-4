package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

public interface Command {
    void execute(ClientContext context);

    default boolean shouldTerminate() {
        return false;
    }
}
