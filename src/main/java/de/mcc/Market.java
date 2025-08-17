package de.mcc;

import lombok.extern.java.Log;
import lombok.Getter;
import java.util.*;

@Log
@Getter
public class Market {
    private Map<String, Product> products = new HashMap<>();

    public void addOfferWithAttributes(String productName, Offer offer, String[] attrs) {
        String key = productName.toLowerCase();
        if (!products.containsKey(key)) {
            products.put(key, new Product(productName, attrs));
        }
        products.get(key).addOffer(offer);
    }

    public void searchByAttribute(String keyword) {
        boolean found = false;
        for (Product p : products.values()) {
            if (p.matchesAttribute(keyword)) {
                log.info(p.getName() + " → Attribute: " + String.join(", ", p.getAttributes()));
                p.showOffers();
                found = true;
            }
        }
        if (!found) {
            log.info("Keine Produkte mit Attribut: " + keyword);
        }
    }

    public void showAllProducts() {
        if (products.isEmpty()) {
            log.info("Keine Produkte vorhanden.");
        } else {
            for (Product p : products.values()) {
                String preis = (p.getCurrentPrice() > 0)
                        ? p.getCurrentPrice() + "€"
                        : "n/a";
                log.info(p.getName() + " - Aktueller Kurs: " + preis);
            }
        }
    }

    public void buyProduct(String name, int qty) {
        Product p = products.get(name.toLowerCase());
        if (p == null) {
            log.warning("Produkt '" + name + "' nicht gefunden.");
            return;
        }
        p.processPurchase(qty);
    }

    public void showProductOffers(String name) {
        Product p = products.get(name.toLowerCase());
        if (p == null) {
            log.warning("Produkt '" + name + "' nicht gefunden.");
            return;
        }
        p.showOffers();
    }

    public void showPriceHistory(String name) {
        Product p = products.get(name.toLowerCase());
        if (p == null) {
            log.warning("Produkt '" + name + "' nicht gefunden.");
            return;
        }
        p.showPriceHistory();
    }

    public void showSellerOffers(String seller) {
        boolean found = false;
        for (Product p : products.values()) {
            List<Offer> offers = p.getOffersBySeller(seller);
            if (!offers.isEmpty()) {
                log.info("Produkt: " + p.getName());
                for (Offer o : offers) {
                    log.info("  " + o);
                }
                found = true;
            }
        }
        if (!found) {
            log.info("Keine Angebote von '" + seller + "' gefunden.");
        }
    }

    public void updateSellerOffer(String seller, String productName, double newPrice, int newQty) {
        Product p = products.get(productName.toLowerCase());
        if (p == null) {
            log.warning("Produkt nicht gefunden.");
            return;
        }
        boolean success = p.updateOfferFromSeller(seller, newPrice, newQty);
        if (success) {
            log.info("Angebot aktualisiert.");
        } else {
            log.warning("Kein passendes Angebot gefunden.");
        }
    }

    public void updateProductAttributes(String seller, String productName, String[] newAttrs) {
        Product p = products.get(productName.toLowerCase());
        if (p == null) {
            log.warning("Produkt nicht gefunden.");
            return;
        }
        p.setAttributes(Arrays.asList(newAttrs));
        log.info("Attribute aktualisiert: " + String.join(", ", newAttrs));
    }

    public void removeSellerOffer(String seller, String productName) {
        Product p = products.get(productName.toLowerCase());
        if (p == null) {
            log.warning("Produkt nicht gefunden.");
            return;
        }
        boolean removed = p.removeOfferFromSeller(seller);
        if (removed) {
            log.info("Angebot von '" + seller + "' entfernt.");
        } else {
            log.warning("Kein passendes Angebot gefunden.");
        }
    }
}