// File: src/main/java/com/islandbreeze/hotelmgmtsystem/service/FileBasedService.java

package com.hotelmanagementsystem.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An abstract generic service to handle common file-based CRUD operations.
 * Subclasses must implement the conversion logic to and from CSV format.
 * @param <T> The model type this service will manage (e.g., User, Booking).
 */
public abstract class FileBasedService<T> {

    private final Path filePath;
    private static final String DATA_DIRECTORY = "data";

    public FileBasedService(String fileName) {
        try {
            // Ensure the 'data' directory exists
            Files.createDirectories(Paths.get(DATA_DIRECTORY));
            this.filePath = Paths.get(DATA_DIRECTORY, fileName);
            // Ensure the data file exists
            if (!Files.exists(this.filePath)) {
                Files.createFile(this.filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize file-based service for: " + fileName, e);
        }
    }

    /**
     * Reads all lines from the file and converts them to a list of objects.
     * @return A list of objects of type T.
     */
    protected List<T> readFromFile() {
        try {
            return Files.lines(filePath)
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::fromCsv)
                    .filter(Objects::nonNull) // Filter out any nulls from malformed lines
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading from file " + filePath + ": " + e.getMessage());
            return new ArrayList<>(); // Return an empty list on error
        }
    }

    /**
     * Writes a list of objects to the file, overwriting existing content.
     * @param items The list of objects to write.
     */
    protected void writeToFile(List<T> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), false))) { // false to overwrite
            for (T item : items) {
                writer.write(toCsv(item));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Abstract method to define how a CSV string is converted to an object.
     * Must be implemented by subclasses.
     * @param csv The comma-separated string line.
     * @return An object of type T.
     */
    protected abstract T fromCsv(String csv);

    /**
     * Abstract method to define how an object is converted to a CSV string.
     * Must be implemented by subclasses.
     * @param item The object to convert.
     * @return A comma-separated string representation.
     */
    protected abstract String toCsv(T item);
}