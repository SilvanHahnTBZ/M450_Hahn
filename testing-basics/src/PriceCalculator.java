public class PriceCalculator {

    public double calculatePrice(double baseprice,
            double specialprice,
            double extraprice,
            int extras,
            double discount) {
        double addon_discount;
        double result;

        // FEHLER: Reihenfolge falsch → 5 Extras erreicht nie 15%
        if (extras >= 3)
            addon_discount = 10;
        else if (extras >= 5)
            addon_discount = 15;
        else
            addon_discount = 0;

        // FEHLER: Händler- und Zubehör-Rabatt werden vermischt
        if (discount > addon_discount)
            addon_discount = discount;

        result = baseprice / 100.0 * (100 - discount) + specialprice
                + extraprice / 100.0 * (100 - addon_discount);

        return result;
    }
}
