package me.nitkanikita21.example;

import io.leangen.geantyref.TypeToken;
import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.nitkanikita21.registry.Identifier;
import me.nitkanikita21.registry.RegistryEntry;
import me.nitkanikita21.registry.cloud.IdentifierParser;
import me.nitkanikita21.registry.cloud.RegistryEntryIdentifierParser;
import me.nitkanikita21.registry.cloud.RegistryEntryParser;
import me.nitkanikita21.registry.cloud.RegistryEntryValueParser;
import org.bukkit.Material;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.bukkit.parser.NamespacedKeyParser;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.paper.util.sender.Source;

import static me.nitkanikita21.registry.cloud.IdentifierParser.identifierParser;
import static me.nitkanikita21.registry.cloud.RegistryEntryIdentifierParser.registryEntryIdentifierParser;
import static me.nitkanikita21.registry.cloud.RegistryEntryParser.registryEntryParser;
import static me.nitkanikita21.registry.cloud.RegistryEntryValueParser.registryEntryValueParser;
import static org.incendo.cloud.bukkit.parser.NamespacedKeyParser.namespacedKeyParser;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Commands {
    final CommandManager<Source> manager;

    public void initialize() {
        CloudKey<Identifier> registryIdKey = CloudKey.of("id", Identifier.class);
        CloudKey<Material> registryValueKey = CloudKey.of("value", Material.class);
        CloudKey<RegistryEntry<Material>> registryEntryKey = CloudKey.of("value", new TypeToken<>() {});

        Command.Builder<Source> test = manager.commandBuilder("test");

        manager.command(
            test.literal("id")
                .required(registryIdKey, registryEntryIdentifierParser(ItemRegistry.REGISTRY))
                .handler(ctx -> {
                    Identifier identifier = ctx.get(registryIdKey);
                    ctx.sender().source().sendMessage("Just test command for argument parsers");
                })
        );

        manager.command(
            test.literal("value")
                .required(registryValueKey, registryEntryValueParser(ItemRegistry.REGISTRY))
                .handler(ctx -> {
                    Material material = ctx.get(registryValueKey);
                    ctx.sender().source().sendMessage("Just test command for argument parsers");
                })
        );
        manager.command(
            test.literal("entry")
                .required(registryEntryKey, registryEntryParser(ItemRegistry.REGISTRY))
                .handler(ctx -> {
                    RegistryEntry<Material> materialRegistryEntry = ctx.get(registryEntryKey);

                    ctx.sender().source().sendMessage("Just test command for argument parsers");
                })
        );

        manager.command(
            test.literal("namespaced")
                .required("namespaced", namespacedKeyParser())
                .handler(ctx -> {
                    ctx.get("namespaced");

                    ctx.sender().source().sendMessage("Just test command for argument parsers");
                })
        );
        manager.command(
            test.literal("identifier")
                .required("identifier", identifierParser())
                .handler(ctx -> {
                    ctx.get("identifier");

                    ctx.sender().source().sendMessage("Just test command for argument parsers");
                })
        );
    }
}
