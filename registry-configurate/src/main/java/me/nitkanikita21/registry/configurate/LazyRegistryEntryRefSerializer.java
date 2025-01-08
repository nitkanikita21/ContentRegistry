package me.nitkanikita21.registry.configurate;

import me.nitkanikita21.registry.Identifier;
import me.nitkanikita21.registry.LazyRegistryEntryRef;
import me.nitkanikita21.registry.Registry;
import me.nitkanikita21.registry.exception.RegistryEntryNotFoundException;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public class LazyRegistryEntryRefSerializer implements TypeSerializer<LazyRegistryEntryRef<?>> {
    @Override
    public LazyRegistryEntryRef<?> deserialize(Type type, ConfigurationNode node) throws SerializationException, AssertionError {
        Objects.requireNonNull(node);

        assert type instanceof ParameterizedType : "Must be ParameterizedType";
        assert ((ParameterizedType) type).getActualTypeArguments().length == 1 : "Must contains generic type";

        Identifier registryKey;
        Identifier valueKey;

        if (node.isMap()) {
            assert node.hasChild("from") : "Must contains child node \"from\"";
            assert node.hasChild("id") : "Must contains child node \"id\"";

            registryKey = new Identifier(node.node("from").getString());
            valueKey = new Identifier(node.node("id").getString());
        } else {
            String[] nodeContent = node.getString().split("/");

            registryKey = new Identifier(nodeContent[0]);
            valueKey = new Identifier(nodeContent[1]);
        }

        Registry<?> registry = Registry.REGISTRY.get(registryKey).getOrElseThrow(() ->
            new SerializationException(
                new RegistryEntryNotFoundException(
                    String.format("Registry entry %s not found in registry %s", valueKey, registryKey)
                )
            )
        );

        return new LazyRegistryEntryRef<>(valueKey, registry);

    }

    @Override
    public void serialize(Type type, @Nullable LazyRegistryEntryRef<?> obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }

        assert type instanceof ParameterizedType : "Must be ParameterizedType";
        assert ((ParameterizedType) type).getActualTypeArguments().length == 1 : "Must contains generic type";

        node.set(String.class, obj.getRegistry().getId() + "/" + obj.getId());

    }
}
