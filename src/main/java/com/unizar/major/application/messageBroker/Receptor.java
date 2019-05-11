package com.unizar.major.application.messageBroker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.unizar.major.application.dtos.BookingDtoReturn;
import com.unizar.major.application.dtos.LoginDto;
import com.unizar.major.application.dtos.UserDto;
import com.unizar.major.application.service.BookingService;
import com.unizar.major.application.service.UserService;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.User;
import com.unizar.major.domain.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapted from <https://github.com/UNIZAR-62227-TMDAD/messaging>
 */

@Component
public class Receptor implements CommandLineRunner {

    @Value("${app.messageBroker.default-requests-queue:}")
    private String REQUEST_QUEUE;

    @Value("${app.messageBroker.default-responses-queue:}")
    private String RESPONSE_QUEUE;

    private static Logger logger = LoggerFactory.getLogger(Receptor.class);

    @Value("${app.messageBroker.user:}")
    private String USER;

    @Value("${app.messageBroker.password:}")
    private String PASSWORD;

    @Value("${app.messageBroker.amqp-url:}")
    private String AMQP_URL;

    @Autowired
    BookingService bookingService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {

        Channel channel;
        QueueingConsumer consumer;


        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(USER);
        factory.setPassword(PASSWORD);

        String amqpURL = AMQP_URL;

        try {
            factory.setUri(amqpURL);
        } catch (Exception e) {
            logger.error(" [*] AMQP broker not found in " + amqpURL);
            System.exit(-1);
        }
        logger.info("AMQP broker found in " + amqpURL);

        Connection connection = factory.newConnection();

        channel = connection.createChannel();
        channel.queueDeclare(REQUEST_QUEUE, false, false, false, null);
        channel.queueDeclare(RESPONSE_QUEUE, false, false, false, null);
        consumer = new QueueingConsumer(channel);


        channel.basicConsume(REQUEST_QUEUE, true, consumer);

        //noinspection InfiniteLoopStatement
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            String[] messageParts = message.split(";");
            /*logger.info("Message received: \n\tResponse queue: " + messageParts[0] +
                    "\n\tFunction: " + messageParts[1] +
                    "\n\tArguments: " + (messageParts.length == 3 ? messageParts[2] : "No args"));*/

            String retMessage;
            switch (messageParts[1]) {
                case "login":
                    retMessage = login(messageParts[2]);
                    break;
                case "createNewUser":
                    retMessage = createNewUser(messageParts[2]);
                    break;
                case "fetchUserByID":
                    retMessage = fetchUserByID(messageParts[2]);
                    break;
                case "deleteUserByID":
                    retMessage = deleteUserByID(messageParts[2]);
                    break;
                case "updateUserByID":
                    retMessage = updateUserByID(messageParts[2]);
                    break;
                case "fetchUserBookingsByID":
                    retMessage = fetchUserBookingsByID(messageParts[2]);
                    break;
                case "getUserIDOfBooking":
                    retMessage = "200;155";
                    break;
                case "getBookingById":
                    retMessage = "200" + ";" + "{ \"id\": 2, \"isPeriodic\": false, \"reason\": \"charla tfg9\", \"period\": [ { \"startDate\": \"2019-04-09T18:00:00.000+0000\", \"endDate\": \"2019-04-09T20:00:00.000+0000\" } ], \"state\": \"inicial\", \"active\": true, \"periodRep\": null, \"finalDate\": null}";
                    break;
                default:
                    retMessage = "500;null";
                    logger.error("Message not recognized");
                    break;
            }

            logger.info(" Message send: {" + retMessage + "}");
            channel.basicPublish("", messageParts[0], null, retMessage.getBytes());
        }
    }

    private String login(String message) {
        logger.info("login message received{" + message + "}");
        try {
            Optional logUser = userService.loginUser(new ObjectMapper().readValue(message, LoginDto.class));

            if (logUser.isPresent()) {
                return "200;" + UUID.randomUUID().toString() + "::" + ((User) logUser.get()).getRol().toUpperCase() + "::" + ((User) logUser.get()).getId();
            }
            return "401;null";
        } catch (IOException e) {
            logger.error("login", e);
            return "500;null";
        }
    }

    private String createNewUser(String message) {
        logger.info("createNewUser message received{" + message + "}");
        try {
            UserDto inUserDto = new ObjectMapper().readValue(message, UserDto.class);
            if (!userRepository.findByUserName(inUserDto.getUserName()).isPresent() &&
                    !userRepository.findByEmail(inUserDto.getEmail()).isPresent()) {
                Boolean status = userService.createUser(inUserDto);
                return (status ? "201;null" : "500;null");
            }
            return "409;null";
        } catch (IOException e) {
            logger.error("createNewUser", e);
            return "500;null";
        }
    }

    private String fetchUserByID(String message) {
        logger.info("fetchUserByID message received{" + message + "}");

        try {
            Optional fetchedUser = userService.getUser(Long.parseLong(message));
            if (fetchedUser.isPresent()) {
                return "200;" + new ObjectMapper().writeValueAsString(new ModelMapper().map(fetchedUser.get(), UserDto.class));
            }
            return "404;null";
        } catch (Exception e) {
            logger.error("fetchUserByID", e);
            return "500;null";
        }
    }

    private String deleteUserByID(String message) {
        logger.info("deleteUserByID message received{" + message + "}");
        try {
            Boolean status = userService.deleteUser(Long.parseLong(message));
            return (status ? "202;null" : "404;null");
        } catch (Exception e) {
            logger.error("deleteUserByID", e);
            return "500;null";
        }
    }

    private String updateUserByID(String message) {
        logger.info("updateUserByID message received{" + message + "}");
        String[] args = message.split("::");
        try {
            Boolean status = userService.updateUser(Long.parseLong(args[0]), new ObjectMapper().readValue(args[1], UserDto.class));
            return (status ? "202;null" : "404;null");
        } catch (Exception e) {
            logger.error("deleteUserByID", e);
            return "500;null";
        }
    }

    private String fetchUserBookingsByID(String message) {
        logger.info("fetchUserBookingsByID message received{" + message + "}");
        try {
            List<Booking> bookings = userService.getBookings(Long.parseLong(message));
            if(bookings == null) {
                return "404;null";
            }
            return "200;" + new ObjectMapper().writeValueAsString(convertBookingListIntoDto(bookings));
        } catch (Exception e) {
            logger.error("fetchUserBookingsByID", e);
            return "500;null";
        }


    }

    private List<BookingDtoReturn> convertBookingListIntoDto(List<Booking> input) {
        List<BookingDtoReturn> output = new ArrayList<>();
        for(Booking booking : input) {
            output.add(new ModelMapper().map(booking, BookingDtoReturn.class));
        }
        return output;
    }
/*
    private String getUserIDOfBooking(String message) {

    }
*/
}
