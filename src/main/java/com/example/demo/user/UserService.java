package com.example.demo.user;

import com.example.demo.grpc.GRPC_CLIENTS;
import com.example.demo.grpc.GrpcChannelFactory;
import com.example.demo.OrderIntegration;
import com.google.protobuf.StringValue;
import user.User;
import user.UserServiceGrpc;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;


public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private final UserServiceGrpc.UserServiceBlockingStub _userClient;

    public UserService() {
        var userChannel = GrpcChannelFactory.createChannel(GRPC_CLIENTS.USER);
        _userClient = UserServiceGrpc.newBlockingStub(userChannel);
    }

    public User.UserResponse addUser(OrderIntegration orderIntegration) {
        var request = Converter.parse(orderIntegration);
        var res = _userClient.createUser(request);

        logger.log(INFO, "User created with id: " + res.getPid());

        return res;
    }

}

class Converter {

    public static User.CreateUserRequest parse(OrderIntegration orderIntegration) {
        var address = User.ShippingAddress.newBuilder()
                .setCountry(StringValue.of(orderIntegration.country()))
                .setAddress(StringValue.of(orderIntegration.shippingAddress()))
                .build();

        var paymentMethods = User.PaymentMethod.newBuilder()
                .setCreditCardNumber(StringValue.of(orderIntegration.creditCardNumber()))
                .setCreditCardType(StringValue.of(orderIntegration.creditCardType()))
                .build();

        return User.CreateUserRequest.newBuilder()
                .setFullName(StringValue.of(orderIntegration.firstName()))
                .setEmail(orderIntegration.email())
                .setAddress(address)
                .addPaymentMethods(paymentMethods)
                .build();
    }


}

