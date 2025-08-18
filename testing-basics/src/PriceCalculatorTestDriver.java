public class PriceCalculatorTestDriver {

        private static final double EPS = 1e-6;

        private static boolean almostEqual(double a, double b) {
                return Math.abs(a - b) < EPS;
        }

        private static boolean testCase(String name,
                        double baseprice, double specialprice, double extraprice, int extras, double discount,
                        double expected) {
                PriceCalculator pc = new PriceCalculator();
                double actual = pc.calculatePrice(baseprice, specialprice, extraprice, extras, discount);
                boolean ok = almostEqual(actual, expected);
                System.out.printf("%-30s expected=%.2f actual=%.2f -> %s%n",
                                name, expected, actual, ok ? "OK" : "FAIL");
                return ok;
        }

        public static boolean test_calculate_price() {
                boolean allOk = true;

                allOk &= testCase("No extras, no discount",
                                10000, 500, 0, 0, 0, 10500);

                allOk &= testCase("2 extras, 5% dealer",
                                10000, 500, 2000, 2, 5, 12000);

                allOk &= testCase("3 extras -> 10% addon",
                                10000, 500, 3000, 3, 0, 13200);

                allOk &= testCase("5 extras -> 15% addon, 5% dealer",
                                10000, 1000, 4000, 5, 5, 13900);

                return allOk;
        }

        public static void main(String[] args) {
                boolean ok = test_calculate_price();
                System.out.println(ok ? "ALL TESTS PASSED" : "SOME TESTS FAILED");
        }
}
