package com.extrigger;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * List All Files in a Directory
 *
 * Created by gxy on 2016/8/20.
 */
public class ListFiles {

    public static void main(String[] args) throws Exception {
        Files.list(Paths.get("."))
             .forEach(System.out::println);

        System.out.println(" --------- ");

        // list only the subdirectories
        Files.list(Paths.get("."))
             .filter(Files::isDirectory)
             .forEach(System.out::println);
    }



}
