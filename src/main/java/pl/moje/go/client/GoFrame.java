package pl.moje.go.client;

import java.awt.*;
import java.awt.event.*;

public class GoFrame extends Frame {

    private BoardCanvas boardCanvas;
    private TextArea messages;

    public GoFrame(GameClient client) {
        setTitle("Gra Go");
        setSize(620, 720);
        setLayout(new BorderLayout());

        boardCanvas = new BoardCanvas(client);

        messages = new TextArea(5, 40);
        messages.setEditable(false);

        add(boardCanvas, BorderLayout.CENTER);
        add(messages, BorderLayout.SOUTH);

        client.setBoardCanvas(boardCanvas);
        client.setMessages(messages);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }
}
