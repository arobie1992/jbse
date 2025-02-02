package jbse.mem;

import jbse.bc.ClassFile;
import jbse.bc.Signature;
import jbse.common.exc.InvalidInputException;
import jbse.val.Calculator;
import jbse.val.HistoryPoint;
import jbse.val.KlassPseudoReference;

/**
 * Class that represents the shared portion of an object 
 * in the static method area, i.e., its static fields.
 */
public final class KlassImpl extends ObjektImpl implements Klass {
    private boolean initializationStarted;
    private boolean initializationCompleted;

    /**
     * Constructor.
     * 
     * @param calc a {@link Calculator}. It must not be {@code null}.
     * @param symbolic a {@code boolean}, whether this object is symbolic
     *        (i.e., not explicitly created during symbolic execution, 
     *        but rather assumed).
     * @param classFile a {@link ClassFile}, the class of this object
     *        (i.e., the class where the static fields this object gathers
     *        are declared). It must not be {@code null}.
     * @param origin a {@link KlassPseudoReference} "pointing" to this 
     *        {@link KlassImpl}.
     * @param epoch the creation {@link HistoryPoint} of this {@link KlassImpl}.
     * @param numOfStaticFields an {@code int}, the number of static fields.
     * @param fieldSignatures varargs of field {@link Signature}s, all the
     *        fields this object knows. It must not be {@code null}.
     * @throws InvalidInputException if {@code calc == null || classFile == null || fieldSignatures == null}.
     */
    KlassImpl(Calculator calc, boolean symbolic, ClassFile classFile, KlassPseudoReference origin, HistoryPoint epoch, int numOfStaticFields, Signature... fieldSignatures) 
    throws InvalidInputException {
        super(calc, symbolic, classFile, origin, epoch, true, numOfStaticFields, fieldSignatures);
        this.initializationStarted = false;
        this.initializationCompleted = false;
    }

	KlassWrapper makeWrapper(StaticMethodArea destinationStaticArea, ClassFile classFile) {
		return new KlassWrapper(destinationStaticArea, classFile, this);
	}

    @Override
    public boolean initializationStarted() {
        return this.initializationStarted;
    }

    @Override
    public boolean initializationCompleted() {
        return this.initializationCompleted;
    }

    @Override
    public void setInitializationStarted() {
        this.initializationStarted = true;
    }

    @Override
    public void setInitializationCompleted() {
        this.initializationStarted = true;
        this.initializationCompleted = true;
    }

    @Override
    public KlassImpl clone() {
        final KlassImpl o = (KlassImpl) super.clone();
        o.fields = fieldsDeepCopy();

        return o;
    }
}
