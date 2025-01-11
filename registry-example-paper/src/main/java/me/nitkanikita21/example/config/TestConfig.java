package me.nitkanikita21.example.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.nitkanikita21.example.ItemRegistry;
import me.nitkanikita21.registry.configurate.ConfigDeferredRegistry;
import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@NoArgsConstructor
@Getter
public class TestConfig {
    @Setting("items")
    @Comment("Registry for all items")
    ConfigDeferredRegistry<Material> itemsDeferredRegistry = new ConfigDeferredRegistry<>(ItemRegistry.REGISTRY);
}