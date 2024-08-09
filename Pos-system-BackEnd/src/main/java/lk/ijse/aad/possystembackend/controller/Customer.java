package lk.ijse.aad.possystembackend.controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.aad.possystembackend.bo.CustomerBOIMPL;
import lk.ijse.aad.possystembackend.dto.CustomerDTO;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/customer",loadOnStartup = 3)
public class Customer extends HttpServlet {

    static org.slf4j.Logger logger = LoggerFactory.getLogger(Customer.class.getName());
    Connection connection;


    @Override
    public void init() throws ServletException {
        logger.info("Init method invoked");
        try {
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/PosSystem");
            this.connection = pool.getConnection();
            logger.info("Connection initialized");

        }catch (SQLException | NamingException e){
            e.printStackTrace();
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo: Save student

        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);

            logger.info("Received Customer: " + customer);

            var customerBOIMPL = new CustomerBOIMPL();
            String result = customerBOIMPL.saveCustomer(customer, connection);

            writer.write(result);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }




    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo: Delete student

        // Extract the customer ID from the query parameter
        String customerId = req.getParameter("id");

        if (customerId == null || customerId.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
            return;
        }

        try (var writer = resp.getWriter()) {
            var customerBOIMPL = new CustomerBOIMPL();
            if (customerBOIMPL.deleteCustomer(customerId, connection)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
            } else {
                writer.write("Delete failed");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }



    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);

            logger.info("Received Customer for update: " + customer);

            var customerBOIMPL = new CustomerBOIMPL();
            boolean result = customerBOIMPL.updateCustomer(customer.getId(), customer, connection);

            if (result) {
                writer.write("Customer Updated Successfully");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                writer.write("Failed to Update Customer");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<CustomerDTO> customers = new ArrayList<>();
        try {
            var customerBOIMPL = new CustomerBOIMPL();
            if (req.getParameter("id") == null) {
                customers = customerBOIMPL.getAllCustomers(connection);
            } else {
                CustomerDTO customer = customerBOIMPL.getCustomer(req.getParameter("id"), connection);
                if (customer != null) {
                    customers.add(customer);
                }
            }
            Jsonb jsonb = JsonbBuilder.create();
            resp.setContentType("application/json");
            resp.getWriter().write(jsonb.toJson(customers));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
