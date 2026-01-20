package pl.moje.go.client;

import pl.moje.go.common.Kolor;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GoFrame extends Frame {

    private final BoardCanvas boardCanvas;
    private GameClient client;

    private final Label statusLabel;
    private final Button btnPass;
    private final Button btnSurrender;
    private final Button btnAccept;
    private final Button btnReject;

    public GoFrame(int size) {
        super("Go");

        setLayout(new BorderLayout());

        statusLabel = new Label("Oczekiwanie na połączenie...");
        statusLabel.setAlignment(Label.CENTER);
        statusLabel.setBackground(Color.LIGHT_GRAY);
        add(statusLabel, BorderLayout.NORTH);

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

    public void setClient(GameClient client) {
        this.client = client;
        boardCanvas.setClient(client);
        setMessage("Połączono z serwerem.");
    }

    public void updateBoard(Kolor[][] board) {
        boardCanvas.setBoard(board);
    }

    public void setMessage(String msg) {
        statusLabel.setText(msg);
    }
}
