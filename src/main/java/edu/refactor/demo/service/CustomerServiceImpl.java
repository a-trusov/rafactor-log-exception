package edu.refactor.demo.service;

import edu.refactor.demo.dao.BillingAccountDAO;
import edu.refactor.demo.dao.CustomerDAO;
import edu.refactor.demo.model.BillingAccount;
import edu.refactor.demo.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerDAO customerDAO;
    private final BillingAccountDAO billingAccountDAO;
    Customer c = new Customer();
    BillingAccount b = new BillingAccount();


    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO, BillingAccountDAO billingAccountDAO) {
        this.customerDAO = customerDAO;
        this.billingAccountDAO = billingAccountDAO;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDAO.findCustomerByStatusNotLike("delete");
    }

    @Override
    @Transactional
    public List<BillingAccount> billingAccounts(Long id) {
        Optional<Customer> customer = customerDAO.findById(id);
        if(!customer.isPresent()) {
            throw new RuntimeException("Customer doesn't exist");
        }
        return customer.get().getBillingAccounts();
    }

    @Override
    public Customer createCustomer(Customer customer) {
        if (customerDAO.findCustomerByLoginAndEmail(customer.getLogin(), customer.getEmail()) != null) {
            throw new RuntimeException("Create customer error");
        }
        Customer newCustomer = new Customer().copyFrom(customer);
        newCustomer = customerDAO.save(newCustomer);
        BillingAccount billingAccount = new BillingAccount();
        billingAccount.setMoney(100);
        billingAccount.setPrimary(true);
        newCustomer.getBillingAccounts().add(billingAccount);
        billingAccount.setCustomer(newCustomer);
        billingAccountDAO.save(billingAccount);
        return newCustomer;
    }

    @Override
    @Transactional
    public Customer getCustomerById(Long id) {
        Optional<Customer> customer = customerDAO.findById(id);
        if(!customer.isPresent()) {
            throw new RuntimeException("Customer doesn't exist");
        }
        return customer.get();
    }

    @Override
    @Transactional
    public BillingAccount billingAccountForCustomer(Long id, Long baId) {
        Optional<Customer> customer = customerDAO.findById(id);
        if(!customer.isPresent()) {
            throw new RuntimeException("Customer doesn't exist");
        }

        Optional<BillingAccount> billingAccount = customer.get().getBillingAccounts()
                                                    .stream().filter(e -> e.getId().equals(baId)).findFirst();
        if(!billingAccount.isPresent()) {
            throw new RuntimeException("BillingAccount doesn't exist");
        }
        return billingAccount.get();
    }

    @Override
    @Transactional
    public BillingAccount createBAForCustomer(BillingAccount billingAccount, Long id) {
        Optional<Customer> customer = customerDAO.findById(id);
        if(!customer.isPresent()) {
            throw new RuntimeException("Customer doesn't exist");
        }
        billingAccount.setCustomer(customer.get());
        return billingAccountDAO.save(billingAccount);
    }
}
