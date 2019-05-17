package com.unizar.major.application.messageBroker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.unizar.major.application.dtos.*;
import com.unizar.major.application.service.BookingService;
import com.unizar.major.application.service.EmailService;
import com.unizar.major.application.service.SpaceService;
import com.unizar.major.application.service.UserService;
import com.unizar.major.domain.Booking;
import com.unizar.major.domain.Space;
import com.unizar.major.domain.User;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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
    SpaceService spaceService;

    @Autowired
    EmailService emailService;

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
                    retMessage = getUserIDOfBooking(messageParts[2]);
                    break;
                case "getBookingById":
                    retMessage = getBookingById(messageParts[2]);
                    break;
                case "createNewBooking":
                    retMessage = createNewBooking(messageParts[2]);
                    break;
                case "createNewPeriodicBooking":
                    retMessage = createNewPeriodicBooking(messageParts[2]);
                    break;
                case "deleteBookingById":
                    retMessage = deleteBookingById(messageParts[2]);
                    break;
                case "putBookingById":
                    retMessage = putBookingById(messageParts[2]);
                    break;
                case "fetchAllBookings":
                    retMessage = fetchAllBookings();
                    break;
                case "fetchPendingBookings":
                    retMessage = fetchPendingBookings();
                    break;
                case "validateBookingById":
                    retMessage = validateBookingById(messageParts[2]);
                    break;
                case "cancelBookingById":
                    retMessage = cancelBookingById(messageParts[2]);
                    break;
                case "createCSVBooking":
                    retMessage = createCSVBooking(messageParts[2]);
                    break;
                case "fetchAllSpaces":
                    retMessage = fetchAllSpaces();
                    break;
                case "fetchSpaceByID":
                    retMessage = fetchSpaceByID(messageParts[2]);
                    break;
                case "putSpaceByID":
                    retMessage = putSpaceByID(messageParts[2]);
                    break;
                case "fetchSpaceBookingsByID":
                    retMessage = fetchSpaceBookingsByID(messageParts[2]);
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

    private BookingDtoReturn convertBookingIntoDto(Booking booking) {
        BookingDtoReturn bdr = new BookingDtoReturn();
        bdr.setId(booking.getId());
        bdr.setIsPeriodic(booking.isIsPeriodic());
        bdr.setReason(booking.getReason());
        bdr.setPeriod(booking.getPeriod());
        bdr.setState(booking.getState());
        bdr.setActive(booking.isActive());
        bdr.setPeriodRep(booking.getPeriodRep());
        bdr.setFinalDate(booking.getFinalDate());
        List<Integer> spaces = new ArrayList<>();
        for (Space space: booking.getSpaces()) {
            spaces.add(space.getGid());
        }
        bdr.setSpaces(spaces);
        return bdr;
    }

    private List<BookingDtoReturn> convertBookingListIntoDto(List<Booking> input) {
        List<BookingDtoReturn> output = new ArrayList<>();
        for(Booking booking : input) {
            output.add(convertBookingIntoDto(booking));
            //output.add(new ModelMapper().map(booking, BookingDtoReturn.class));
        }
        return output;
    }

    private SpaceDto convertSpaceIntoDto(Space space) {
        SpaceDto spaceDto = new SpaceDto();
        spaceDto.setId(space.getId());
        spaceDto.setGid(space.getGid());
        spaceDto.setLayer(space.getLayer());
        spaceDto.setSubclasses(space.getSubclasses());
        spaceDto.setExtendeden(space.getExtendeden());
        spaceDto.setLinetype(space.getLinetype());
        spaceDto.setEntityhand(space.getEntityhand());
        spaceDto.setText(space.getText());
        spaceDto.setArea(space.getArea());
        spaceDto.setPerimeter(space.getPerimeter());
        spaceDto.setDataSpace(space.getDataSpace());
        spaceDto.setMaterials(space.getMaterials());
        spaceDto.setBookings(convertBookingListIntoDto(space.getBookings()));
        return spaceDto;
    }

    private List<SpaceDto> convertSpaceListIntoDto(List<Space> input) {
        List<SpaceDto> output = new ArrayList<>();
        for(Space space : input) {
            output.add(convertSpaceIntoDto(space));
        }
        return output;
    }

    private String login(String message) {
        logger.info("login message received{" + message + "}");
        try {
            Optional logUser = userService.loginUser(new ObjectMapper().readValue(message, LoginDto.class));

            if (logUser.isPresent()) {
                return "200;" + UUID.randomUUID().toString() + "::" + ((User) logUser.get()).getRol() + "::" + ((User) logUser.get()).getId();
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
            if (!userService.existsUserInSystem(inUserDto)) {
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
                ((User) fetchedUser.get()).setPassword(null);
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

    private String getUserIDOfBooking(String message) {
        logger.info("getUserIDOfBooking message received{" + message + "}");
        try {
            long fetchedID = bookingService.getBookingOwnerByID(Long.parseLong(message));
            return (fetchedID != -1 ? "200;" + fetchedID : "404;null");
        } catch (Exception e) {
            logger.error("getUserIDOfBooking", e);
            return "500;null";
        }
    }

    private String getBookingById(String message) {
        logger.info("getBookingById message received{" + message + "}");
        try {
            Optional booking = bookingService.getBookingById(Long.parseLong(message));
            if(booking.isPresent()) {
                return "200;" + new ObjectMapper().writeValueAsString(new ModelMapper().map(convertBookingIntoDto((Booking) booking.get()), BookingDtoReturn.class));
            } else {
                return "404;null";
            }
        } catch (Exception e) {
            logger.error("getBookingById", e);
            return "500;null";
        }
    }

    private String createNewBooking(String message) {
        logger.info("createNewBooking message received{" + message + "}");
        String[] args = message.split("::");
        try {
            Boolean status = bookingService.createNewBooking(Long.parseLong(args[0]), new ObjectMapper().readValue(args[1], BookingDto.class));
            return (status ? "201;null" : "404;null");
        } catch (Exception e) {
            logger.error("createNewBooking", e);
            return "500;null";
        }
    }

    private String createNewPeriodicBooking(String message) {
        logger.info("createNewPeriodicBooking message received{" + message + "}");
        String[] args = message.split("::");
        try {
            Boolean status = bookingService.createNewPeriodicBooking(Long.parseLong(args[0]), new ObjectMapper().readValue(args[1], BookingDto.class));
            return (status ? "201;null" : "404;null");
        } catch (Exception e) {
            logger.error("createNewPeriodicBooking", e);
            return "500;null";
        }
    }

    private String deleteBookingById(String message) {
        logger.info("deleteBookingById message received{" + message + "}");
        try {
            Boolean status = bookingService.deleteBooking(Long.parseLong(message));
            return (status ? "202;null" : "404;null");
        } catch (Exception e) {
            logger.error("deleteBookingById", e);
            return "500;null";
        }
    }

    private String putBookingById(String message) {
        logger.info("putBookingById message received{" + message + "}");
        String[] args = message.split("::");
        try {
            Boolean status = bookingService.updateBooking(Long.parseLong(args[0]), new ObjectMapper().readValue(args[1], BookingDto.class));
            return (status ? "202;null" : "404;null");
        } catch (Exception e) {
            logger.error("putBookingById", e);
            return "500;null";
        }
    }

    private String fetchAllBookings() {
        logger.info("fetchAllBookings message received{ no args }");
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            return "200;" + new ObjectMapper().writeValueAsString(convertBookingListIntoDto(bookings));
        } catch (Exception e) {
            logger.error("fetchAllBookings", e);
            return "500;null";
        }
    }

    private String fetchPendingBookings() {
        logger.info("fetchPendingBookings message received{ no args }");
        try {
            List<Booking> bookings = bookingService.getBookingPending();
            return "200;" + new ObjectMapper().writeValueAsString(convertBookingListIntoDto(bookings));
        } catch (Exception e) {
            logger.error("fetchPendingBookings", e);
            return "500;null";
        }
    }

    private String validateBookingById(String message) {
        logger.info("validateBookingById message received{" + message + "}");
        try {
            Optional<Booking> booking = bookingService.validateBooking(Long.parseLong(message));
            if(booking.isPresent()) {
                long userID = bookingService.getBookingOwnerByID(booking.get().getId());
                if( userID != -1) {
                    Optional<User> user = userService.getUser(userID);
                    if(user.isPresent()) {
                        emailService.sendSimpleEmail(user.get().getEmail(), "Reserva confirmada", "La siguiente reserva acaba de ser confirmada" + new ObjectMapper().writeValueAsString(convertBookingIntoDto(booking.get())));
                        return "200;null";
                    }
                }
            }
            return "404;null";
        } catch (Exception e) {
            logger.error("validateBookingById", e);
            return "500;null";
        }
    }

    private String cancelBookingById(String message) {
        logger.info("cancelBookingById message received{" + message + "}");
        try {
            Optional<Booking> booking = bookingService.cancelBooking(Long.parseLong(message));
            if(booking.isPresent()) {
                long userID = bookingService.getBookingOwnerByID(booking.get().getId());
                if (userID != -1) {
                    Optional<User> user = userService.getUser(userID);
                    if (user.isPresent()) {
                        emailService.sendSimpleEmail(user.get().getEmail(), "Reserva Denegada", "La siguiente reserva acaba de ser denegada" + new ObjectMapper().writeValueAsString(convertBookingIntoDto(booking.get())));
                        return "200;null";
                    }
                }
            }
            return "404;null";
        } catch (Exception e) {
            logger.error("cancelBookingById", e);
            return "500;null";
        }
    }

    private String createCSVBooking(String message) {
        logger.info("createCSVBooking message received{" + message + "}");
        String[] args = message.split("::");
        try {
            Boolean status = bookingService.createNewBooking(Long.parseLong(args[0]), new ObjectMapper().readValue(args[1], Bookingcsv.class));
            return (status ? "201;null" : "404;null");
        } catch (Exception e) {
            logger.error("createCSVBooking", e);
            return "500;null";
        }
    }

    private String fetchAllSpaces() {
        logger.info("fetchAllSpaces message received{ no args }");
        try {
            List<Space> spaces = spaceService.getAllSpaces();
            return "200;" + new ObjectMapper().writeValueAsString(convertSpaceListIntoDto(spaces));
        } catch (Exception e) {
            logger.error("fetchAllSpaces", e);
            return "500;null";
        }
    }

    private String fetchSpaceByID(String message) {
        logger.info("fetchSpaceByID message received{" + message + "}");
        try {
            Optional space = spaceService.getSpaceByGid(Integer.parseInt(message));
            if(space.isPresent()) {
                return "200;" + new ObjectMapper().writeValueAsString(new ModelMapper().map(convertSpaceIntoDto((Space) space.get()), SpaceDto.class));
            } else {
                return "404;null";
            }
        } catch (Exception e) {
            logger.error("fetchSpaceByID", e);
            return "500;null";
        }
    }

    private String putSpaceByID(String message) {
        logger.info("putSpaceByID message received{" + message + "}");
        String[] args = message.split("::");
        try {
            Boolean status = spaceService.updateInfoSpace(Integer.parseInt(args[0]), new ObjectMapper().readValue(args[1], SpaceInfoDto.class));
            return (status ? "202;null" : "404;null");
        } catch (Exception e) {
            logger.error("putSpaceByID", e);
            return "500;null";
        }
    }

    private String fetchSpaceBookingsByID(String message) {
        logger.info("fetchSpaceBookingsByID message received{" + message + "}");
        try {
            List<Booking> bookings = spaceService.getBookingByIdSpace(Integer.parseInt(message));
            if(bookings != null) {
                return "200;" + new ObjectMapper().writeValueAsString(convertBookingListIntoDto(bookings));
            } else {
                return "404;null";
            }
        } catch (Exception e) {
            logger.error("fetchSpaceBookingsByID", e);
            return "500;null";
        }
    }
}
