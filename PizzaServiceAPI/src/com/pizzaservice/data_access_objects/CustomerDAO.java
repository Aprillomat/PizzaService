package com.pizzaservice.data_access_objects;

import com.pizzaservice.buissness_objects.Customer;

/**
 * Created by philipp on 24.01.17.
 */
public interface CustomerDAO
{
    /**
     * Since customers will be identified by their phone number, this function must check
     * if there already exists a customer with the same phone number as the input customer.
     * @param customer
     * @return true if a new customer could be inserted (no duplicate phone numbers)
     */
    boolean addCustomer( Customer customer ) throws DataAccessException;

    Customer getCustomerByPhoneNumber( String phoneNumber ) throws DataAccessException;
}
