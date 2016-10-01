package com.extrigger;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by gxy on 2016/8/25.
 */
public class FileWriterEAM {

    private final FileWriter writer;
    private FileWriterEAM(final String fileName) throws IOException {
        writer = new FileWriter(fileName);
    }
    private void close() throws IOException {
        System.out.println("close called automatically...");
        writer.close();
    }
    public void writeStuff(final String message) throws IOException {
        writer.write(message);
    }

    //using higher-order functions
    public static void use(String fileName, UseInstance<FileWriterEAM, IOException> block) throws IOException {
        FileWriterEAM writerEAM = new FileWriterEAM(fileName);
        try {
            block.accept(writerEAM);
        } finally {
            writerEAM.close();
        }
    }

    public static void main(String[] args) throws IOException {
        FileWriterEAM.use("eam.txt", writerEAM -> writerEAM.writeStuff("sweet"));
        FileWriterEAM.use("eam2.txt", writerEAM -> {
            writerEAM.writeStuff("how");
            writerEAM.writeStuff("sweet");
        });
    }

}
