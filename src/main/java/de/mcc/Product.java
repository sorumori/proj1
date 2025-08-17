package de.mcc;

import lombok.Getter;
import lombok.extern.java.Log;

import java.util.*;

@Getter
@Log
public class Product {
    private String name;
    private List<Offer> offers = new ArrayList<>();
    private List<Double> priceHistory = new ArrayList<>();
    private List<String> attributes = new ArrayList<>();

    public Product(String name, String... attrs) {
        this.name = name;
        this.attributes.addAll(Arrays.asList(attrs));
    }

    public boolean matchesAttribute(String keyword) {
        return attributes.stream().anyMatch(attr -> attr.equalsIgnoreCase(keyword));
    }

    public void addOffer(Offer offer) {
        offers.add(offer);
        offers.sort(Comparator.comparingDouble(Offer::getPrice));
    }

    public void processPurchase(int quantity) {
        Iterator<Offer> iterator = offers.iterator();
        int remaining = quantity;

        while (iterator.hasNext() && remaining > 0) {
            Offer offer = iterator.next();
            int available = offer.getQuantity();

            if (available <= remaining) {
                remaining -= available;
                priceHistory.add(offer.getPrice());
                iterator.remove();
            } else {
                offer.setQuantity(available - remaining);
                priceHistory.add(offer.getPrice());
                remaining = 0;
            }
        }

        if (remaining > 0) {
            log.warning("Nicht genug Ware verfügbar.");
        } else {
            log.info("Kauf erfolgreich.");
        }
    }

    public double getCurrentPrice() {
        return offers.isEmpty() ? -1 : offers.get(0).getPrice();
    }

    public void showOffers() {
        if (offers.isEmpty()) {
            log.info("Keine Angebote verfügbar.");
        } else {
            for (Offer o : offers) {
                log.info(o.toString());
            }
        }
    }

    public void showPriceHistory() {
        int size = priceHistory.size();
        if (size == 0) {
            log.info("Keine Transaktionen vorhanden.");
            return;
        }
        int from = Math.max(size - 3, 0);
        List<Double> recent = priceHistory.subList(from, size);
        log.info("Letzte Kurse: " + recent);
    }

    public List<Offer> getOffersBySeller(String seller) {
        return offers.stream()
                .filter(o -> o.getSellerName().equalsIgnoreCase(seller))
                .toList();
    }

    public boolean updateOfferFromSeller(String seller, double newPrice, int newQty) {
        for (Offer o : offers) {
            if (o.getSellerName().equalsIgnoreCase(seller)) {
                o.setPrice(newPrice);
                o.setQuantity(newQty);
                return true;
            }
        }
        return false;
    }

    public boolean removeOfferFromSeller(String seller) {
        return offers.removeIf(o -> o.getSellerName().equalsIgnoreCase(seller));
    }

    public void setAttributes(List<String> newAttrs) {
        this.attributes = new ArrayList<>(newAttrs);
    }

    @Override
    public String toString() {
        return name + " [Attribute: " + String.join(", ", attributes) + "]";
    }
}