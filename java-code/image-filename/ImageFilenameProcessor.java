import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ImageFilenameProcessor {
    private static final Map<String, String> MONTH_DESC = Map.ofEntries(
        Map.entry("01", "Jan"),
        Map.entry("02", "Feb"),
        Map.entry("03", "Mar"),
        Map.entry("04", "Apr"),
        Map.entry("05", "May"),
        Map.entry("06", "Jun"),
        Map.entry("07", "Jul"),
        Map.entry("08", "Aug"),
        Map.entry("09", "Sep"),
        Map.entry("10", "Oct"),
        Map.entry("11", "Nov"),
        Map.entry("12", "Dec")
    );

    public static String getDateFromFilename(String filename) {
        String dateText;
        if (filename.contains("-")) {
            dateText = filename.substring(0, 4) + filename.substring(5, 7) + filename.substring(11, 13);
        } else {
            dateText = filename.substring(0, 8);
        }

        if (dateText.length() != 8 || !dateText.chars().allMatch(Character::isDigit)) {
            return null;
        }

        try {
            LocalDate.parse(dateText, DateTimeFormatter.BASIC_ISO_DATE);
            return dateText;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String getFilenameWithoutSequence(String filename) {
        String filenameWithoutExtension = stripExtension(filename);
        String[] nameParts = filenameWithoutExtension.split(" ");
        if (nameParts.length <= 1) {
            return filenameWithoutExtension;
        }
        return String.join(" ", java.util.Arrays.copyOf(nameParts, nameParts.length - 1));
    }

    public static String getFolderDescription(String filename) {
        String filenameWithoutSequence = getFilenameWithoutSequence(filename);
        int firstSpaceIndex = filenameWithoutSequence.indexOf(' ');
        if (firstSpaceIndex < 0) {
            return "";
        }
        return filenameWithoutSequence.substring(firstSpaceIndex + 1);
    }

    public static String formatFullFilename(String dateText, Path folder) {
        String folderDescription = getFolderDescription(folder.getFileName().toString());
        LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.BASIC_ISO_DATE);
        String monthAbbreviation = MONTH_DESC.get(dateText.substring(4, 6));
        StringBuilder filename = new StringBuilder();
        filename.append(String.format("%04d-%02d%s-%02d", date.getYear(), date.getMonthValue(), monthAbbreviation, date.getDayOfMonth()));
        if (!folderDescription.isEmpty()) {
            filename.append(" ").append(folderDescription);
        }
        return filename.toString();
    }

    public static void processFolder(Path folder, List<String> filenames) throws IOException {
        List<Path> files;
        if (filenames == null) {
            files = Files.list(folder)
                .filter(Files::isRegularFile)
                .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                .collect(Collectors.toList());
        } else {
            files = filenames.stream()
                .map(folder::resolve)
                .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                .collect(Collectors.toList());
        }

        List<DatedFile> datedFiles = new ArrayList<>();
        for (Path path : files) {
            String dateText = getDateFromFilename(path.getFileName().toString());
            if (dateText != null) {
                datedFiles.add(new DatedFile(path, dateText));
            }
        }

        if (datedFiles.isEmpty()) {
            return;
        }

        boolean splitByDate = getFolderDescription(datedFiles.get(0).path().getFileName().toString()).isEmpty();

        List<StagedFile> stagedFiles = new ArrayList<>();
        for (DatedFile datedFile : datedFiles) {
            String filename = datedFile.path().getFileName().toString();
            String stagedName = filename.substring(0, filename.lastIndexOf('.')) + "_" + getExtension(filename);
            stagedFiles.add(new StagedFile(datedFile.path(), datedFile.dateText(), stagedName));
        }

        stagedFiles.sort(Comparator.comparing(StagedFile::stagedName));

        Map<String, Integer> dateCounts = new HashMap<>();
        for (StagedFile stagedFile : stagedFiles) {
            dateCounts.put(stagedFile.dateText(), dateCounts.getOrDefault(stagedFile.dateText(), 0) + 1);
        }

        Map<String, Integer> dateSequences = new HashMap<>();
        int overallSequence = 0;
        int overallWidth = Math.max(2, String.valueOf(stagedFiles.size()).length());
        List<RenamePlan> renamePlan = new ArrayList<>();

        for (StagedFile stagedFile : stagedFiles) {
            int sequence;
            int sequenceWidth;
            if (splitByDate) {
                sequence = dateSequences.getOrDefault(stagedFile.dateText(), 0) + 1;
                dateSequences.put(stagedFile.dateText(), sequence);
                sequenceWidth = Math.max(2, String.valueOf(dateCounts.get(stagedFile.dateText())).length());
            } else {
                overallSequence += 1;
                sequence = overallSequence;
                sequenceWidth = overallWidth;
            }

            String filenameWithoutSequence;
            if (stagedFile.stagedName().contains("-")) {
                filenameWithoutSequence = getFilenameWithoutSequence(stagedFile.stagedName());
            } else {
                filenameWithoutSequence = formatFullFilename(stagedFile.dateText(), folder);
            }

            String extension = getExtension(stagedFile.stagedName());
            String newName = filenameWithoutSequence + " " + String.format("%0" + sequenceWidth + "d", sequence) + extension;
            renamePlan.add(new RenamePlan(stagedFile.path(), newName));
        }

        Set<String> sourceNames = new HashSet<>();
        for (StagedFile stagedFile : stagedFiles) {
            sourceNames.add(stagedFile.path().getFileName().toString());
        }

        Set<String> destinationNames = new HashSet<>();
        for (RenamePlan renamePlanEntry : renamePlan) {
            String destinationName = renamePlanEntry.newName();
            if (!destinationNames.add(destinationName)) {
                throw new IOException("Multiple files in " + folder + " would be renamed to " + destinationName);
            }
            Path destination = folder.resolve(destinationName);
            if (Files.exists(destination) && !sourceNames.contains(destinationName)) {
                throw new IOException("Rename destination already exists: " + destination);
            }
        }

        List<TemporaryRename> temporaryFiles = new ArrayList<>();
        for (RenamePlan renamePlanEntry : renamePlan) {
            Path temporary = folder.resolve(".imagefilename-" + UUID.randomUUID() + ".tmp");
            while (Files.exists(temporary)) {
                temporary = folder.resolve(".imagefilename-" + UUID.randomUUID() + ".tmp");
            }
            Files.move(renamePlanEntry.path(), temporary);
            temporaryFiles.add(new TemporaryRename(renamePlanEntry.path(), temporary, renamePlanEntry.newName()));
        }

        for (TemporaryRename temporaryRename : temporaryFiles) {
            Path destination = temporaryRename.path().resolveSibling(temporaryRename.newName());
            Files.move(temporaryRename.temporaryPath(), destination);
        }
    }

    private static String stripExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex < 0) {
            return filename;
        }
        return filename.substring(0, lastDotIndex);
    }

    private static String getExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex < 0) {
            return "";
        }
        return filename.substring(lastDotIndex);
    }

    private record DatedFile(Path path, String dateText) {}
    private record StagedFile(Path path, String dateText, String stagedName) {}
    private record RenamePlan(Path path, String newName) {}
    private record TemporaryRename(Path path, Path temporaryPath, String newName) {}
}
