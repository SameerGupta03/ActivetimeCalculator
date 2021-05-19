import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Calculator {

    public static void main(String[] args) throws IOException {

        Map<String, Long> times;
        Map<String, String> names = new HashMap<>();

        File file = new File("names.txt");
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine())
            names.put(sc.nextLine(), sc.nextLine());

        times = Files.lines(Paths.get("staffuuid.txt")).collect(Collectors.toMap(n -> n, n -> 0L));

        Config post_config = ConfigFactory.parseFile(new File("post_players.stor"));

        for (String key : times.keySet()) {
            if (!post_config.hasPath(key)) continue;
            times.put(key, post_config.getConfig(key).getLong("activetime"));
        }

        Config pre_config = ConfigFactory.parseFile(new File("pre_players.stor"));

        for (String key : times.keySet()) {
            if (!pre_config.hasPath(key)) continue;
            times.put(key, times.get(key) - pre_config.getConfig(key).getLong("activetime"));
        }

        times.values().removeIf(v -> v == 0);

        Files.write(Paths.get("timDiff.txt"), times.entrySet().stream()
                .map(n -> names.get(n.getKey()) + "," + String.format("%.2f", n.getValue()/3600.))
                .collect(Collectors.toList()));
    }

}
