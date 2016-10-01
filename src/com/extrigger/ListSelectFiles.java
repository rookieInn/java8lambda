package com.extrigger;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Listing Select Files in a DIrectory
 *
 * Created by gxy on 2016/8/20.
 */
public class ListSelectFiles {

    public static void main(String[] args) throws Exception {
        String[] files = new File("./src/test/java/").list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".java");
            }
        });
        System.out.println(Arrays.toString(files));

        Files.newDirectoryStream(
                Paths.get("./src/test/java/"), path -> path.toString().endsWith(".java"))
                .forEach(System.out::println);

        //pick files based on file properties, such as if a file is executable, readable, or writable
        File[] files1 = new File(".").listFiles(file -> file.isHidden());
        System.out.println(Arrays.toString(files1));

        new File(".").listFiles(File::isHidden);
    }

}
