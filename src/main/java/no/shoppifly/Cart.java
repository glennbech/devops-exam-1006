package no.shoppifly;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Cart {
    private String id;

    public List<Item> getItems() {
        return items;
    }
    List<Item> items = new ArrayList<>();
}

@Data
class Item {

    private String description;
    private int qty;

    public Float getUnitPrice() {
        return unitPrice;
    }

    private Float unitPrice;


}