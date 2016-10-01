package com.extrigger;

import java.nio.file.*;
import java.util.concurrent.TimeUnit;

/**
 * Watching a File Change
 *
 * Created by gxy on 2016/8/20.
 */
public class WatchFileChange {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get(".");
        WatchService watchService = path.getFileSystem().newWatchService();
        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        System.out.println("Report any file changed within next 1 minutes ...");

        WatchKey watchKey = watchService.poll(1, TimeUnit.MINUTES);

        if (watchKey != null) {
            watchKey.pollEvents().stream().forEach(event -> System.out.println(event.context()));
        }
    }


}
