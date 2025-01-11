package me.nitkanikita21.example;

import me.nitkanikita21.registry.Identifier;
import me.nitkanikita21.registry.Registry;
import org.bukkit.Material;

public class ItemRegistry {
    public static Registry<Material> REGISTRY =
        Registry.create(new Identifier("example:items"));
}
