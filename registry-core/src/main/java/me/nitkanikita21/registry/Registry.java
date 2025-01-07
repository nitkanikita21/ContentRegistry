package me.nitkanikita21.registry;

import io.vavr.control.Option;

import java.util.Iterator;
import java.util.List;

/**
 * Represents a registry that maps unique ids to elements of type T.
 * Provides methods to register, retrieve, and manage elements in the registry.
 * Supports both dynamic and frozen registries.
 *
 * @param <T> the type of elements stored in the registry
 */
public interface Registry<T> extends Identifiable {

    Registry<Registry<?>> REGISTRY = new RegistryImpl<>(
        new Identifier("registry")
    );

    static <T> Registry<T> create(Identifier key) {
        return (Registry<T>) REGISTRY.register(key, new RegistryImpl<T>(key)).getValue();
    }

    /**
     * Registers an object in the registry under a specific id.
     * This operation can only be performed if the registry is dynamic.
     *
     * @param id   the unique identifier for the element
     * @param value the element to be registered
     * @return the registered element wrapped as a RegistryEntry
     * @throws IllegalStateException if the registry is frozen
     */
    RegistryEntry<T> register(Identifier id, T value);

    /**
     * Retrieves an element from the registry by its unique id.
     *
     * @param id the unique identifier
     * @return the element if found, or Option.empty() if not found
     */
    Option<T> get(Identifier id);

    /**
     * Retrieves the RegistryEntry containing the element and its metadata from the registry by its unique id.
     *
     * @param id the unique identifier
     * @return the RegistryEntry if found, or Option.empty() if not found
     */
    Option<RegistryEntry<T>> getEntry(Identifier id);

    /**
     * Checks if an element exists in the registry with the specified id.
     *
     * @param id the unique identifier
     * @return true if the element exists in the registry
     */
    boolean contains(Identifier id);

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
     * Sets a tag for a list of ids, associating them with the specified tag.
     *
     * @param tag the tag to be associated with the elements
     * @param ids the list of ids that should be associated with the tag
     */
    void setTag(Identifier tag, List<Identifier> ids);

    /**
     * Adds an entry to an existing tag.
     *
     * @param tag the tag to which the element should be added
     * @param id the id of the element to add to the tag
     */
    void addToTag(Identifier tag, Identifier id);

    /**
     * Adds an entry to an existing tag.
     *
     * @param tag the tag to which the element should be added
     * @param ids the ids of the elements to add to the tag
     */
    default void addToTag(Identifier tag, Identifier... ids) {
        for (Identifier key : ids) {
            addToTag(tag, key);
        }
    }

    /**
     * Retrieves all elements associated with a specific tag.
     *
     * @param tag the tag to search for elements
     * @return the list of elements associated with the tag
     */
    List<RegistryEntry<T>> getAllEntriesByTag(Identifier tag);

    /**
     * Retrieves all elements associated with a specific tag.
     *
     * @param tag the tag to search for elements
     * @return the list of elements associated with the tag
     */
    List<T> getAllByTag(Identifier tag);

    List<Identifier> getAllTags();
}
