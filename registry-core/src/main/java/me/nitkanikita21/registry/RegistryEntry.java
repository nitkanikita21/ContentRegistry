package me.nitkanikita21.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Represents a wrapper around an element in the registry, providing access to the element and its metadata (key).
 *
 * @param <T> the type of the element stored in the registry
 */
public interface RegistryEntry<T> extends Identifiable {

    /**
     * Gets the element stored in this registry entry.
     *
     * @return the element
     */
    @NotNull T getValue();

    /**
     * Gets the unique key associated with this registry entry.
     *
     * @return the unique key
     */
    @NotNull Identifier getId();

    /**
     * Gets the tags associated with this registry entry.
     *
     * @return a set of tags associated with the entry
     */
     Set<Identifier> getTags();

}