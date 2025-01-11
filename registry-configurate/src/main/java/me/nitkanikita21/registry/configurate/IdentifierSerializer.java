package me.nitkanikita21.registry.configurate;

import io.leangen.geantyref.TypeToken;
import me.nitkanikita21.registry.Identifier;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public final class IdentifierSerializer extends ScalarSerializer<Identifier> {
    public IdentifierSerializer() {
        super(Identifier.class);
    }

    @Override
    public Identifier deserialize(Type type, Object obj) throws SerializationException {
        return new Identifier(obj.toString());
    }

    @Override
    protected Object serialize(Identifier item, Predicate<Class<?>> typeSupported) {
        return item.toString();
    }
}
