package lk.ijse.aad.possystembackend.dao;

import lk.ijse.aad.possystembackend.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public sealed interface CustomerDAO permits CustomerDAOIMPL {
    String saveCustomer(CustomerDTO customer, Connection connection) throws SQLException;

    boolean deleteCustomer(String id, Connection connection) throws SQLException;

    boolean updateCustomer(String id, CustomerDTO customer,Connection connection) throws SQLException;

    CustomerDTO getCustomer(String id, Connection connection) throws SQLException;

    List<CustomerDTO> getAllCustomers(Connection connection) throws SQLException;
}
