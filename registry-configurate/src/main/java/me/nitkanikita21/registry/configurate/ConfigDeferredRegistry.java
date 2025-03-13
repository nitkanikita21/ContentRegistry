package me.nitkanikita21.registry.configurate;

import me.nitkanikita21.registry.DeferredRegistry;
import me.nitkanikita21.registry.Identifier;
import me.nitkanikita21.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ConfigSerializable
public class ConfigDeferredRegistry<T> {

    private final String id;
    private Map<Identifier, T> entry = new HashMap<>();
    private @Nullable Map<Identifier, Set<Identifier>> tags = null;

    public ConfigDeferredRegistry(Identifier id) {
        this.id = id.toString();
    }

    public ConfigDeferredRegistry(Registry<T> registry) {
        this.id = registry.getIdentifier().toString();
    }

    private ConfigDeferredRegistry() {
        this.id = null;
    }

    public void registerAll(@Nullable DeferredRegistry.RegistrationCallback<T> afterRegister) {
        Registry<T> registry = getRegistry();
        entry.forEach((key, value) -> {
            registry.register(key, value);
            if (afterRegister != null) afterRegister.onRegistered(value, key, registry);
        });
        if(tags == null) return;

        tags.forEach((tag, tagEntry) -> {
            tagEntry.forEach(entry -> registry.addToTag(tag, entry));
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
