package me.nitkanikita21.registry;

import com.google.common.collect.Lists;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

/**
 * Implementation of the IRegistry interface. This registry stores elements
 * and allows them to be categorized and retrieved by tags.
 *
 * @param <T> the type of the element stored in the registry
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RegistryImpl<T> implements Registry<T> {

    @Getter
    private final Identifier identifier;

    private final Map<Identifier, T> entries = new HashMap<>();
    private final Map<Identifier, Set<Identifier>> tags = new HashMap<>();

    private boolean isFrozen = false;

    @Override
    public RegistryEntry<T> register(Identifier id, T value) {
        entries.put(id, value);
        return new RegistryEntryImpl<>(this, id, value);
    }

    @Override
    public Option<T> get(Identifier id) {
        return Option.of(entries.get(id));
    }

    @Override
    public Option<RegistryEntry<T>> getEntry(Identifier id) {
        return get(id).map(obj -> new RegistryEntryImpl<>(this, id, obj));
    }

    @Override
    public boolean contains(Identifier id) {
        return entries.containsKey(id);
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
    public void setTag(Identifier tag, List<Identifier> ids) {
        tags.computeIfAbsent(tag, k -> new HashSet<>()).addAll(ids);
    }

    @Override
    public void addToTag(Identifier tag, Identifier id) {
        tags.computeIfAbsent(tag, k -> new HashSet<>()).add(id);
    }

    @Override
    public List<RegistryEntry<T>> getAllEntriesByTag(Identifier tag) {
        return Option.of(tags.get(tag))
            .map(entriesWithTag -> entriesWithTag.stream()
                .map(entryIdentifier -> getEntry(entryIdentifier).getOrNull())
                .filter(Objects::nonNull)
                .toList()
            ).getOrElse(List::of);
    }

    @Override
    public List<T> getAllByTag(Identifier tag) {
        return getAllEntriesByTag(tag)
            .stream()
            .map(RegistryEntry::getValue)
            .toList();
    }

    @Override
    public List<Identifier> getAllTags() {
        return tags.keySet().stream().toList();
    }

}