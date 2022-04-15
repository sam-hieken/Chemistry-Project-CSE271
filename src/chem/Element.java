package chem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Represents an element in chemistry.
 * <br> 
 * Note that any non-calculated values will be public and final <br>
 * to avoid excess amounts of getters.
 * <br>
 * 
 * <i>WARNING: Data table is incomplete, unknown double values will equal NaN,
 * unknown ints will equal Integer.MIN_VALUE, and unknown Strings will equal null</i>
 * 
 * Density: g/mL
 * @author Sam Hieken
 *
 */
public class Element {
	
	public final int	atomicNumber, 
						period, 
						group, 
						isotopes, 
						valenceElectrons;
	
	public final double	atomicMass, 
						ionicRadius, 
						atomicRadius, 
						electroNegativity, 
						firstIonization,
						density, 
						meltingPoint, 
						boilingPoint,
						specificHeat;
	
	public final String	element,
						symbol,
						phase,
						type,
						electronConfig;
	
	/**
	 * For use in class only.
	 */
	private Element(int atomicNumber, String element, String symbol, double atomicMass, int period, int group, 
			String phase, String type, double ionicRadius, double atomicRadius, double electroNegativity,
			double firstIonization, double density, double meltingPoint, double boilingPoint,
			int isotopes, double specificHeat, String electronConfig, int valenceElectrons) 
	{
		this.atomicNumber = atomicNumber;
		this.period = period;
		this.group = group;
		this.isotopes = isotopes;
		this.valenceElectrons = valenceElectrons;
		this.atomicMass = atomicMass;
		this.ionicRadius = ionicRadius;
		this.atomicRadius = atomicRadius;
		this.electroNegativity = electroNegativity;
		this.firstIonization = firstIonization;
		this.density = density;
		this.meltingPoint = meltingPoint;
		this.boilingPoint = boilingPoint;
		this.specificHeat = specificHeat;
		this.electronConfig = electronConfig;
		this.element = element;
		this.symbol = symbol;
		this.phase = phase;
		this.type = type;
	}
	
	/** @return The number of moles based on given gram amount. */
	public double toMoles(double grams) { return grams / atomicMass; }
	/** @return The number of grams  based on given mole amount. */
	public double toGrams(double moles) { return moles * atomicMass; }
	
	/** @return mass in grams */
	public double getMass(double volume) { return density * volume; }
	/** @return volume in mL (or gm/cm<sup>3</sup>)*/
	public double getVolume(double mass) { return mass / density; }
	
	/** @return The abbreviated electron configuration of this element */
	public String getElectronConfig() { return electronConfig; }
	/** @return The full electron configuration of this element */
	public String getFullElectronConfig() { return getFullElectronConfig(electronConfig); }
	
	/** @return The meat and potatoes of the previous method */
	private String getFullElectronConfig(String electronConfig) {
		
		String config[] = electronConfig.split(" ");
		
		if (config[0].startsWith("[") && config[0].endsWith("]")) {
			String symbol = config[0].substring(1, config[0].length() - 1);
			try {
				String cat = Element.getElement(ElemList.class.getField(symbol).getInt(null)).electronConfig + " ";
				
				for (int i = 1; i < config.length; i++) {
					cat += config[i];
					
					if (i < config.length - 1) cat += " ";
				}
				
				return getFullElectronConfig(cat);
				
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
					| IOException e) {
				e.printStackTrace();
				
				return null;
			}
		}
		
		else return electronConfig;
	}
	
	
//	--------------------------------------------------------------------------------
	
	/**
	 * Get an element based on its atomic number. <br>
	 * <i>It is recommended to use {@link ElemList}.[elementSymbol]</i><br><br>
	 * O(n) time complexity (n = number of elements in table = 118)
	 * @param atomicNumber
	 * @return
	 * @throws IOException
	 */
	public static Element getElement(int atomicNumber) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader("rsc/ptable.csv"));
		
		String line = "";
		
		while ((line = br.readLine()) != null) {
			
			String[] split = line.split("\\,");
			
			// Found the element in table
			if (split[0].equals(Integer.toString(atomicNumber))) {
				
				br.close();
				return new Element(parseInt(split[0]),
						split[1], parseString(split[2]), parseDouble(split[3]), parseInt(split[4]), 				
						parseInt(split[5]), parseString(split[6]), parseString(split[7]),
						parseDouble(split[8]), parseDouble(split[9]), parseDouble(split[10]), 
						parseDouble(split[11]), parseDouble(split[12]), parseDouble(split[13]), 
						parseDouble(split[14]), parseInt(split[15]),
						parseDouble(split[16]), parseString(split[17]), parseInt(split[18]));
				
			}
		}
		
		br.close();
		throw new ChemicalNotFoundException("Chemical " 
				+ atomicNumber + " does not exist, it is recommended to use ElemList.");
	}
	
	/**
	 * Checks if the given quantum numbers are valid.
	 * @param ls The list of quantum numbers, in the order: n, l, ml, ms
	 * @return True if they're valid
	 */
	public static boolean validQuantum(double...ls) {
		int size = ls.length;
		
		double n = ls[0];
		
		// n not valid
		if (n < 0 || (n % 1) != 0) return false;
		// n is valid and only number
		else if (size == 1) return true;
		
		double l = ls[1];
		
		// l not valid
		if (!(l >= 0 && l <= n - 1) || (l % 1) != 0) return false;
		// l is valid and n/l are only numbers
		else if (ls.length == 2) return true;
		
		double ml = ls[2];
		
		if (!(ml >= -l && ml <= l) || (l % 1) != 0) {
			return false;
		}
		else if (ls.length == 3) return true;
		
		double ms = ls[3];
		
		if (ms != 0.5 && ms != -0.5) return false;
		else if (ls.length == 4) return true;
		
		return false;
	}
	
	/**
	 * Shorthand for {@code Double.parseDouble()}; returns NaN if {@link NumberFormatException} occurs
	 * @param str
	 * @return
	 */
	private static double parseDouble(String str) {
		try {
			return Double.parseDouble(str);
		}
		catch (NumberFormatException e) {
			return Double.NaN;
		}
	}
	
	/**
	 * Shorthand for {@code Integer.parseInt()}; returns Integer.MIN_VALUE if exceptions occurs
	 * @param str
	 * @return
	 */
	private static int parseInt(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			// No NaN equivelant for int; at least the min value should indicate something went very wrong
			return Integer.MIN_VALUE;
		}
	}
	
	/**
	 * Converts empty strings to null; returns string otherwise
	 */
	private static String parseString(String str) {
		return str.equals("") ? null : str;
	}
}
