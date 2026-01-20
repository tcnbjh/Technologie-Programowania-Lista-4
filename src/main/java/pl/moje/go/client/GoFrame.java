package pl.moje.go.client;

import pl.moje.go.common.Kolor;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GoFrame extends Frame {

    private final BoardCanvas boardCanvas;
    private GameClient client;

    public GoFrame(int size) {
        super("Go");

        boardCanvas = new BoardCanvas(size);
        add(boardCanvas, BorderLayout.CENTER);

        setSize(620, 640);
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
    }

    public void updateBoard(Kolor[][] board) {
        boardCanvas.setBoard(board);
    }
}
