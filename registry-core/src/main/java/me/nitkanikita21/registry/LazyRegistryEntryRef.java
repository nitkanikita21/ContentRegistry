package me.nitkanikita21.registry;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;

import java.util.Set;


/**
 * Represents a lazy reference to a registry entry. This class provides a way to access
 * a registry entry by its key, resolving the entry only when needed.
 *
 * @param <T> the type of the value stored in the registry entry
 */
public class LazyRegistryEntryRef<T> implements RegistryEntry<T> {

    /**
     * The key associated with this registry entry reference.
     */
    @Getter
    private final Key key;

    /**
     * The registry from which this entry will be retrieved.
     */
    @Getter
    private final Registry<T> registry;

    private @Nullable RegistryEntry<T> lazyEntry = null;

    /**
     * Constructs a lazy registry entry reference.
     *
     * @param key      the key of the registry entry
     * @param registry the registry where the entry resides
     */
    public LazyRegistryEntryRef(Key key, Registry<T> registry) {
        this.key = key;
        this.registry = registry;
    }

    /**
     * Gets the value of the registry entry. This will resolve the lazy reference if it
     * hasn't already been resolved.
     *
     * @return the value stored in the registry entry
     * @throws RuntimeException if the key cannot be resolved to a registry entry
     */
    @Override
    public T getValue() {
        return getLazyEntry().getValue();
    }

    /**
     * Gets the set of tags associated with the registry entry. This will resolve the lazy
     * reference if it hasn't already been resolved.
     *
     * @return a set of tags associated with the entry
     * @throws RuntimeException if the key cannot be resolved to a registry entry
     */
    @Override
    public Set<Key> getTags() {
        return getLazyEntry().getTags();
    }

    private RegistryEntry<T> getLazyEntry() {
        if(lazyEntry == null) {
            lazyEntry = registry.getEntry(key).orElseThrow();
        }
        return lazyEntry;
    }
}