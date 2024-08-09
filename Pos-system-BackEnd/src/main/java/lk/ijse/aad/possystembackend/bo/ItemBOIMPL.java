package lk.ijse.aad.possystembackend.bo;

import lk.ijse.aad.possystembackend.dao.CustomerDAOIMPL;
import lk.ijse.aad.possystembackend.dao.ItemDAOIMPL;
import lk.ijse.aad.possystembackend.dto.CustomerDTO;
import lk.ijse.aad.possystembackend.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ItemBOIMPL implements ItemBO{


    @Override
    public String saveItem(ItemDTO item, Connection connection) throws Exception {
        var itemDAOIMPL = new ItemDAOIMPL();
        System.out.println(item.getItemId() + "itemBOIMPL");
        return itemDAOIMPL.saveItem(item, connection);
    }

    @Override
    public boolean deleteItem(String id, Connection connection) throws Exception {
        var itemDAOIMPL = new ItemDAOIMPL();
        return itemDAOIMPL.deleteItem(id, connection);
    }

    @Override
    public boolean updateItem(String id, ItemDTO item, Connection connection) throws Exception {
        var itemDAOIMPL = new ItemDAOIMPL();
        return itemDAOIMPL.updateItem(id, item, connection);
    }

    @Override
    public ItemDTO getItem(String id, Connection connection) throws Exception {
        var itemDAOIMPL = new ItemDAOIMPL();
        return itemDAOIMPL.getItem(id, connection);
    }

    @Override
    public List<ItemDTO> getAllItem(Connection connection) throws SQLException {
        var itemDAOIMPL = new ItemDAOIMPL();
        return itemDAOIMPL.getAllItem(connection);
    }
}
