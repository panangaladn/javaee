package lk.ijse.aad.possystembackend.dao;

import lk.ijse.aad.possystembackend.dto.CustomerDTO;
import lk.ijse.aad.possystembackend.dto.ItemDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOIMPL implements ItemDAO{

    public static String SAVE_ITEM = "INSERT INTO item (itemId,itemName,itemQty,itemPrice) VALUES(?,?,?,?)";
    public static String GET_ITEM = "SELECT * FROM item WHERE itemId=?";
    public static String UPDATE_ITEM = "UPDATE item SET itemName=?,itemQty=?,itemPrice=? WHERE itemId=?";
    public static String DELETE_ITEM = "DELETE FROM item WHERE itemId=?";


    @Override
    public String saveItem(ItemDTO item, Connection connection) throws SQLException {

        try {
            var ps = connection.prepareStatement(SAVE_ITEM);
            ps.setString(1, item.getItemId());
            ps.setString(2, item.getItemName());
            ps.setInt(3, item.getItemQty());
            ps.setInt(4, item.getItemPrice());
            if(ps.executeUpdate() != 0){
                return "Item Saved Successfully";
            }else {
                return "Failed to Save Item";
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }


    @Override
    public boolean deleteItem(String id, Connection connection) throws SQLException {
        var ps = connection.prepareStatement(DELETE_ITEM);
        ps.setString(1, id);
        return ps.executeUpdate() != 0;
    }

    @Override
    public boolean updateItem(String id, ItemDTO itemDTO, Connection connection) throws SQLException {

        try {
            var ps = connection.prepareStatement(UPDATE_ITEM);
            ps.setString(1, itemDTO.getItemName());
            ps.setInt(2, itemDTO.getItemQty());
            ps.setInt(3, itemDTO.getItemPrice());
            ps.setString(4, id);
            return ps.executeUpdate() != 0;
        }catch (SQLException e){
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public ItemDTO getItem(String id, Connection connection) throws SQLException {
        try {
            ItemDTO itemDTO = new ItemDTO();
            var ps = connection.prepareStatement(GET_ITEM);
            ps.setString(1, id);
            var rst = ps.executeQuery();
            while (rst.next()){
                itemDTO.setItemId(rst.getString("ItemId"));
                itemDTO.setItemName(rst.getString("ItemName"));
                itemDTO.setItemQty(Integer.parseInt(rst.getString("ItemQty")));
                itemDTO.setItemPrice(Integer.parseInt(rst.getString("ItemPrice")));

            }
            return itemDTO;
        }catch (Exception e){
            throw new SQLException(e.getMessage());
        }
    }



    @Override
    public List<ItemDTO> getAllItem(Connection connection) throws SQLException {
        List<ItemDTO> item = new ArrayList<>();
        String GET_ALL_Item = "SELECT * FROM item";
        try (var ps = connection.prepareStatement(GET_ALL_Item);
             var rst = ps.executeQuery()) {
            while (rst.next()) {
                ItemDTO itemDTO = new ItemDTO();
                itemDTO.setItemId(rst.getString("ItemId"));
                itemDTO.setItemName(rst.getString("ItemName"));
                itemDTO.setItemQty(Integer.parseInt(rst.getString("ItemQty")));
                itemDTO.setItemPrice(Integer.parseInt(rst.getString("ItemPrice")));
                item.add(itemDTO);
            }
        }
        return item;
    }
}
