
package ma.emsi.soubaibi.tp2.tests;

public class TestMeteo {

    public static void main(String[] args) {

        MeteoTool meteo = new MeteoTool();

        double latitude = 48.85;
        double longitude = 2.35;

        int jourDepart = 0;
        int nombreJours = 3;

        System.out.println("=== TEST MÉTÉO PARIS ===");

        boolean pluie = meteo.isPluiePrevue(
                latitude,
                longitude,
                jourDepart,
                nombreJours
        );

        System.out.println();

        if (pluie) {
            System.out.println(
                    "Conseil : emporter un parapluie."
            );
        } else {
            System.out.println(
                    "Aucun jour ne dépasse le seuil de pluie."
            );
        }
    }
}
