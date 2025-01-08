package me.nitkanikita21.registry.configurate;

import io.leangen.geantyref.TypeToken;
import lombok.experimental.UtilityClass;
import me.nitkanikita21.registry.LazyRegistryEntryRef;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

@UtilityClass
public class RegistryConfigurate {
    public static TypeSerializerCollection typeSerializerCollection() {
        return TypeSerializerCollection.builder()
            .registerAll(TypeSerializerCollection.defaults())
            .register(new IdentifierSerializer())
            .register(new TypeToken<LazyRegistryEntryRef<?>>() {}, new LazyRegistryEntryRefSerializer())
            .registerAnnotatedObjects(ObjectMapper.factory())
            .build();
    }
}
