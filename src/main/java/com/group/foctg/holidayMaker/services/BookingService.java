/*
 * Copyright 2020-2030 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.group.foctg.holidayMaker.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group.foctg.holidayMaker.model.Booking;
import com.group.foctg.holidayMaker.model.DateChecker;
import com.group.foctg.holidayMaker.model.ReservedDates;
import com.group.foctg.holidayMaker.model.Room;
import com.group.foctg.holidayMaker.repositories.BookingRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Service class for the {@link com.group.foctg.holidayMaker.model.Booking}
 * column and entity. Autowires the repository.
 *
 * @author Frida Ek
 *
 * @see com.group.foctg.holidayMaker.repositories.BookingRepository
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReservedDatesService reservedDatesService;

    /**
     * Saves the {@link com.group.foctg.holidayMaker.model.Booking} object from
     * parameter in the database.
     *
     * @param booking {@link com.group.foctg.holidayMaker.model.Booking} object
     * that shall be saved.
     * @return A boolean value representing whether the saving was successful or
     * not.
     */
    public boolean saveBooking(Booking booking) {

        for (Room r : booking.getRooms()) {
            if (!roomService.findById(r.getId()).isEmpty()) {
                ReservedDates rd
                        = reservedDatesService.findReservedDatesByRoomId(r.getId());
                try {
                    if (!DateChecker.isOverlapping(rd.getDateFrom(),
                            rd.getDateTo(),
                            new SimpleDateFormat("dd/MM/yyyy").parse(booking.getDateFrom()),
                            new SimpleDateFormat("dd/MM/yyyy").parse(booking.getDateTo()))) {

                        ReservedDates newRd = new ReservedDates(
                                new SimpleDateFormat("dd/MM/yyyy").parse(booking.getDateFrom()),
                                new SimpleDateFormat("dd/MM/yyyy").parse(booking.getDateTo()),
                                r, booking);
                        reservedDatesService.saveReservedDates(newRd);
                        
                        booking.setReservedDates(newRd);
                        bookingRepository.saveAndFlush(booking);

                        //r.setReservedDates((r.getReservedDates().add(newRd)));
                        roomService.updateRoom(r, r.getId());

                        return true;
                    } else {
                        // throw isOverlapping exception
                        return false;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        
        return false;
    }

    /**
     * Removes the {@link com.group.foctg.holidayMaker.model.Booking} object
     * with the same <code>id</code> as the parameter from the database.
     *
     * @param id Long value used for finding and removing
     * {@link com.group.foctg.holidayMaker.model.Booking} with that
     * <code>id</code>
     * @return A boolean value representing whether the removing was successful
     * or not.
     */
    public boolean removeBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * If there is a Booking object already that has the same id as the
     * {@link com.group.foctg.holidayMaker.model.Booking} passed as parameter
     * then it'll update the existing object.Otherwise it will save the object.
     *
     * @param booking {@link com.group.foctg.holidayMaker.model.Booking} object
     * passed for updating or saving.
     * @param id
     * @return A Booking object representing the new Booking
     */
    public Booking updateBooking(Booking booking, Long id) {
        return bookingRepository.findById(id)
                .map(bkn -> {
                    bkn.setNumberOfAdults(booking.getNumberOfAdults());
                    bkn.setNumberOfKids(booking.getNumberOfKids());
                    bkn.setAllInclusive(booking.getAllInclusive());
                    bkn.setFullBoard(booking.getFullBoard());
                    bkn.setHalfBoard(booking.getHalfBoard());
                    bkn.setExtraBeds(booking.getExtraBeds());
                    return bookingRepository.save(bkn);
                })
                .orElseGet(() -> {
                    booking.setId(id);
                    return bookingRepository.save(booking);
                });
    }

    /**
     * Goes through the database, checks and returns all
     * {@link com.group.foctg.holidayMaker.model.Booking} objects in the
     * List&lt;{@link com.group.foctg.holidayMaker.model.Booking}&gt; if a
     * customer with given <code>id</code> exists.
     *
     * @param id Long value to use for finding the
     * {@link com.group.foctg.holidayMaker.model.Customer}
     * @return List&lt;{@link com.group.foctg.holidayMaker.model.Booking}&gt;
     * from {@link com.group.foctg.holidayMaker.model.Customer} with the given
     * <code>id</code>, if it exists
     */
    public List<Booking> findBookingsByCustomerId(Long id) {
        return bookingRepository.findBookingsByCustomerID(id);
    }
}
