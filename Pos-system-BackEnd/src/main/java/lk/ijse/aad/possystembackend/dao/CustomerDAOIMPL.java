package lk.ijse.aad.possystembackend.dao;

import lk.ijse.aad.possystembackend.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class CustomerDAOIMPL implements CustomerDAO{


    public static String SAVE_CUSTOMER = "INSERT INTO customer (id,name,address,salary) VALUES(?,?,?,?)";
    public static String GET_Customer = "SELECT * FROM customer WHERE id=?";
    public static String UPDATE_Customer = "UPDATE customer SET name=?,address=?,salary=? WHERE id=?";
    public static String DELETE_Customer = "DELETE FROM customer WHERE id=?";

    @Override
    public String saveCustomer(CustomerDTO customer, Connection connection) throws SQLException {
        try {
            var ps = connection.prepareStatement(SAVE_CUSTOMER);
            ps.setString(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getAddress());
            ps.setInt(4, customer.getSalary());
            if(ps.executeUpdate() != 0){
                return "Customer Save Successfully";
            }else {
                return "Failed to Save Customer";
            }
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }











    @Override
    public boolean deleteCustomer(String id, Connection connection) throws SQLException {
        var ps = connection.prepareStatement(DELETE_Customer);
        ps.setString(1, id);
        return ps.executeUpdate() != 0;
    }

    @Override
    public boolean updateCustomer(String id, CustomerDTO customer, Connection connection) throws SQLException {
        try {
            var ps = connection.prepareStatement(UPDATE_Customer);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setInt(3, customer.getSalary());
            ps.setString(4, id);
            return ps.executeUpdate() != 0;
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public CustomerDTO getCustomer(String id, Connection connection) throws SQLException {
        try {
            CustomerDTO customerDTO = new CustomerDTO();
            var ps = connection.prepareStatement(GET_Customer);
            ps.setString(1, id);
            var rst = ps.executeQuery();
            while (rst.next()){
                customerDTO.setId(rst.getString("id"));
                customerDTO.setName(rst.getString("name"));
                customerDTO.setAddress(rst.getString("address"));
                customerDTO.setSalary(Integer.parseInt(rst.getString("salary")));

            }
            return customerDTO;
        }catch (Exception e){
            throw new SQLException(e.getMessage());
        }
    }



    public List<CustomerDTO> getAllCustomers(Connection connection) throws SQLException {
        List<CustomerDTO> customers = new ArrayList<>();
        String GET_ALL_CUSTOMERS = "SELECT * FROM customer";
        try (var ps = connection.prepareStatement(GET_ALL_CUSTOMERS);
             var rst = ps.executeQuery()) {
            while (rst.next()) {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setId(rst.getString("id"));
                customerDTO.setName(rst.getString("name"));
                customerDTO.setAddress(rst.getString("address"));
                customerDTO.setSalary(rst.getInt("salary"));
                customers.add(customerDTO);
            }
        }
        return customers;
    }
}
