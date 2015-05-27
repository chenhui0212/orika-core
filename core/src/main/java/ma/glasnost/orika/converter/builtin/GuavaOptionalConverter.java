package ma.glasnost.orika.converter.builtin;

import com.google.common.base.Optional;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

/**
 * <p>
 *     Converter which will convert one {@linkplain Optional} field into another optional field.
 * </p>
 * <p>
 *     For those that wish to support the Java 8 <code>java.util.Optional</code> class, the code is exactly the
 *     same as this class apart from the static methods to construct the <code>Optional</code> classes.
 * </p>
 * @param <S> Type of the optional field to map from.
 * @param <D> Type of the optional field to map to.
 */
public class GuavaOptionalConverter<S, D> implements Converter<Optional<S>, Optional<D>> {

    private final Type<S> sourceType;
    private final Type<D> destinationType;
    private MapperFacade mapper;

    /**
     * Construct a new {@linkplain Optional} converter configured to convert an {@linkplain Optional} field
     * to another <code>Optional</code> field.
     * @param sourceType Type the source {@linkplain Optional} field contains.
     * @param destinationType Type the destination {@linkplain Optional} field will contain.
     */
    public GuavaOptionalConverter(final Type<S> sourceType, final Type<D> destinationType) {
        this.sourceType = sourceType;
        this.destinationType = destinationType;
    }

    public boolean canConvert(final Type<?> sourceType, final Type<?> destinationType) {
        final Type<?> sourceComponentType = sourceType.getComponentType();
        final Type<?> destinationComponentType = destinationType.getComponentType();

        return !(sourceComponentType == null || destinationComponentType == null)
                && this.sourceType.isAssignableFrom(sourceComponentType.getRawType())
                && this.destinationType.isAssignableFrom(destinationComponentType.getRawType());
    }

    public Optional<D> convert(final Optional<S> optionalSource, final Type<? extends Optional<D>> destinationType, final MappingContext mappingContext) {
        if (!optionalSource.isPresent()) {
            return Optional.absent();
        }

        final S source = optionalSource.get();

        return Optional.fromNullable(mapper.map(source, sourceType, this.destinationType, mappingContext));
    }

    public void setMapperFacade(final MapperFacade mapper) {
        this.mapper = mapper;
    }

    public Type<Optional<S>> getAType() {
        return getOptionalTypeOf(sourceType);
    }

    public Type<Optional<D>> getBType() {
        return getOptionalTypeOf(destinationType);
    }

    @SuppressWarnings("unchecked")
    private <T> Type<Optional<T>> getOptionalTypeOf(Type<T> type) {
        return (Type) TypeFactory.valueOf(Optional.class, type);
    }
}
