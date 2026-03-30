package fnatnr;

import java.io.*;

public class SaveData {

    private static final String SAVE_FILE = "save.dat";

    public static void saveNightsUnlocked(int nights) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            writer.println(nights);
        } catch (IOException e) {
            System.out.println("Failed to save: " + e.getMessage());
        }
    }

    public static int loadNightsUnlocked() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            return Math.max(1, Integer.parseInt(reader.readLine().trim()));
        } catch (IOException | NumberFormatException e) {
            System.out.println("Failed to load save: " + e.getMessage());
            return 1;
        }
    }

    public static void deleteSave() {
        new File(SAVE_FILE).delete();
    }
}
