package de.mcc;

import lombok.extern.java.Log;
import java.util.Scanner;

@Log
public class Main {

    static {
        for (var handler : java.util.logging.Logger.getLogger("").getHandlers()) {
            handler.setFormatter(new java.util.logging.Formatter() {
                @Override
                public String format(java.util.logging.LogRecord record) {
                    return record.getMessage() + "\n";
                }
            });
        }
    }

    private static final Scanner scanner = new Scanner(System.in);
    private static final Market market = new Market();

    public static void main(String[] args) {
        while (true) {
            log.info("""
                    === Hauptmenü ===
                    1. Als Käufer fortfahren
                    2. Als Verkäufer fortfahren
                    0. Beenden
                    """);

            log.info("Auswahl: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> runBuyerConsole();
                case "2" -> runSellerConsole();
                case "0" -> {
                    log.info("Programm beendet. Auf Wiedersehen!");
                    return;
                }
                default -> log.warning("Ungültige Eingabe.");
            }
        }
    }

    private static void runBuyerConsole() {
        while (true) {
            log.info("""
                === Käufermenü ===
                1. Alle Produkte anzeigen
                2. Produkt kaufen
                3. Angebote zu einem Produkt anzeigen
                4. Kursverlauf anzeigen
                5. Nach Attribut suchen
                0. Zurück
                """);

            log.info("Auswahl: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> market.showAllProducts();
                case "2" -> {
                    log.info("Produktname: ");
                    String name = scanner.nextLine();
                    log.info("Menge: ");
                    int qty;
                    while (true) {
                        try {
                            qty = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            log.warning("Bitte geben Sie eine gültige ganze Zahl ein.");
                        }
                    }
                    market.buyProduct(name, qty);
                }
                case "3" -> {
                    log.info("Produktname: ");
                    String name = scanner.nextLine();
                    market.showProductOffers(name);
                }
                case "4" -> {
                    log.info("Produktname: ");
                    String name = scanner.nextLine();
                    market.showPriceHistory(name);
                }
                case "5" -> {
                    log.info("Attribut eingeben: ");
                    String attr = scanner.nextLine();
                    market.searchByAttribute(attr);
                }
                case "0" -> {
                    log.info("Zurück zum Hauptmenü.");
                    return;
                }
                default -> log.warning("Ungültige Eingabe.");
            }
        }
    }

    private static void runSellerConsole() {
        log.info("Ihr Verkäufername: ");
        String seller = scanner.nextLine();

        while (true) {
            log.info("""
                === Verkäufermenü ===
                1. Neues Angebot hinzufügen
                2. Eigene Angebote anzeigen
                3. Preis/Menge ändern
                4. Attribute ändern
                5. Angebot löschen
                6. Zurück
                """);

            log.info("Auswahl: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    log.info("Produktname: ");
                    String name = scanner.nextLine();
                    log.info("Menge: ");
                    int qty;
                    while (true) {
                        try {
                            qty = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            log.warning("Bitte geben Sie eine gültige ganze Zahl ein.");
                        }
                    }
                    log.info("Preis: ");
                    double price;
                    while (true) {
                        try {
                            price = Double.parseDouble(scanner.nextLine().replace(",", "."));
                            break;
                        } catch (NumberFormatException e) {
                            log.warning("Bitte geben Sie eine gültige Dezimalzahl ein.");
                        }
                    }
                    log.info("Attribute (kommagetrennt): ");
                    String[] attrs = scanner.nextLine().split(",");

                    market.addOfferWithAttributes(name, new Offer(seller, price, qty), attrs);
                    log.info("Angebot hinzugefügt.");
                }
                case "2" -> {
                    log.info("Eigene Angebote:");
                    market.showSellerOffers(seller);
                }
                case "3" -> {
                    log.info("Produktname: ");
                    String name = scanner.nextLine();
                    log.info("Neuer Preis: ");
                    double newPrice;
                    while (true) {
                        try {
                            newPrice = Double.parseDouble(scanner.nextLine().replace(",", "."));
                            break;
                        } catch (NumberFormatException e) {
                            log.warning("Bitte geben Sie eine gültige Dezimalzahl ein.");
                        }
                    }
                    log.info("Neue Menge: ");
                    int newQty;
                    while (true) {
                        try {
                            newQty = Integer.parseInt(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            log.warning("Bitte geben Sie eine gültige ganze Zahl ein.");
                        }
                    }
                    market.updateSellerOffer(seller, name, newPrice, newQty);
                }
                case "4" -> {
                    log.info("Produktname: ");
                    String name = scanner.nextLine();
                    log.info("Neue Attribute (kommagetrennt): ");
                    String[] newAttrs = scanner.nextLine().split(",");
                    market.updateProductAttributes(seller, name, newAttrs);
                }
                case "5" -> {
                    log.info("Produktname: ");
                    String name = scanner.nextLine();
                    market.removeSellerOffer(seller, name);
                }
                case "6" -> {
                    log.info("Zurück zum Hauptmenü.");
                    return;
                }
                default -> log.warning("Ungültige Eingabe.");
            }
        }
    }
}