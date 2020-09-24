package com.group.foctg.holidayMaker;

import com.group.foctg.holidayMaker.model.Accommodation;
import com.group.foctg.holidayMaker.model.Booking;
import com.group.foctg.holidayMaker.model.Customer;
import com.group.foctg.holidayMaker.model.Location;
import com.group.foctg.holidayMaker.model.ReservedDates;
import com.group.foctg.holidayMaker.model.Room;
import com.group.foctg.holidayMaker.repositories.BookingRepository;
import com.group.foctg.holidayMaker.services.AccommodationService;
import com.group.foctg.holidayMaker.services.BookingService;
import com.group.foctg.holidayMaker.services.CustomerService;
import com.group.foctg.holidayMaker.services.LocationService;
import com.group.foctg.holidayMaker.services.ReservedDatesService;
import com.group.foctg.holidayMaker.services.RoomService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christoffer Hansen
 */
@Component
@Slf4j
public class MockDataGenerator implements CommandLineRunner {

    private final Random rand = new Random();

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ReservedDatesService reservedDatesService;

    private List<String> mockNames;

    private List<String> mockPlaces;

    private List<String> mockURL;

    private String mockDescription;

    private final File fNames = new File("." + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "data" + File.separator + "mockdatanames.txt");
    private final File fPlaces = new File("." + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "data" + File.separator + "mockdataplaces.txt");

    @Override
    public void run(String... args) throws Exception {
        log.info("Started mockdata generation, sit back and take it easy because this might take awhile...");

        Long beforeMillis = System.currentTimeMillis();

        mockNames = getDataFromFile(fNames);
        mockPlaces = getDataFromFile(fPlaces);
        mockURL = new ArrayList<>(Arrays.asList("http://www.newdesignfile.com/postpic/2009/07/home-real-estate-stock-photos_385209.jpg",
                "http://www.newdesignfile.com/postpic/2009/07/american-modern-home_385211.jpg", "http://www.newdesignfile.com/postpic/2009/07/high-water-lake-house-dock_385213.jpg",
                "http://www.newdesignfile.com/postpic/2009/07/adobe-abandoned-house_385217.jpg", "https://images.freeimages.com/images/large-previews/dae/houses-1527478.jpg"));
        mockDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "Maecenas efficitur urna vitae aliquet rhoncus. "
                + "Nunc aliquam turpis sed porta pharetra. "
                + "Nam scelerisque rhoncus urna. Phasellus et augue sem. "
                + "Morbi consequat nec dolor ut scelerisque. "
                + "Sed aliquet, est a auctor blandit, sapien lorem cursus est, "
                + "a feugiat urna nisi ut neque. In sit amet elementum tellus. "
                + "Pellentesque congue bibendum sem a accumsan. Suspendisse eu "
                + "lectus placerat, dictum elit at, volutpat sapien. Donec "
                + "sollicitudin in erat sed efficitur. Integer vel blandit urna.";

        createMockLocations();

        createMockCustomers();

        createMockAccommodations();

        createMockRooms();

        //Create mock bookings - Essentially hardcoded, which is why
        //I am hiding the abomination in a method that you can collapse
        createMockBookings();

        log.info("Mockdata generation has finished!");
        mockDataStats(beforeMillis);

        mockNames.clear();
        mockPlaces.clear();
    }

