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
package com.group.foctg.holidayMaker.controllers;

import com.group.foctg.holidayMaker.exceptions.CustomerNotFoundException;
import com.group.foctg.holidayMaker.model.Accommodation;
import com.group.foctg.holidayMaker.model.Booking;
import com.group.foctg.holidayMaker.model.Customer;
import com.group.foctg.holidayMaker.services.CustomerService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController for the {@link com.group.foctg.holidayMaker.model.Customer}
 * entity and column. This class holds all the endpoints for
 * {@link com.group.foctg.holidayMaker.model.Customer}.
 *
 * Autowiring {@link com.group.foctg.holidayMaker.services.CustomerService}.
 *
 * The default URL value is set to, "/api".
 *
 * @author Christoffer Hansen &lt;chris.hansen.ch@outlook.com&gt;
 * @see com.group.foctg.holidayMaker.services.CustomerService
 */
@RestController
@RequestMapping(value = "/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * GET endpoint method that listens on <code>"/customers"</code> URL and
     * will call the
     * {@link com.group.foctg.holidayMaker.services.CustomerService#getAll}
     * method from the Service.
     *
     *
     * @return a List&lt;{@link com.group.foctg.holidayMaker.model.Customer}&gt; from the autowired Service
     */
    @GetMapping("/customers")
    public List<Customer> allCustomers() {
        return customerService.findAll();
    }

    /**
     * POST endpoint method that listens on <code>"/customer"</code> URL and
     * will call the
     * {@link com.group.foctg.holidayMaker.services.CustomerService#saveCustomer}
     * method from the Service.
     *
     * @param customer {@link com.group.foctg.holidayMaker.model.Customer}
     * object to pass to the Service class
     * @return a boolean value from the autowired Service
     */
    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    public boolean saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    /**
     * DELETE endpoint method that listens on <code>"/customer"</code> URL and
     * will call the
     * {@link com.group.foctg.holidayMaker.services.CustomerService#removeCustomer}
     * method from the autowired Service.
     *
     * @param id Long value to pass to the Service class
     * @return a boolean value from the Service
     */
    @RequestMapping(value = "/customer", method = RequestMethod.DELETE)
    public boolean removeCustomer(@RequestParam Long id) {
        return customerService.removeCustomer(id);
    }

    /**
     * GET endpoint method that listens on <code>"/customer"</code> URL and will
     * call the
     * {@link com.group.foctg.holidayMaker.services.CustomerService#findById}
     * method from the autowired Service.
     *
     * @param id Long value to pass to the Service class
     * @return a Optional list of type
     * {@link com.group.foctg.holidayMaker.model.Customer} object from the
     * Service
     */
    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public Optional<Customer> findCustomerById(@RequestParam Long id) {
        Optional<Customer> cust = customerService.findById(id);

        if (cust.isEmpty()) {
            throw new CustomerNotFoundException(id);
        }

        return cust;
    }

    /**
     * GET endpoint method that listens on <code>"/customer/by"</code> URL and
     * will call the
     * {@link com.group.foctg.holidayMaker.services.CustomerService#findCustomerByEmail}
     * method from the autowired Service.
     *
     * @param email String value to pass to the Service class
     * @return a Optional list of type
     * {@link com.group.foctg.holidayMaker.model.Customer} value from the
     * Service
     */
    @RequestMapping(value = "/customer/by", method = RequestMethod.GET)
    public Optional<Customer> findCustomerByEmail(@RequestParam String email) {
        Optional<Customer> cust = customerService.findCustomerByEmail(email);
        
        if (cust.isEmpty()) {
            throw new CustomerNotFoundException(email);
        }
                
        return cust;
    }

    /**
     * GET endpoint method that listens on <code>"/customer/bookings"</code> URL
     * and will call the
     * {@link com.group.foctg.holidayMaker.services.CustomerService#findCustomersBookingsByCustomerID}
     * method from the autowired Service.
     *
     * @param id Long value to pass to the Service class
     * @return a List object from the Service
     */
    @RequestMapping(value = "/customer/bookings", method = RequestMethod.GET)
    public List<Booking> findBookingsByCustomerId(@RequestParam Long id) {
        return customerService.findCustomersBookingsByCustomerID(id);
    }

    /**
     * GET endpoint method that listens on
     * <code>"/customer/accommodations"</code> URL and will call the
     * {@link com.group.foctg.holidayMaker.services.CustomerService#findCustomersAccommodationsByCustomerID}
     * method from the autowired Service.
     *
     * @param id Long value to pass to the Service class
     * @return a List object from the Service
     */
    @RequestMapping(value = "/customer/accommodations", method = RequestMethod.GET)
    public List<Accommodation> findAccommodationsByCustomerId(@RequestParam Long id) {
        return customerService.findCustomersAccommodationsByCustomerID(id);
    }

}
