package com.extrigger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Listing Immediate Subdirectors Using flatMap
 *
 * Created by gxy on 2016/8/20.
 */
public class ListSubDirs {

    public static void main(String[] args) {
        //use the traditional for loop to iterator over the file in the given direction
        List<Object> files = new ArrayList<>();

        File[] filesInCurrentDir = new File(".").listFiles();
        for (File file : filesInCurrentDir) {
            File[] filesInSubDir = file.listFiles();
            if (filesInSubDir != null) {
                files.addAll(Arrays.asList(filesInSubDir));
            } else {
                files.add(file);
            }
        }
        System.out.println("Count: " + files.size());

        //use flapMap() method flapMap将多个stream合并

        List<File> files1 = Stream.of(new File(".").listFiles())
                .flatMap(file -> file.listFiles() == null ? Stream.of(file) : Stream.of(file.listFiles()))
                .collect(Collectors.toList());
        System.out.println("Count: " + files1.size());

    }

}
