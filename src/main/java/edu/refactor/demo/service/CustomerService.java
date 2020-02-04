package edu.refactor.demo.service;

import edu.refactor.demo.model.BillingAccount;
import edu.refactor.demo.model.Customer;

import javax.transaction.Transactional;
import java.util.List;

public interface CustomerService {
    @Transactional
    List<Customer> getAllCustomers();

    @Transactional
    List<BillingAccount> billingAccounts(Long id);

    @Transactional
    Customer createCustomer(Customer customer);

    @Transactional
    Customer getCustomerById(Long id);

    @Transactional
    BillingAccount billingAccountForCustomer(Long id, Long baId);

    @Transactional
    BillingAccount createBAForCustomer(BillingAccount billingAccount, Long id);
}
