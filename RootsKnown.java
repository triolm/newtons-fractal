public interface RootsKnown {
    public abstract int closestRootIndex(ComplexNumber n);

    public abstract ComplexNumber closestRoot(ComplexNumber n);

    public abstract ComplexNumber evaluate(ComplexNumber n);

    public abstract Function derivative();
}
