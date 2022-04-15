package chem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A class representing a compound (two or more unique elements combined) in chemistry.
 * @author Sam Hieken
 *
 */
public class Compound {
	private int		CID,
					rotatableBonds;
	
	private String	name,
					molecularFormula;
	
	private double	polarArea,
					molecularWeight;

	/**
	 * For use in class only.
	 */
	private Compound(int CID, String name, double molecularWeight, 
					String molecularFormula, double polarArea, 
					int rotatableBonds) {
		
		this.CID = CID;
		this.rotatableBonds = rotatableBonds;
		
		this.name = name;
		this.molecularFormula = molecularFormula;
		
		this.polarArea = polarArea;
		this.molecularWeight = molecularWeight;
	}


	//----------------------------------------------------------------------	

	/**
	 * Get data about a compound <br>
	 * <i>note: charge appears as +n/-n, not n+/n-</i><br><br>
	 * O(n) time complexity (n = number of compounds in table)
	 * @param symbol The compound's symbol, ex: {@code Mg+2} (Magnesium), {@code CO2} (carbon dioxide)
	 * {@code 

	 * @throws IOException if the data table (ions.csv) could not be located 
	 */
	public static Compound getCompound(String symbol) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("rsc/compounds.csv"));

		String line = "";

		while ((line = br.readLine()) != null) {

			String[] split = line.split("\\,");
			// Found the compound in table
			if (split[3].equals(symbol)) {

				br.close();
				return new Compound(Integer.parseInt(split[0]), split[1], 
								Double.parseDouble(split[2]), split[3],
								Double.parseDouble(split[4]), Integer.parseInt(split[5]));
			}
		}

		br.close();
		throw new ChemicalNotFoundException("Compound " 
				+ symbol + " could not be found in the data table.");
	}
	
	/**
	 * Get Pubchem's CID for this compound.
	 */
	public int getCID() {
		return CID;
	}

	/**
	 * Get # of rotatable bonds
	 */
	public int getRotatableBonds() {
		return rotatableBonds;
	}

	/**
	 * Get name of compound
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get molecular formula of compound
	 */
	public String getMolecularFormula() {
		return molecularFormula;
	}

	/**
	 * Get polar area of compound.
	 */
	public double getPolarArea() {
		return polarArea;
	}

	/**
	 * Get the molecular weight of this compound.
	 */
	public double getMolecularWeight() {
		return molecularWeight;
	}
	
	/**
	 * @return {@code "Compound Name (molecular formula)"}
	 */
	@Override
	public String toString() {
		return this.getName() + 
				" (" + this.getMolecularFormula() + ")";
	}
	
	/**
	 * Equals another compound if molecular formulas match.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Compound)) return false;
		
		Compound cmpd = (Compound) obj;
		
		return cmpd.molecularFormula.equals(this.molecularFormula);
	}
}