package me.nitkanikita21.registry;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents a deferred registry that allows items to be registered at a later point in time.
 * It supports registering items with a namespace and allows executing a callback after the registration.
 *
 * @param <T> the type of the items stored in the registry
 */
public class DeferredRegistry<T> {

    /**
     * The namespace used for identifying registered items.
     */
    @Getter
    final String namespace;

    /**
     * The actual registry where items will be stored after deferred registration.
     */
    @Getter
    final Registry<T> registry;

    /**
     * A callback function that is executed after an item is registered.
     */
    @Getter
    final RegistrationCallback<T> afterRegistry;

    /**
     * Constructs a DeferredRegistry with a given namespace and registry, without an after-registration callback.
     *
     * @param namespace the namespace for identifying items in the registry
     * @param registry the underlying registry where items will be registered
     */
    public DeferredRegistry(String namespace, Registry<T> registry) {
        this.registry = registry;
        this.namespace = namespace;
        this.afterRegistry = null;
    }

    /**
     * Constructs a DeferredRegistry with a given namespace, registry, and an after-registration callback.
     *
     * @param namespace the namespace for identifying items in the registry
     * @param registry the underlying registry where items will be registered
     * @param afterRegistry a callback to execute after registering each item
     */
    public DeferredRegistry(String namespace, RegistryImpl<T> registry, RegistrationCallback<T> afterRegistry) {
        this.namespace = namespace;
        this.registry = registry;
        this.afterRegistry = afterRegistry;
    }

    /**
     * A map of items to be registered, with their associated keys.
     */
    final Map<Identifier, T> items = new HashMap<>();

    /**
     * Registers an item with a given path and returns the registered item.
     *
     * @param path the path (or unique identifier) for the item
     * @param value the item to be registered
     * @param <I> the type of the item, which must be a subclass of T
     * @return the registered item
     */
    public <I extends T> I register(String path, I value) {
        items.put(new Identifier(namespace, path), value);
        return value;
    }

    /**
     * Registers all deferred items into the underlying registry and executes the after-registration callback
     * for each item, if it exists.
     */
    public void registerAll() {
        items.forEach((id, item) -> {
            if(afterRegistry != null) {
                afterRegistry.onRegistered(item, id, registry);
            }
            registry.register(id, item);
        });
    }

    public Map<Identifier, T> getItems() {
        return ImmutableMap.copyOf(items);
    }

    public interface RegistrationCallback<T> {
        void onRegistered(T item, Identifier id, Registry<T> registry);
    }
}