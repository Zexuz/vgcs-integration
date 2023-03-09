package com.example.demo;

import com.example.demo.csv.CSVReader;
import com.google.gson.GsonBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@SpringBootApplication
public class DemoApplication {
    private static final Logger logger = Logger.getLogger(DemoApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    private static final String csvInput = System.getenv("DEMO_CSV_INPUT");
    private static final String jsonOut = System.getenv("DEMO_PROCESSED_ORDERS");


    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            var cvsReader = new CSVReader();
            var rows = cvsReader.getContent(csvInput);

            var jobs = new ArrayList<Job>();
            try {
                ExecutorService executor = Executors.newFixedThreadPool(10);
                for (var row : rows) {
                    Job worker = new Job(row);
                    executor.execute(worker);
                    jobs.add(worker);
                }
                executor.shutdown();
                while (!executor.isTerminated()) {
                }
                logger.log(java.util.logging.Level.INFO, "Finished all threads");
            } catch (Exception e) {
                logger.log(java.util.logging.Level.INFO, e.getMessage());
            }

            writeRecordsToFile(jobs.stream().map(Job::getResult).toArray(ProcessedRecord[]::new));
        };
    }

    private static void writeRecordsToFile(ProcessedRecord[] processedRecords) {
        String json = convertToJsonString(processedRecords);
        logger.log(java.util.logging.Level.INFO, json);

        try (var writer = new java.io.FileWriter(jsonOut)) {
            writer.write(json);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertToJsonString(ProcessedRecord[] processedRecords) {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(processedRecords);
    }


    record ProcessedRecord(
            String userPid,
            String orderPid,
            String supplierPid
    ) {
    }

}

