package dev.invasion.plugins.games.mlgrush.Utils.File;

import com.google.gson.Gson;
import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.maps.SerializableLocation;
import org.bukkit.GameMode;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileManager {

    private static Gson gson = new Gson();
    private static String normalPath = MLGRush.getInstance().getDataFolder().getAbsolutePath();

    public static void save(Object toSave, String fileName) throws IOException{
        String filePath = normalPath + "/" + fileName;
        File file = new File(filePath);
        generateIfNonexistent(file);
        String save = gson.toJson(toSave);
        Files.write(Paths.get(file.getAbsolutePath()), save.getBytes());
    }

    public static Object load(Type type, String fileName) throws IOException {
        String filePath = normalPath + "/" + fileName;
        File file = new File(filePath);
        generateIfNonexistent(file);
        InputStream is = new FileInputStream(file);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while(line != null){
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String fileAsString = sb.toString();
        return gson.fromJson(fileAsString, type);
    }

    private static boolean generateIfNonexistent(File file) throws IOException {
        file.getParentFile().mkdirs();
        return file.createNewFile();
    }

}
