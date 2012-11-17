package constraint;

public class RepugnantConditionException extends Exception {
	public RepugnantConditionException(Exception e) {
		super(e);
	}

	private static final long serialVersionUID = -5331169497594547641L;
}
