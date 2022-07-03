package io.github.gerardpi.ft.logging;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

public class TextAreaLogTarget extends OutputStream {
    private final TextArea textArea;

    public TextAreaLogTarget(TextArea textArea) {
        this.textArea = textArea;
    }

    private void writeLine(String line) {
        textArea.appendText(line);
    }
    @Override
    public void write(int b) throws IOException {
        writeLine(Integer.toString(b));
    }

    @Override
    public void write(byte[] b) throws IOException {
        writeLine(new String(b));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        writeLine(new String(b, off, len));
    }

    @Override
    public void flush() throws IOException {
        textArea.setScrollTop(Double.MAX_VALUE);
    }
}
