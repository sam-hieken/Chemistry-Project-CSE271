package chem;

/**
 * Thrown when a chemical, element, or ion is not found in the data tables.
 * @author Sam Hieken
 *
 */
public class ChemicalNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Just the basic RuntimeException constructor
	 * @param msg error msg
	 */
	public ChemicalNotFoundException(String msg) {
		super(msg);
	}
}
