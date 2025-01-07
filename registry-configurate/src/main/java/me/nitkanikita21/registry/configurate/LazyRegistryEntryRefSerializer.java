package me.nitkanikita21.registry.configurate;

import me.nitkanikita21.registry.Identifier;
import me.nitkanikita21.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import me.nitkanikita21.registry.LazyRegistryEntryRef;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class LazyRegistryEntryRefSerializer implements TypeSerializer<LazyRegistryEntryRef<?>> {
    @Override
    public LazyRegistryEntryRef<?> deserialize(Type type, ConfigurationNode node) throws SerializationException {
        assert node != null;

        assert type instanceof ParameterizedType;
        assert ((ParameterizedType) type).getActualTypeArguments().length == 1;

        assert node.hasChild("from");
        assert node.hasChild("id");

        Identifier registryKey = new Identifier(node.node("from").getString());
        Identifier valueKey = new Identifier(node.node("id").getString());

        Registry<?> registry = Registry.REGISTRY.get(registryKey).getOrElseThrow(RuntimeException::new);

        return new LazyRegistryEntryRef<>(valueKey, registry);

    }

    @Override
    public void serialize(Type type, @Nullable LazyRegistryEntryRef<?> obj, ConfigurationNode node) throws SerializationException {
        if(obj == null) {
            node.set(null);
            return;
        }

        assert type instanceof ParameterizedType;
        assert ((ParameterizedType) type).getActualTypeArguments().length == 1;

        node.node("from").set(obj.getRegistry().getId().toString());
        node.node("id").set(obj.getId().toString());

    }
}
