package com.example.demo;

import com.example.demo.csv.CSVParser;
import com.example.demo.user.UserService;

public class Job implements Runnable {

    private final String row;
    private DemoApplication.ProcessedRecord _result;

    public Job(String row) {
        this.row = row;
    }

    @Override
    public void run() {
        _result = processRawRow(row);
    }

    private DemoApplication.ProcessedRecord processRawRow(String row) {
        var orderIntegration = CSVParser.toOrderIntegration(row);

        var userService = new UserService();
        var user = userService.addUser(orderIntegration);

        return new DemoApplication.ProcessedRecord(
                user.getPid(),
                orderIntegration.orderId(),
                orderIntegration.supplierPid()
        );
    }

    public DemoApplication.ProcessedRecord getResult() {
        return _result;
    }

    @Override
    public String toString() {
        return this.row;
    }
}
