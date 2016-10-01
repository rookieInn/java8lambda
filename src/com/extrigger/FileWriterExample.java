package com.extrigger;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by gxy on 2016/8/25.
 */
public class FileWriterExample {

    private FileWriter writer;

    public FileWriterExample(String fileName) throws IOException {
        writer = new FileWriter(fileName);
    }

    public void writeStuff(String message) throws IOException{
        writer.write(message);
    }

    public void finalize() throws IOException {
        writer.close();
    }

    public void close() throws IOException {
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        FileWriterExample writerExample = new FileWriterExample("peekaboo.txt");
        try {
            writerExample.writeStuff("peek-a-boo");
        } finally {
            writerExample.close();
        }
    }

}
