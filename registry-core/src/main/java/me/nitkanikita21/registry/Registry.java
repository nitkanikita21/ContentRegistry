package me.nitkanikita21.registry;

import io.vavr.control.Option;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.util.Iterator;
import java.util.List;

/**
 * Represents a registry that maps unique keys to elements of type T.
 * Provides methods to register, retrieve, and manage elements in the registry.
 * Supports both dynamic and frozen registries.
 *
 * @param <T> the type of elements stored in the registry
 */
public interface Registry<T> extends Keyed {

    Registry<Registry<?>> REGISTRY = new RegistryImpl<>(
        Key.key("registry")
    );

    static <T> Registry<T> create(Key key) {
        return (Registry<T>) REGISTRY.register(key, new RegistryImpl<T>(key)).getValue();
    }

    /**
     * Registers an object in the registry under a specific key.
     * This operation can only be performed if the registry is dynamic.
     *
     * @param key   the unique identifier for the element
     * @param value the element to be registered
     * @return the registered element wrapped as a RegistryEntry
     * @throws IllegalStateException if the registry is frozen
     */
    RegistryEntry<T> register(Key key, T value);

    /**
     * Retrieves an element from the registry by its unique key.
     *
     * @param key the unique identifier
     * @return the element if found, or Option.empty() if not found
     */
    Option<T> get(Key key);

    /**
     * Retrieves the RegistryEntry containing the element and its metadata from the registry by its unique key.
     *
     * @param key the unique identifier
     * @return the RegistryEntry if found, or Option.empty() if not found
     */
    Option<RegistryEntry<T>> getEntry(Key key);

    /**
     * Checks if an element exists in the registry with the specified key.
     *
     * @param key the unique identifier
     * @return true if the element exists in the registry
     */
    boolean contains(Key key);

    /**
     * Freezes the registry, preventing further registrations.
     * Once frozen, no more elements can be added to the registry.
     */
    void freeze();

    /**
     * Checks if the registry is frozen.
     *
     * @return true if the registry is frozen, false otherwise
     */
    boolean isFrozen();

    /**
     * Returns an iterator over the elements in the registry.
     *
     * @return an iterator over the elements in the registry
     */
    Iterator<RegistryEntry<T>> iterator();

    /**
     * Returns a list of all entries in the registry.
     *
     * @return a list containing all RegistryEntries
     */
    List<RegistryEntry<T>> getAll();

    /**
     * Sets a tag for a list of keys, associating them with the specified tag.
     *
     * @param tag the tag to be associated with the elements
     * @param keys the list of keys that should be associated with the tag
     */
    void setTag(Key tag, List<Key> keys);

    /**
     * Adds an entry to an existing tag.
     *
     * @param tag the tag to which the element should be added
     * @param key the key of the element to add to the tag
     */
    void addToTag(Key tag, Key key);

    /**
     * Adds an entry to an existing tag.
     *
     * @param tag the tag to which the element should be added
     * @param keys the keys of the elements to add to the tag
     */
    default void addToTag(Key tag, Key... keys) {
        for (Key key : keys) {
            addToTag(tag, key);
        }
    }

    /**
     * Retrieves all elements associated with a specific tag.
     *
     * @param tag the tag to search for elements
     * @return the list of elements associated with the tag
     */
    List<RegistryEntry<T>> getAllEntriesByTag(Key tag);

    /**
     * Retrieves all elements associated with a specific tag.
     *
     * @param tag the tag to search for elements
     * @return the list of elements associated with the tag
     */
    List<T> getAllByTag(Key tag);

    List<Key> getAllTags();
}
