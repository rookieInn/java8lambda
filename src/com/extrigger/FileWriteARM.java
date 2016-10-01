package com.extrigger;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by gxy on 2016/8/25.
 */
public class FileWriteARM implements AutoCloseable{
    private FileWriter writer;

    public FileWriteARM(String fileName) throws IOException {
        writer = new FileWriter(fileName);
    }

    public void writeStuff(String message) throws IOException {
        writer.write(message);
    }


    @Override
    public void close() throws IOException {
        System.out.println("close called automatically...");
        writer.close();
    }

    public static void main(String[] args) {
        try(FileWriteARM writeARM = new FileWriteARM("peekaboo.txt")) {
            writeARM.writeStuff("peek-a-boo");
            System.out.println("done with the resource");
        }catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
