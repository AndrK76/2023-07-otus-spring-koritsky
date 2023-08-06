package ru.otus.andrk.service;

import java.io.OutputStream;
import java.io.PrintStream;

public class DialogServiceStreamImpl implements DialogService {

    private final PrintStream outStream;


    public DialogServiceStreamImpl() {
        this.outStream = System.out;
    }

    public DialogServiceStreamImpl(OutputStream outStream) {
        this.outStream = new PrintStream(outStream);
    }


    @Override
    public void displayText(String textForDisplay) {
        outStream.println(textForDisplay);
    }
}
