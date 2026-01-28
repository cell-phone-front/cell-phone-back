package com.example.cellphoneback.dto.request.operation;


import com.example.cellphoneback.entity.operation.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ProductBulkUpsertRequest {

    List<Item> productList;

    @Getter
    @Setter
    static public class Item {
        private int id;
        private String brand;
        private String variety;

    }
}
