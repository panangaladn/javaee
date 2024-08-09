package lk.ijse.aad.possystembackend.bo;

import lk.ijse.aad.possystembackend.dao.CustomerDAOIMPL;
import lk.ijse.aad.possystembackend.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomerBOIMPL implements CustomerBO{
    @Override
    public String saveCustomer(CustomerDTO customer, Connection connection) throws Exception {
        var customerDAOIMPL = new CustomerDAOIMPL();
        System.out.println(customer.getId() + "customerBOIMPL");
        return customerDAOIMPL.saveCustomer(customer, connection);
    }

    @Override
    public boolean deleteCustomer(String id, Connection connection) throws Exception {
        var customerDAOIMPL = new CustomerDAOIMPL();
        return customerDAOIMPL.deleteCustomer(id, connection);
    }



    @Override
    public boolean updateCustomer(String id, CustomerDTO customer, Connection connection) throws Exception {
        var customerDAOIMPL = new CustomerDAOIMPL();
        return customerDAOIMPL.updateCustomer(id, customer, connection);
    }

    @Override
    public CustomerDTO getCustomer(String id, Connection connection) throws Exception {
        var customerDAOIMPL = new CustomerDAOIMPL();
        return customerDAOIMPL.getCustomer(id, connection);
    }

    @Override
    public List<CustomerDTO> getAllCustomers(Connection connection) throws SQLException {
        var customerDAOIMPL = new CustomerDAOIMPL();
        return customerDAOIMPL.getAllCustomers(connection);
    }
}
