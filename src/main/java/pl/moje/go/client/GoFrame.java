package pl.moje.go.client;

import pl.moje.go.common.Kolor;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Główne okno aplikacji oparte na bibliotece AWT.
 * <p>
 * Kontener dla płótna z planszą {@link BoardCanvas} oraz panelu sterowania.
 * Odpowiada za inicjalizację przycisków i etykiet oraz udostępnianie metod
 * do bezpiecznej aktualizacji widoku z poziomu wątku klienta.
 */
public class GoFrame extends Frame {

    private final BoardCanvas boardCanvas;
    private GameClient client;

    private final Label statusLabel;
    private final Label turnLabel;
    private final Button btnPass;
    private final Button btnSurrender;
    private final Button btnAccept;
    private final Button btnReject;

    /**
     * Tworzy nowe okno gry.
     *
     * @param size rozmiar planszy (ilość przecięć linii, np. 19)
     */
    public GoFrame(int size) {
        super("Go");

        setLayout(new BorderLayout());

        statusLabel = new Label("Oczekiwanie na połączenie...");
        statusLabel.setAlignment(Label.CENTER);
        statusLabel.setBackground(Color.LIGHT_GRAY);
        add(statusLabel, BorderLayout.NORTH);

        turnLabel = new Label("---");
        turnLabel.setAlignment(Label.CENTER);
        turnLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        turnLabel.setBackground(new Color(200, 255, 200));
        add(turnLabel, BorderLayout.SOUTH);

        boardCanvas = new BoardCanvas(size);
        add(boardCanvas, BorderLayout.CENTER);

        Panel sidePanel = new Panel();
        sidePanel.setLayout(new GridLayout(4, 1, 5, 5));
        sidePanel.setBackground(SystemColor.control);

        btnPass = new Button("PASUJ");
        btnSurrender = new Button("PODDAJ");
        btnAccept = new Button("AKCEPTUJ");
        btnReject = new Button("ODRZUC");

        btnPass.addActionListener(e -> {
            if (client != null) client.sendPass();
        });

        btnSurrender.addActionListener(e -> {
            if (client != null) client.sendSurrender();
        });

        btnAccept.addActionListener(e -> {
            if (client != null) client.sendConfirm();
        });

        btnReject.addActionListener(e -> {
            if (client != null) client.sendReject();
        });

        sidePanel.add(btnPass);
        sidePanel.add(btnSurrender);
        sidePanel.add(btnAccept);
        sidePanel.add(btnReject);

        add(sidePanel, BorderLayout.EAST);

        setSize(800, 640);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (client != null) client.sendExit();
                dispose();
                System.exit(0);
            }
        });
    }

    /**
     * Ustawia referencję do obiektu klienta sieciowego.
     * Wymagane, aby przyciski mogły wysyłać komendy do serwera.
     *
     * @param client obiekt obsługujący komunikację
     */
    public void setClient(GameClient client) {
        this.client = client;
        boardCanvas.setClient(client);
        setMessage("Połączono z serwerem.");
    }

    /**
     * Przekazuje nową tablicę stanu planszy do komponentu rysującego.
     *
     * @param board dwuwymiarowa tablica kolorów reprezentująca układ kamieni
     */
    public void updateBoard(Kolor[][] board) {
        boardCanvas.setBoard(board);
    }

    /**
     * Wyświetla komunikat systemowy na górnym pasku statusu.
     *
     * @param msg treść wiadomości
     */
    public void setMessage(String msg) {
        statusLabel.setText(msg);
    }

    /**
     * Aktualizuje informację o bieżącej turze (np. "TWÓJ RUCH").
     *
     * @param msg treść informacji o turze
     */
    public void setTurnInfo(String msg) {
        turnLabel.setText(msg);
        if (msg.startsWith("TWÓJ")) turnLabel.setBackground(Color.GREEN);
        else turnLabel.setBackground(Color.LIGHT_GRAY);
    }
}
