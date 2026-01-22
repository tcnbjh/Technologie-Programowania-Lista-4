package pl.moje.go.command;

import pl.moje.go.serwer.ClientContext;

/**
 * Interfejs reprezentujący pojedyncze polecenie w systemie.
 * <p>
 * Każda klasa implementująca ten interfejs odpowiada za wykonanie konkretnej
 * akcji w grze na podstawie danych otrzymanych od klienta.
 */
public interface Command {

    /**
     * Wykonuje logikę komendy w kontekście danego klienta.
     *
     * @param context kontekst klienta zawierający sesję gry, gracza i strumień wyjścia
     */
    void execute(ClientContext context);

    /**
     * Określa, czy wykonanie tej komendy powinno zakończyć obsługę klienta (zamknąć połączenie).
     *
     * @return true jeśli połączenie ma zostać zamknięte (np. EXIT), false w przeciwnym razie
     */
    default boolean shouldTerminate() {
        return false;
    }
}
