import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    private static final String SAVES_DIR_PATH = ".";
    private static final String ARCHIEVE_NAME = "compressed saves.zip";
    private static final String PATH_SEPARATOR = System.getProperty("file.separator");

    public static void main(String[] args) throws Exception {
        GameProgress[] progress = {
                new GameProgress(10, 3, 80, 1),
                new GameProgress(9, 1, 15, 7),
                new GameProgress(8, 14, 2, 74)
        };

        List<String> savedFiles = new ArrayList<>();

        for (int i = 0; i < progress.length; i++) {
            String name = SAVES_DIR_PATH + PATH_SEPARATOR + String.format("save%d.dat", i + 1);

            saveGame(name, progress[i]);

            savedFiles.add(name);
        }

        zipFiles(savedFiles, SAVES_DIR_PATH + PATH_SEPARATOR + ARCHIEVE_NAME);

        for (String path : savedFiles) {
            File f = new File(path);

            if (!f.delete()) {
                System.out.printf("failed to remove file '%s'\n", path);
            }
        }
    }

    private static void zipFiles(List<String> files, String archievePath) throws IOException {
        try (ZipOutputStream archieve = new ZipOutputStream(
                new FileOutputStream(archievePath))) {

            for (String path : files) {
                File f = new File(path);

                ZipEntry entry = new ZipEntry(f.getName());

                archieve.putNextEntry(entry);

                try (FileInputStream savedFile = new FileInputStream(f)) {
                    for (int b = savedFile.read(); b != -1; b = savedFile.read()) {
                        archieve.write(b);
                    }
                } finally {
                    archieve.closeEntry();
                }
            }
        }
    }

    private static void saveGame(String filePath, GameProgress proress) throws IOException {
        try (ObjectOutputStream s = new ObjectOutputStream(new FileOutputStream(filePath))) {
            s.writeObject(proress);
        } catch (IOException e) {
            throw e;
        }
    }
}
