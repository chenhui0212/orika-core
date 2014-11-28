package ma.glasnost.orika.metadata.func;

/**
 * 
 * MapFunc aim to provide functional customization to ClassMapBuilder API
 * so it can be used inline as lambda
 * 
 * @author sidi-mohamed.elaatif
 *
 * @param <S> Source type
 * @param <D> Destination type
 */
@FunctionalInterface
public interface MapFunc<S,D> {
     void map(S source, D destination);
}
