package me.nitkanikita21.registry;

public interface RegistryContent<T> {
    Registry<T> getRegistry();

    default Identifier getId() {
        return getRegistry().getIdentifier();
    }
}
