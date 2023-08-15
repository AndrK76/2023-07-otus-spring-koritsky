package ru.otus.andrk.service.dialog;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;


public class DialogServiceStreamImpl implements DialogService {

    private final PrintStream outStream;

    private final Scanner scanner;

    public DialogServiceStreamImpl(OutputStream outStream, InputStream inStream) {
        this.outStream = new PrintStream(outStream);
        this.scanner = new Scanner(inStream);
    }

    @Override
    public void displayText(String textForDisplay) {
        outStream.println(textForDisplay);
    }

    @Override
    public String readText() {
        return scanner.nextLine();
    }
}
