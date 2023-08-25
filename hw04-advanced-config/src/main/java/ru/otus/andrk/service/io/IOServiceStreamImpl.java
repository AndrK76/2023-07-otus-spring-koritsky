package ru.otus.andrk.service.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Scanner;

public class IOServiceStreamImpl implements IOService {

    private final PrintStream outStream;

    private final Scanner scanner;

    public IOServiceStreamImpl(OutputStream outStream, InputStream inStream, Charset charset) {
        this.outStream = new PrintStream(outStream, true, charset);
        this.scanner = new Scanner(inStream, charset);
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
