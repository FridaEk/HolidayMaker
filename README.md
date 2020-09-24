# Holiday Maker
This Project is a school-group-project written by
* Stoffmeister Hansen
* Olle Johansson
* Frida Ek
* Tanvir Siddique


## Contents
* [General info](#general-info)
* [Tech](#tech)
* [Setup](#setup)
* [Features](#features)
* [Status](#status)


## General info
Holidaymakers should be a website where customers can book
holiday accommodation in a modern and simple way.
You can do complex searches for vacant accommodation based on several choices.
You can log in and book an accommodation, and see / change Already booked
accommodation.

## Tech
* Springboot
* SQLite 3.32.x
* Java 14

## Setup
Setup and run this application on

 - Windows:
    * cd [path-to-where-you-want-the-application-to-be-stored-without-the-brackets]
    * git clone https://github.com/ollejj/HolidayMaker
    * cd HolidayMaker
    * mvn spring-boot:run

    Access the server from http://localhost:8080/


## Example

```
//AccommodationController.java
@GetMapping("/accommodation/customer")
public List<Accommodation> findAccommodationsByCustomerId(@RequestParam Long id) {
    return accommodationService.findAccommodationsByCustomerId(id);
}
```

```
//BookingController.java
@PutMapping("/booking")
public Booking updateBooking(@RequestBody Booking booking, @RequestParam Long id) {
    return bookingService.updateBooking(booking, id);
}
```
  
  
  
  

## Features

* Filter all Accommodation objects
* Sort all Accommodation objects by rating
* Sort all Room objects by price
* Booking a Room
* Deleting a Booking
* Edit a Booking
* Deleting an Accommodation
* Sign-in/Sign-up/Sign-out

## Status
Packed and Ready for Holidays!
