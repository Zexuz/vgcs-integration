package com.example.demo.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;


public class GrpcChannelFactory {

    private static final String ORDER_SERVICE_URL_ENV_NAME = "DEMO_ORDER_SERVICE_URL";
    private static final String PRODUCT_SERVICE_URL_ENV_NAME = "DEMO_PRODUCT_SERVICE_URL";
    private static final String USER_SERVICE_URL_ENV_NAME = "DEMO_USER_SERVICE_URL";
    private static final String SUPPLIER_SERVICE_URL_ENV_NAME = "DEMO_SUPPLIER_SERVICE_URL";

    private static final ConcurrentHashMap<GRPC_CLIENTS, Channel> channels = new ConcurrentHashMap<>();
    private static final Lock lock = new ReentrantLock();


    // Create a logger
    private static final Logger logger = Logger.getLogger(GrpcChannelFactory.class.getName());


    public static Channel createChannel(GRPC_CLIENTS client) {
        return switch (client) {
            case ORDER -> getOrCreateChannel(GRPC_CLIENTS.ORDER);
            case PRODUCT -> getOrCreateChannel(GRPC_CLIENTS.PRODUCT);
            case USER -> getOrCreateChannel(GRPC_CLIENTS.USER);
            case SUPPLIER -> getOrCreateChannel(GRPC_CLIENTS.SUPPLIER);
        };
    }

    private static String getTarget(GRPC_CLIENTS client) {
        // TODO: Make a service to get the values from env files instead of hardcoding them
        return switch (client) {
            case ORDER -> System.getenv(ORDER_SERVICE_URL_ENV_NAME);
            case PRODUCT -> System.getenv(PRODUCT_SERVICE_URL_ENV_NAME);
            case USER -> System.getenv(USER_SERVICE_URL_ENV_NAME);
            case SUPPLIER -> System.getenv(SUPPLIER_SERVICE_URL_ENV_NAME);
        };
    }


    private static Channel getOrCreateChannel(GRPC_CLIENTS client) {
        lock.lock();
        try {
            if (channels.containsKey(client)) {
                // TODO: Verify channel is still active
                return channels.get(client);
            }

            var target = getTarget(client);
            var channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
            logger.log(java.util.logging.Level.INFO, "Created channel for " + client + " at " + target);
            channels.put(client, channel);

            return channel;
        } finally {
            lock.unlock();
        }
    }
}
