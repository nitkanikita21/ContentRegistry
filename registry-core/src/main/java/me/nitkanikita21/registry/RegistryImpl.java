package me.nitkanikita21.registry;

import com.google.common.collect.Lists;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Implementation of the IRegistry interface. This registry stores elements
 * and allows them to be categorized and retrieved by tags.
 *
 * @param <T> the type of the element stored in the registry
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RegistryImpl<T> implements Registry<T> {

    private final Key id;

    private final Map<Key, T> entries = new HashMap<>();
    private final Map<Key, Set<Key>> tags = new HashMap<>();

    private boolean isFrozen = false;

    @Override
    public RegistryEntry<T> register(Key key, T value) {
        return new RegistryEntryImpl<>(this, key, entries.put(key, value));
    }

    @Override
    public Option<T> get(Key key) {
        return Option.of(entries.get(key));
    }

    @Override
    public Option<RegistryEntry<T>> getEntry(Key key) {
        return get(key).map(obj -> new RegistryEntryImpl<>(this, key, obj));
    }

    @Override
    public boolean contains(Key key) {
        return entries.containsKey(key);
    }

    @Override
    public void freeze() {
        isFrozen = true;
    }

    @Override
    public boolean isFrozen() {
        return isFrozen;
    }

    @Override
    public Iterator<RegistryEntry<T>> iterator() {
        return entries.entrySet()
            .stream().map(entry ->
                (RegistryEntry<T>) new RegistryEntryImpl<>(this, entry.getKey(), entry.getValue())
            )
            .iterator();
    }

    @Override
    public List<RegistryEntry<T>> getAll() {
        return Lists.newArrayList(iterator());
    }

    @Override
    public void setTag(Key tag, List<Key> keys) {
        tags.computeIfAbsent(tag, k -> new HashSet<>()).addAll(keys);
    }

    @Override
    public void addToTag(Key key, Key tag) {
        tags.computeIfAbsent(tag, k -> new HashSet<>()).add(key);
    }

    @Override
    public List<RegistryEntry<T>> getAllEntriesByTag(Key tag) {
        return Option.of(tags.get(tag))
            .map(entriesWithTag -> entriesWithTag.stream()
                .map(entryKey -> getEntry(entryKey).getOrNull())
                .filter(Objects::nonNull)
                .toList()
            ).getOrElse(List::of);
    }

    @Override
    public List<T> getAllByTag(Key tag) {
        return getAllEntriesByTag(tag)
            .stream()
            .map(RegistryEntry::getValue)
            .toList();
    }

    @Override
    public List<Key> getAllTags() {
        return tags.keySet().stream().toList();
    }


    @Override
    public @NotNull Key key() {
        return id;
    }
}