package com.example.demo.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class CSVReader {

    public String[] getContent(String pathname) {
        var list = new ArrayList<String>();

        var file = new File(pathname);
        try (Scanner scanner = new Scanner(file)) {
            skipHeaderRow(scanner);

            while (scanner.hasNextLine()) {
                var csvRawRow = scanner.nextLine();

                list.add(csvRawRow);
            }
        } catch (FileNotFoundException e) {
            //TODO: handle exception, file not found
            throw new RuntimeException(e);
        }
        return list.toArray(new String[0]);
    }

    private void skipHeaderRow(Scanner scanner) {
        scanner.nextLine();
    }
}
