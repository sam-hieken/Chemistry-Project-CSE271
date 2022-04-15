package chem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A class representing an ion in chemistry.
 * @author Sam Hieken
 *
 */
public class Ion {
	private String	symbol,
					name;
	
	private int		charge;
	
	/**
	 * For use in class only.
	 */
	private Ion(String symbol, String name, int charge) {
		this.symbol = symbol;
		this.name = name;
		
		this.charge = charge;
	}

	
//	----------------------------------------------------------------------	
	
	/**
	 * Get data about an ion.<br>
	 * O(n) time complexity (n = number of ions in table)
	 * @param symbol The ion's symbol, ex: {@code D-} (deuterium), {@code CO3--} (carbonate)
	 * {@code 
	 * @throws IOException if the data table (ions.csv) could not be located 
	 */
	public static Ion getIon(String symbol) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("rsc/ions.csv"));
		
		String line = "";
		
		while ((line = br.readLine()) != null) {
			
			String[] split = line.split("\\,");
			
			// Found the ion in table
			if (split[0].equals(symbol)) {
				
				br.close();
				return new Ion(split[0], split[1], Integer.parseInt(split[2]));
			}
		}
		
		br.close();
		return null;
	}

	/**
	 * @return Symbol of ion.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return The name of this ion.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Increase the charge of the ion (does not effect {@code name})
	 */
	public void chargePlus() {
		this.charge++;
		
		// If already a negative ion then it cancels
		if (this.symbol.contains("-")) {
			this.symbol = this.symbol.replaceFirst("\\-", "");
		}
		
		else this.symbol += "+";
	}
	
	/**
	 * Decrease the charge of the ion (does not effect {@code name})
	 */
	public void chargeMinus() {
		this.charge--;
		
		// If already a positive ion then it cancels
		if (this.symbol.contains("+")) {
			this.symbol = this.symbol.replaceFirst("\\+", "");
		}
		
		else this.symbol += "-";
	}

	/**
	 * @return The charge of the ion.
	 */
	public int getCharge() {
		return charge;
	}

	@Override
	public String toString() {
		return name + " (" + symbol + ")";
	}
	
	/**
	 * Equals another ion if their molecular formulas match
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Ion)) return false;
		
		Ion ion = (Ion) obj;
		
		return ion.symbol.equalsIgnoreCase(this.symbol);
	}
}
