package jbse.val;

import static jbse.val.HistoryPoint.unknown;

import jbse.common.Type;
import jbse.common.exc.InvalidInputException;
import jbse.val.exc.InvalidOperandException;
import jbse.val.exc.InvalidTypeException;

/**
 * Class representing the values resulting from a narrowing
 * conversion between numeric primitive types.
 * 
 * @author Pietro Braione
 *
 */
public final class NarrowingConversion extends PrimitiveSymbolic {
    private final Primitive arg;
    private final int hashCode;
    private final String toString;
    private final String originString;

    private NarrowingConversion(char type, Calculator calc, Primitive arg) 
    throws InvalidOperandException, InvalidTypeException, InvalidInputException {
        super(type, unknown(), calc); //TODO put sensible history point?

        //checks on parameters
        if (arg == null) {
            throw new InvalidOperandException("Null operand in narrowing construction.");
        }
        if (!Type.narrows(type, arg.getType())) {
            throw new InvalidTypeException("Cannot narrow type " + arg.getType() + " to type " + type + ".");
        }
        
        this.arg = arg;

        //calculates hashCode
        final int prime = 311;
        int result = 1;
        result = prime * result + arg.hashCode();
        result = prime * result + type;
        this.hashCode = result;

        //calculates toString
        this.toString = "NARROW-"+ getType() + "(" + arg.toString() + ")";

        //calculates originString
        this.originString = "NARROW-"+ getType() + "(" + (arg.isSymbolic() ? ((Symbolic) arg).asOriginString() : arg.toString()) + ")";
    }

    /**
     * Constructs a {@link NarrowingConversion}.
     * 
     * @param type a {@code char}, the destination type of the conversion.
     * @param calc a {@link Calculator}. It must not be {@code null}.
     * @param arg a {@link Primitive}, the value that is being narrowed. 
     *        It must not be {@code null}.
     * @return a {@link NarrowingConversion}.
     * @throws InvalidOperandException if {@code arg == null}.
     * @throws InvalidTypeException if {@code arg} cannot be narrowed to {@code type},
     *         or {@code type} is not a valid primitive type.
     * @throws InvalidInputException if {@code calc == null}.
     */
    public static NarrowingConversion make(char type, Calculator calc, Primitive arg) 
    throws InvalidOperandException, InvalidTypeException, InvalidInputException {
        return new NarrowingConversion(type, calc, arg);
    }

    public Primitive getArg() {
        return this.arg;
    }

	@Override
	public String asOriginString() {
		return this.originString;
	}

	@Override
	public Symbolic root() {
		return this;
	}

	@Override
	public boolean hasContainer(Symbolic s) {
		if (s == null) {
			throw new NullPointerException();
		}
		return equals(s);
	}

    @Override
    public void accept(PrimitiveVisitor v) throws Exception {
        v.visitNarrowingConversion(this);
    }

    @Override
    public String toString() {
        return this.toString;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NarrowingConversion other = (NarrowingConversion) obj;
        if (!this.arg.equals(other.arg)) {
            return false;
        }
        return true;
    }

}
