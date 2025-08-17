package de.mcc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {

    private Market market;

    @BeforeEach
    void setUp() {
        market = new Market();
    }

    @Test
    void testAddOfferWithAttributes() {
        Offer offer = new Offer("John Doe", 2.0, 10);
        market.addOfferWithAttributes("Apfel", offer, new String[]{"obst", "bio"});
        Product p = market.getProducts().get("apfel");
        assertNotNull(p);
        assertEquals(1, p.getOffers().size());
        assertTrue(p.matchesAttribute("obst"));
    }

    @Test
    void testSearchByAttributeMitTreffer() {
        Offer offer = new Offer("John Doe", 1.5, 5);
        market.addOfferWithAttributes("Birne", offer, new String[]{"bio"});
        market.searchByAttribute("bio");
    }

    @Test
    void testSearchByAttributeOhneTreffer() {
        market.searchByAttribute("fleisch");
    }

    @Test
    void testShowAllProductsMitProdukt() {
        market.addOfferWithAttributes("Apfel", new Offer("John Doe", 1.0, 3), new String[]{"süß"});
        market.showAllProducts();
    }

    @Test
    void testShowAllProductsOhneProdukte() {
        market.showAllProducts();
    }

    @Test
    void testBuyProductErfolgreich() {
        market.addOfferWithAttributes("Banane", new Offer("John Doe", 1.0, 4), new String[]{"frucht"});
        market.buyProduct("Banane", 2);
        Product p = market.getProducts().get("banane");
        assertEquals(2, p.getOffers().get(0).getQuantity());
    }

    @Test
    void testBuyProductNichtGefunden() {
        market.buyProduct("Zitrone", 1);
    }

    @Test
    void testShowProductOffersGefunden() {
        market.addOfferWithAttributes("Mango", new Offer("John Doe", 2.5, 6), new String[]{"exotisch"});
        market.showProductOffers("Mango");
    }

    @Test
    void testShowProductOffersNichtGefunden() {
        market.showProductOffers("Papaya");
    }

    @Test
    void testShowPriceHistoryGefunden() {
        market.addOfferWithAttributes("Kiwi", new Offer("Jane Doe", 1.0, 2), new String[]{"grün"});
        market.buyProduct("Kiwi", 2);
        market.showPriceHistory("Kiwi");
    }

    @Test
    void testShowPriceHistoryNichtGefunden() {
        market.showPriceHistory("Ananas");
    }

    @Test
    void testShowSellerOffersMitTreffer() {
        market.addOfferWithAttributes("Pfirsich", new Offer("Jane Doe", 2.0, 7), new String[]{"frucht"});
        market.showSellerOffers("Jane Doe");
    }

    @Test
    void testShowSellerOffersOhneTreffer() {
        market.showSellerOffers("Unbekannt");
    }

    @Test
    void testUpdateSellerOfferErfolgreich() {
        market.addOfferWithAttributes("Tomate", new Offer("Jo", 1.0, 5), new String[]{"gemüse"});
        market.updateSellerOffer("Jo", "Tomate", 2.0, 10);
        Product p = market.getProducts().get("tomate");
        Offer o = p.getOffers().get(0);
        assertEquals(2.0, o.getPrice());
        assertEquals(10, o.getQuantity());
    }

    @Test
    void testUpdateSellerOfferNichtGefunden() {
        market.updateSellerOffer("Karl", "Brot", 2.0, 10);
    }

    @Test
    void testUpdateProductAttributesErfolgreich() {
        market.addOfferWithAttributes("Reis", new Offer("Markus", 1.5, 3), new String[]{"korn"});
        market.updateProductAttributes("Markus", "Reis", new String[]{"langkorn", "bio"});
        Product p = market.getProducts().get("reis");
        assertTrue(p.matchesAttribute("bio"));
    }

    @Test
    void testUpdateProductAttributesNichtGefunden() {
        market.updateProductAttributes("Nina", "Milch", new String[]{"weiß"});
    }

    @Test
    void testRemoveSellerOfferErfolgreich() {
        market.addOfferWithAttributes("Wasser", new Offer("Sophie", 0.5, 8), new String[]{"getränk"});
        market.removeSellerOffer("Sophie", "Wasser");
        Product p = market.getProducts().get("wasser");
        assertTrue(p.getOffers().isEmpty());
    }

    @Test
    void testRemoveSellerOfferNichtGefunden() {
        market.removeSellerOffer("X", "Cola");
    }
}