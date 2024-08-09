package lk.ijse.aad.possystembackend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private String ItemId;
    private String ItemName;
    private int ItemQty;
    private int ItemPrice;
}
