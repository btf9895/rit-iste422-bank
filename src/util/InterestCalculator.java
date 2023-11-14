public class InterestCalculator {

	public static void main(String[] args) {
		double bal = Double.parseDouble(args[2]);
		int i = Integer.parseInt(args[1]);
		double rate = Double.parseDouble(args[0]);
		double d = bal;
		
		while (i > 0) {
			bal = d;
			d += getInterest(bal, rate);
			i--;
		}
		System.out.println("BALANCE WITH INTEREST: $" + String.format("%.2f",d));
	}

	static double getInterest(double bal, double rate) {
		return Math.round(bal * rate / 12d * 100d) / 100d;
	}

}