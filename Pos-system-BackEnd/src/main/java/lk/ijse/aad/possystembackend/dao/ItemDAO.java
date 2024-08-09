package lk.ijse.aad.possystembackend.dao;

import lk.ijse.aad.possystembackend.dto.CustomerDTO;
import lk.ijse.aad.possystembackend.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ItemDAO {
    String saveItem(ItemDTO itemDTO, Connection connection) throws SQLException;

    boolean deleteItem(String id, Connection connection) throws SQLException;

    boolean updateItem(String id, ItemDTO itemDTO,Connection connection) throws SQLException;

    ItemDTO getItem(String id, Connection connection) throws SQLException;

    List<ItemDTO> getAllItem(Connection connection) throws SQLException;

}
