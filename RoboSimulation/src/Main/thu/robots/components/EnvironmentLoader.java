package thu.robots.components;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Scanner;


public class EnvironmentLoader {

    private static final String OBJ_PART_SEPARATOR = ";";
    private static final int OBJ_PART_COUNT = 6;


    public static Environment loadFromFile(File file){
        Environment env = null;

        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Datei "+file+" konnte nicht gelesen werden.");
        }

        if(sc!=null) {
            if(!sc.hasNext()){
                throw new IllegalArgumentException("Datei "+file+" enthält kein gültiges Environment. Datei ist leer.");
            }

            int lineCount = 0;
            String name = null;

            while (sc.hasNextLine()) {
                lineCount++;
                String objStr = sc.nextLine().trim();
                if(objStr.isEmpty()|| objStr.startsWith("#")){
                    continue;
                }
                String[] parts = objStr.split(OBJ_PART_SEPARATOR);
                if(parts.length == 1){
                    // read name
                    name = parts[0].trim();
                }
                else if (parts.length ==2){
                    // read size and create environment
                    try {
                        int w = Integer.parseInt(parts[0].trim());
                        int h = Integer.parseInt(parts[1].trim());
                        env = new Environment(name, w, h);
                    }
                    catch(Exception e){
                        throw new IllegalArgumentException("Datei "+file+" enthält kein gültiges Environment. Name und/oder Größe ungültig: "+e);
                    }
                }
                else if(parts.length!=OBJ_PART_COUNT){
                    throw new IllegalArgumentException("Datei "+file+" enthält kein gültiges Environment. Anzahl der Elemente pro Objekt ist ungleich "+OBJ_PART_COUNT+": "+parts.length);
                }
                else {
                    if(env == null){
                        throw new IllegalArgumentException("Datei "+file+" enthält kein gültiges Environment. Name und/oder Größe nicht vorhanden!");
                    }
                    try {
                        int x = Integer.parseInt(parts[0].trim());
                        int y = Integer.parseInt(parts[1].trim());
                        int w = Integer.parseInt(parts[2].trim());
                        int len = Integer.parseInt(parts[3].trim());
                        double orientation = Double.parseDouble(parts[4].trim()) * Math.PI / 180.;
                        String colorStr = parts[5].trim();
                        Color color;
                        try {
                            Field field = Color.class.getField(colorStr.toUpperCase());
                            color = (Color) field.get(null);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Datei " + file + " enthält kein gültiges Environment. Ungültiger Wert für Farbe in Zeile " + lineCount);
                        }

                        EnvironmentObject obj = new EnvironmentObject(x, y, w, len, orientation, color);
                        env.addObject(obj);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Datei " + file + " enthält kein gültiges Environment. Ungültiger Wert in Zeile " + lineCount + ": " + e);
                    }
                }

            }
        }

        if(env!=null) {
            env.prepare();
        }

        return env;
    }
}
