package me.nitkanikita21.registry.configurate;

import me.nitkanikita21.registry.Identifier;
import me.nitkanikita21.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@ConfigSerializable
public class ConfigDeferredRegistry<T> {

    private final String id;
    private Map<String, T> values = new HashMap<>();

    public ConfigDeferredRegistry(Identifier id) {
        this.id = id.toString();
    }

    public ConfigDeferredRegistry(Registry<T> registry) {
        this.id = registry.getId().toString();
    }

    public ConfigDeferredRegistry() {
        this.id = null;
    }

    public void registerAll(@Nullable Consumer<T> afterRegister) {
        Registry<T> registry = getRegistry();
        values.forEach((key, value) -> {
            registry.register(new Identifier(key), value);
            if (afterRegister != null) afterRegister.accept(value);
        });
    }

    public void registerAll() {
        registerAll(null);
    }

    public Registry<T> getRegistry() {
        return (Registry<T>) Registry.REGISTRY.get(new Identifier(id))
            .getOrElseThrow(RuntimeException::new);
    }
}
