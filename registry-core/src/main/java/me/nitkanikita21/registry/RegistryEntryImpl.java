package me.nitkanikita21.registry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple implementation of the IRegistryEntry interface, which represents
 * an entry in the registry, holding a key and an associated value.
 *
 * @param <T> the type of the element stored in the registry
 */
@RequiredArgsConstructor
@Getter
public class RegistryEntryImpl<T> implements RegistryEntry<T> {

    private final Registry<T> registry;
    private final Key key;
    private final T value;

    @Override
    public Set<Key> getTags() {
        return registry.getAllTags().stream()
            .filter((tagKey) -> registry.getAllByTag(tagKey).contains(value))
            .collect(Collectors.toSet());
    }
}