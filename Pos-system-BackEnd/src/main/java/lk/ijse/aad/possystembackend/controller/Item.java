package lk.ijse.aad.possystembackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.aad.possystembackend.bo.CustomerBOIMPL;
import lk.ijse.aad.possystembackend.bo.ItemBOIMPL;
import lk.ijse.aad.possystembackend.dto.CustomerDTO;
import lk.ijse.aad.possystembackend.dto.ItemDTO;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/item",loadOnStartup = 3)
public class Item extends HttpServlet {

    static org.slf4j.Logger logger = LoggerFactory.getLogger(Item.class.getName());
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
        //Todo: Save item

        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO item = jsonb.fromJson(req.getReader(), ItemDTO.class);


            logger.info("Received Item: " + item);

            var itemBOIMPL = new ItemBOIMPL();
            String result = itemBOIMPL.saveItem(item, connection);

            writer.write(result);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }




    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo: Delete item

        // Extract the customer ID from the query parameter
        String itemId = req.getParameter("id");

        if (itemId == null || itemId.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item ID is required");
            return;
        }

        try (var writer = resp.getWriter()) {
            var itemBOIMPL = new ItemBOIMPL();
            if (itemBOIMPL.deleteItem(itemId, connection)) {
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
            ItemDTO item = jsonb.fromJson(req.getReader(), ItemDTO.class);

            logger.info("Received Item for update: " + item);

            var itemBOIMPL = new ItemBOIMPL();
            boolean result = itemBOIMPL.updateItem(item.getItemId(), item, connection);

            if (result) {
                writer.write("Item Updated Successfully");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                writer.write("Failed to Update Item");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<ItemDTO> items = new ArrayList<>();
        try {
            var itemBOIMPL = new ItemBOIMPL();
            if (req.getParameter("id") == null) {
                items = itemBOIMPL.getAllItem(connection);
            } else {
                ItemDTO item = itemBOIMPL.getItem(req.getParameter("id"), connection);
                if (item != null) {
                    items.add(item);
                }
            }
            Jsonb jsonb = JsonbBuilder.create();
            resp.setContentType("application/json");
            resp.getWriter().write(jsonb.toJson(items));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}