    private ArrayList<String> getDataFromFile(File path) {
        ArrayList<String> mockdata = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while (br.ready()) {
                mockdata.add(br.readLine());
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MockDataGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MockDataGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mockdata;
    }

    private void createMockLocations() {
        for (int i = 0; i < 5; i++) {
            locationService.saveLocation(new Location(mockPlaces.get(rand.nextInt(mockPlaces.size())), new ArrayList<>()));
        }
    }

    private void createMockCustomers() {
        for (int i = 0; i < 5; i++) {
            customerService.saveCustomer(new Customer(mockNames.get(rand.nextInt(mockNames.size())) + rand.nextInt(255) + "@holidaymaker.io",
                    "password", new ArrayList<>(), new ArrayList<>()));
        }

        customerService.saveCustomer(new Customer("test@test.io",
                "password", new ArrayList<>(), new ArrayList<>()));

    }

    private void createMockAccommodations() {
        for (int i = 0; i < 5; i++) {
            accommodationService.saveAccommodation(new Accommodation(
                    mockNames.get(rand.nextInt(mockNames.size())),
                    rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean(),
                    (short) rand.nextInt(1000), (short) rand.nextInt(1000), locationService.findById(Long.valueOf(i) + 1).get(), mockURL.get(i),
                    mockDescription, new ArrayList<>(), customerService.findById(Long.valueOf(i) + 6).get(), (rand.nextFloat() * (5.0f - 0.1f) + 0.1f)));
            accommodationService.saveAccommodation(new Accommodation(
                    mockNames.get(rand.nextInt(mockNames.size())),
                    rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean(),
                    (short) rand.nextInt(1000), (short) rand.nextInt(1000), locationService.findById(Long.valueOf(i) + 1).get(), mockURL.get(i),
                    mockDescription, new ArrayList<>(), customerService.findById(Long.valueOf(i) + 6).get(), (rand.nextFloat() * (5.0f - 0.1f) + 0.1f)));
        }
    }

    private void createMockRooms() {
        List<Accommodation> dbAccommodations = accommodationService.findAll();

        for(int x = 0; x < (dbAccommodations.size() / 2); x++) {
            for (int i = 0; i < 5; i++) {
                roomService.saveRoom(new Room((short) (rand.nextInt(6) + 2),
                        accommodationService.findById(dbAccommodations.get(x).getId()).get(),
                        (rand.nextFloat() * (20000.0f - 1000.0f) + 1000.0f)));
            }
        }
    }

    /**
     * Whatever you do, DO NOT OPEN THIS METHOD! It is dangerous, ever heard of
     * The Ring? Imagine that one but 100 times worse. Make sure to collapse it
     * at all costs!!!
     *
     * @throws ParseException
     */
    private void createMockBookings() throws ParseException {
        List<Room> dbRooms = roomService.findAll();

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(6)).get(),
                dbRooms.subList(0, 1),
                new SimpleDateFormat("dd/MM/yyyy").parse("02/11/2020"),
                new SimpleDateFormat("dd/MM/yyyy").parse("08/11/2020"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(7)).get(),
                dbRooms.subList(1, 2),
                new SimpleDateFormat("dd/MM/yyyy").parse("10/11/2020"),
                new SimpleDateFormat("dd/MM/yyyy").parse("19/11/2020"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(8)).get(),
                dbRooms.subList(3, 4),
                new SimpleDateFormat("dd/MM/yyyy").parse("20/11/2020"),
                new SimpleDateFormat("dd/MM/yyyy").parse("29/11/2020"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(9)).get(),
                dbRooms.subList(4, 5),
                new SimpleDateFormat("dd/MM/yyyy").parse("02/12/2020"),
                new SimpleDateFormat("dd/MM/yyyy").parse("08/12/2020"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(10)).get(),
                dbRooms.subList(5, 6),
                new SimpleDateFormat("dd/MM/yyyy").parse("20/12/2020"),
                new SimpleDateFormat("dd/MM/yyyy").parse("29/12/2020"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        // 5 bookings so far
        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(6)).get(),
                dbRooms.subList(6, 7),
                new SimpleDateFormat("dd/MM/yyyy").parse("02/01/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("09/01/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(7)).get(),
                dbRooms.subList(7, 8),
                new SimpleDateFormat("dd/MM/yyyy").parse("20/01/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("28/01/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(8)).get(),
                dbRooms.subList(9, 10),
                new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("10/02/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(9)).get(),
                dbRooms.subList(10, 11),
                new SimpleDateFormat("dd/MM/yyyy").parse("16/02/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("26/02/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(10)).get(),
                dbRooms.subList(11, 12),
                new SimpleDateFormat("dd/MM/yyyy").parse("02/03/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("10/03/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        // 10 bookings so far
        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(6)).get(),
                dbRooms.subList(12, 13),
                new SimpleDateFormat("dd/MM/yyyy").parse("12/03/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("23/03/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(7)).get(),
                dbRooms.subList(13, 14),
                new SimpleDateFormat("dd/MM/yyyy").parse("25/03/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("04/04/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(8)).get(),
                dbRooms.subList(14, 15),
                new SimpleDateFormat("dd/MM/yyyy").parse("25/03/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("04/04/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(9)).get(),
                dbRooms.subList(15, 16),
                new SimpleDateFormat("dd/MM/yyyy").parse("12/03/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("21/03/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(10)).get(),
                dbRooms.subList(16, 17),
                new SimpleDateFormat("dd/MM/yyyy").parse("15/04/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("26/04/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        // 15 bookings so far
        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(6)).get(),
                dbRooms.subList(17, 18),
                new SimpleDateFormat("dd/MM/yyyy").parse("10/02/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("18/02/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(7)).get(),
                dbRooms.subList(18, 19),
                new SimpleDateFormat("dd/MM/yyyy").parse("12/02/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("22/02/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(8)).get(),
                dbRooms.subList(19, 20),
                new SimpleDateFormat("dd/MM/yyyy").parse("02/04/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("20/04/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(9)).get(),
                dbRooms.subList(21, 22),
                new SimpleDateFormat("dd/MM/yyyy").parse("20/04/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("28/04/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(10)).get(),
                dbRooms.subList(22, 23),
                new SimpleDateFormat("dd/MM/yyyy").parse("08/03/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("16/03/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        // 20 bookings so far
        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(6)).get(),
                dbRooms.subList(23, 24),
                new SimpleDateFormat("dd/MM/yyyy").parse("15/03/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("26/03/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(7)).get(),
                dbRooms.subList(0, 1),
                new SimpleDateFormat("dd/MM/yyyy").parse("15/03/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("24/03/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(8)).get(),
                dbRooms.subList(1, 2),
                new SimpleDateFormat("dd/MM/yyyy").parse("18/03/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("27/03/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(9)).get(),
                dbRooms.subList(2, 3),
                new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("09/04/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(10)).get(),
                dbRooms.subList(3, 4),
                new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2020"),
                new SimpleDateFormat("dd/MM/yyyy").parse("20/11/2020"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        bookingService.saveBooking(new Booking(
                customerService.findById(Long.valueOf(6)).get(),
                dbRooms.subList(24, 25),
                new SimpleDateFormat("dd/MM/yyyy").parse("15/03/2021"),
                new SimpleDateFormat("dd/MM/yyyy").parse("26/03/2021"),
                (short) 2,
                (short) 0, rand.nextBoolean(), rand.nextBoolean(),
                rand.nextBoolean(), false));

        // 25 bookings so far
    }

    private void mockDataStats(Long ms) {
        log.info("--- Mockdata stats ---");
        List<Customer> customers = customerService.findAll();
        List<Accommodation> accommodations = accommodationService.findAll();
        List<Room> rooms = roomService.findAll();
        List<Booking> bookings = bookingRepository.findAll();
        List<ReservedDates> reservedDates = reservedDatesService.findAll();

        log.info(String.format("$ Customers == %dx", customers.size()));
        log.info(String.format("$ Accommodations == %dx", accommodations.size()));
        log.info(String.format("$ Rooms == %dx", rooms.size()));
        log.info(String.format("$ Bookings == %dx", bookings.size()));
        log.info(String.format("$ ReservedDates == %dx", reservedDates.size()));
        log.info("$ Elapsed in " + (System.currentTimeMillis() - ms) / 1000 + "s");
        log.info("--- END ---");
    }

}
