package net.bopbopstudios.timer_hud.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import net.bopbopstudios.timer_hud.client.TimerHUDClient;
import org.slf4j.Logger;

import java.io.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class Storage {
    private static final Logger LOGGER;
    private static Storage INSTANCE;
    private final File playTimeFile = new File("./playtimes.txt");
    private HashMap<String, Duration> playTimes = new HashMap<>();
    private final String separator = " -> ";

    public static Storage getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Storage();
        }

        return INSTANCE;
    }

    private Storage() {
    }

    public void readFile() {
        HashMap<String, Duration> map = new HashMap<>();
        if (this.playTimeFile.exists()) {
            try {
                BufferedReader bufferedReader = Files.newReader(this.playTimeFile, Charsets.UTF_8);

                try {
                    LinkedList<String> lines = new LinkedList<>();
                    bufferedReader.lines().forEach((linex) -> {
                        linex = linex.trim();
                        if (!linex.isEmpty()) {
                            lines.add(linex);
                        }

                    });
                    if (!lines.isEmpty()) {
                        String firstLine = lines.getFirst();
                        int version;
                        if (firstLine.startsWith("version: ")) {
                            version = Integer.parseInt(firstLine.substring(9));
                        } else {
                            version = 1;
                        }

                        Iterator<String> it = lines.iterator();
                        if (version == 2) {
                            it.next();
                        }

                        if (version == 2 || version == 1) {
                            while(it.hasNext()) {
                                String line = it.next();
                                String[] parts = line.split(" -> ");
                                String name = parts[0];
                                String rest = parts[1];
                                if (!name.contains(" ")) {
                                    long millis = Long.parseLong(rest);
                                    Duration d = Duration.ofMillis(millis);
                                    map.put(name, d);
                                }
                            }
                        }
                    }
                } catch (Throwable var15) {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable var14) {
                            var15.addSuppressed(var14);
                        }
                    }

                    throw var15;
                }

                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception var16) {
                LOGGER.error("Could not load existing playtimes...");
            }
        }

        this.playTimes = map;
    }

    public void writeFile() {
        try {
            BufferedWriter bufferedWriter = Files.newWriter(this.playTimeFile, Charsets.UTF_8);

            try {
                bufferedWriter.write("version: 2\n");
                Iterator<String> stringIterator = this.playTimes.keySet().iterator();

                while(true) {
                    if (!stringIterator.hasNext()) {
                        bufferedWriter.flush();
                        LOGGER.debug("Durations updated...");
                        break;
                    }

                    String name = (String)stringIterator.next();
                    Duration d = (Duration)this.playTimes.getOrDefault(name, Duration.ZERO);
                    long millis = d.toMillis();
                    bufferedWriter.write(name + " -> " + millis + "\n");
                }
            } catch (Throwable var8) {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }

                throw var8;
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (Exception var9) {
            LOGGER.error("Could not write playtimes...");
        }

    }

    public Duration getDuration(String name) {
        return (Duration)this.playTimes.getOrDefault(name, Duration.ZERO);
    }

    public void saveDuration(String path, Duration duration) {
        this.playTimes.put(path, duration);
        this.writeFile();
    }

    static {
        LOGGER = TimerHUDClient.LOGGER;
        INSTANCE = null;
    }
}
