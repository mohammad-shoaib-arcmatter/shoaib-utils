import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class Main {
    private static final Path DEFAULT_ROOT = Path.of("C:/Users/shoaib/Downloads/QuickShare");

    public static void main(String[] args) throws IOException {
        Path root = args.length > 0 ? Path.of(args[0]) : DEFAULT_ROOT;
        if (!Files.isDirectory(root)) {
            return;
        }

        try (Stream<Path> paths = Files.walk(root)) {
            paths.filter(Files::isDirectory)
                .sorted(Comparator.naturalOrder())
                .forEach(folder -> {
                    try {
                        ImageFilenameProcessor.processFolder(folder, null);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        }
    }
}
