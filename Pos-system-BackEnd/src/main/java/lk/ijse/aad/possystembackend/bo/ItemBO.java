package lk.ijse.aad.possystembackend.bo;

import lk.ijse.aad.possystembackend.dto.CustomerDTO;
import lk.ijse.aad.possystembackend.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ItemBO {
    String saveItem(ItemDTO item, Connection connection)throws Exception;
    boolean deleteItem(String id, Connection connection)throws Exception;
    boolean updateItem(String id,ItemDTO item,Connection connection)throws Exception;
    ItemDTO getItem(String id,Connection connection)throws Exception;

    List<ItemDTO> getAllItem(Connection connection) throws SQLException;
}
