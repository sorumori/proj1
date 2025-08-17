package de.mcc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;
    private Offer offer1;
    private Offer offer2;

    @BeforeEach
    void setUp() {
        product = new Product("Apfel", "obst", "bio");
        offer1 = new Offer("Seller1", 1.0, 5);
        offer2 = new Offer("Seller2", 1.5, 3);
    }

    @Test
    void testMatchesAttribute() {
        assertTrue(product.matchesAttribute("Bio"));
        assertFalse(product.matchesAttribute("fleisch"));
    }

    @Test
    void testAddOffer() {
        product.addOffer(offer2); // 1.5
        product.addOffer(new Offer("Seller3", 3.1, 20));
        assertEquals(1.5, product.getCurrentPrice());
    }

    @Test
    void testProcessPurchaseCases() {
        product.addOffer(offer1);
        product.addOffer(offer2);

        // Standardfall
        product.processPurchase(4);
        assertEquals(1, product.getOffers().get(0).getQuantity());
        assertEquals(1.0, product.getCurrentPrice());

        // Grenzfall
        product.processPurchase(4);
        assertEquals(-1, product.getCurrentPrice());

        // NichtGenugFall
        product.addOffer(new Offer("Seller1", 2.0, 2));
        product.processPurchase(5);
        assertEquals(-1, product.getCurrentPrice());
    }

    @Test
    void testGetCurrentPriceEmpty() {
        assertEquals(-1, product.getCurrentPrice());
    }

    @Test
    void testGetOffersBySeller() {
        product.addOffer(offer1);
        product.addOffer(offer2);
        List<Offer> seller1Offers = product.getOffersBySeller("Seller1");
        assertEquals(1, seller1Offers.size());
        assertEquals(1.0, seller1Offers.get(0).getPrice());
    }

    @Test
    void testUpdateOfferFromSeller() {
        product.addOffer(offer1);
        boolean updated = product.updateOfferFromSeller("Seller1", 3.0, 10);
        assertTrue(updated);
        Offer updatedOffer = product.getOffers().get(0);
        assertEquals(3.0, updatedOffer.getPrice());
        assertEquals(10, updatedOffer.getQuantity());
    }

    @Test
    void testUpdateOfferFromSellerNotFound() {
        boolean updated = product.updateOfferFromSeller("Unbekannt", 2.0, 5);
        assertFalse(updated);
    }

    @Test
    void testRemoveOfferFromSeller() {
        product.addOffer(offer1);
        boolean removed = product.removeOfferFromSeller("Seller1");
        assertTrue(removed);
        assertTrue(product.getOffers().isEmpty());
    }

    @Test
    void testRemoveOfferFromSellerNotFound() {
        product.addOffer(offer1);
        boolean removed = product.removeOfferFromSeller("Nobody");
        assertFalse(removed);
    }

    @Test
    void testSetAttributes() {
        product.setAttributes(List.of("regional", "saisonal"));
        assertTrue(product.matchesAttribute("regional"));
        assertFalse(product.matchesAttribute("bio"));
    }

    @Test
    void testToStringContainsAttributes() {
        String result = product.toString();
        assertTrue(result.contains("obst"));
        assertTrue(result.contains("bio"));
        assertTrue(result.contains("Apfel"));
    }
}