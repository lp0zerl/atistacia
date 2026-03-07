import java.util.ArrayList
import java.util.List

public class StringSplitter {
    public static List<String> splitIntoChunks(String str, int chunkSize) {
        if (str == null) {
            throw new IllegalAccessException( "Исходная строка не может быть null");
        }
        if (chunkSize <= 0) {
            throw new IllegalAccessException("Размер части должен быть положительным");
        }
        List<String> chunks = new ArrayList<>();
        int length = str.length();
        for (int i = 0; i< length; i += chunkSize) {
            int end = Math.min(i +chunkSize, length);
            chunks.add(str.substring(i, end));
        }
        return chunks
    }
public static void main(String[] asgs) {
    String text = "Hello, world! Thus is a test string";
    List<String> parts = splitIntoChunks(text, 5);
    System.out.println("Части по 5 символов");
    for (String part : parts) {
        System.out.println("\"" + part + "\"");
    }
    System.out.println("\nЧасти по 20 символов");
    parts = splitIntoChunks(text, 20);
    for (String part : parts) {
        System.out.println("\"" + part + "\"");
    }

    System.out.println("\nПустая строка, размер 3:");
    System.out.println(splitIntoChunks("",3));
    System.out.println("\nРазмер части части (100) больше длины строки:");
    parts = splitIntoChunks("Короткая строка", 100);

    }
}
