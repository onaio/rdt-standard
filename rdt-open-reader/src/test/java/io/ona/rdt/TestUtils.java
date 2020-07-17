package io.ona.rdt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vincent Karuri on 15/11/2019
 */
public class TestUtils {

    public static String getBasePackageFilePath() {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    protected void createTestFile() throws IOException {
        if (!testFileExists()) {
            PrintWriter writer = new PrintWriter(getTestFilePath(), "UTF-8");
            writer.println("The first line");
            writer.close();
        }
    }

    private boolean testFileExists() {
        return fileExists(getTestFilePath());
    }

    public static boolean fileExists(String filePath) {
        return getFile(filePath) != null;
    }

    private static File getFile(String filePath) {
        File file = new File(filePath);
        return file.exists() ? file : null;
    }

    public static String getTestFilePath() {
        return getBasePackageFilePath() + "/src/test/java/io/ona/rdt/";
    }

    public static Date getDateWithOffset(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, offset);
        return calendar.getTime();
    }

    public static void setStaticFinalField(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    public static void cleanUpFiles(String filePath) {
        File file = getFile(filePath);
        if (file != null) {
            file.delete();
        }
    }
}
