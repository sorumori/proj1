package de.mcc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Offer {
    private String sellerName;
    private double price;
    private int quantity;

    @Override
    public String toString() {
        return quantity + " Stück von " + sellerName + " Price: " + price + "€";
    }
}