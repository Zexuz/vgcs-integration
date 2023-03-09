package com.example.demo.csv;

import com.example.demo.OrderIntegration;

public class CSVParser {
    public static OrderIntegration toOrderIntegration(String line) {
        var values = line.split(",");

        return new OrderIntegration(
                values[0],
                values[1],
                values[2],
                values[3],
                values[4],
                values[5],
                values[6],
                values[7],
                values[8],
                values[9],
                values[10],
                values[11],
                values[12],
                values[13],
                values[14]
        );
    }
}
