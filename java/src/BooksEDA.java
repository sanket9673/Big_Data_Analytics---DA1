import java.io.*;
import java.util.*;

/*
 * Books EDA using Java
 * Input  : ../python/books.csv
 * Output : CSV files in java/out/
 */

public class BooksEDA {

    static class Book {
        String title;
        double price;
        int rating;
        String availability;

        Book(String title, double price, int rating, String availability) {
            this.title = title;
            this.price = price;
            this.rating = rating;
            this.availability = availability;
        }
    }

    public static void main(String[] args) throws Exception {

        String inputCsv = "../python/books.csv";
        List<Book> books = loadCSV(inputCsv);

        System.out.println("Step 1: Load CSV");
        System.out.println("Total records loaded: " + books.size());

        saveCSV("out/01_loaded.csv", books);

        // Step 4: Missing values (simple academic rule)
        System.out.println("Step 4: Missing Values");
        books.removeIf(b -> b.title == null || b.title.isEmpty());
        saveCSV("out/04_missing_handled.csv", books);

        // Step 5: Remove duplicates (by title)
        System.out.println("Step 5: Duplicate Removal");
        Set<String> seen = new HashSet<>();
        books.removeIf(b -> !seen.add(b.title));
        saveCSV("out/05_deduplicated.csv", books);

        // Step 6: Descriptive statistics
        System.out.println("Step 6: Descriptive Statistics");
        double sum = 0;
        for (Book b : books) sum += b.price;
        double avg = books.isEmpty() ? 0 : sum / books.size();
        System.out.println("Average Price: " + avg);

        // Step 7: Sorting by price
        books.sort(Comparator.comparingDouble(b -> b.price));
        saveCSV("out/07_sorted_by_price.csv", books);

        // Step 8: Filtering (rating >= 4)
        List<Book> filtered = new ArrayList<>();
        for (Book b : books) {
            if (b.rating >= 4) filtered.add(b);
        }
        saveCSV("out/08_filtered_rating_above_4.csv", filtered);

        // Step 9: Normalization (Min-Max)
        normalizeAndSave(filtered, "out/09_normalized_minmax.csv");

        System.out.println("EDA completed successfully.");
    }

    // ---------------- CSV Helpers ----------------

    static List<Book> loadCSV(String path) throws Exception {
        List<Book> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine(); // skip header

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            String title = parts[0].replace("\"", "");
            double price = Double.parseDouble(parts[1]);
            int rating = Integer.parseInt(parts[2]);
            String availability = parts[3].replace("\"", "");
            list.add(new Book(title, price, rating, availability));
        }
        br.close();
        return list;
    }

    static void saveCSV(String path, List<Book> books) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write("Title,Price,Rating,Availability\n");
        for (Book b : books) {
            bw.write("\"" + b.title + "\"," + b.price + "," + b.rating + ",\"" + b.availability + "\"\n");
        }
        bw.close();
    }

    static void normalizeAndSave(List<Book> books, String path) throws Exception {
        if (books.isEmpty()) return;

        double min = books.stream().mapToDouble(b -> b.price).min().getAsDouble();
        double max = books.stream().mapToDouble(b -> b.price).max().getAsDouble();

        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write("Title,Price,NormalizedPrice\n");

        for (Book b : books) {
            double norm = (b.price - min) / (max - min);
            bw.write("\"" + b.title + "\"," + b.price + "," + norm + "\n");
        }
        bw.close();
    }
}
