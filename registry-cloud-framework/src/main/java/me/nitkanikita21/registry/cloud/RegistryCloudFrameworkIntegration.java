package me.nitkanikita21.registry.cloud;

import com.mojang.brigadier.arguments.ArgumentType;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.vavr.collection.Iterator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.brigadier.CloudBrigadierManager;
import org.incendo.cloud.bukkit.internal.MinecraftArgumentTypes;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionProvider;
import org.incendo.cloud.paper.PaperCommandManager;

@UtilityClass
public class RegistryCloudFrameworkIntegration {
    public static <C> void initialize(CommandManager<C> commandManager) {
        IdentifierParser.registerParserSupplier(commandManager);

        commandManager.captionRegistry().registerProvider(CaptionProvider.constantProvider(
            Captions.ARGUMENT_PARSE_FAILURE_REGISTRY_ENTRY,
            "Failed to parse registry entry for input <input>."
        ));
    }

    public static <C> void initializePaper(PaperCommandManager<C> commandManager) {
        initialize(commandManager);

        CloudBrigadierManager<C, ? extends CommandSourceStack> brigadierManager = commandManager.brigadierManager();
        Class<? extends ArgumentType<?>> resourceLocation = MinecraftArgumentTypes.getClassByKey(NamespacedKey.minecraft("resource_location"));

        Iterator.of(
            new TypeToken<IdentifierParser<C>>() {},
            new TypeToken<RegistryEntryParser<C, ?>>() {},
            new TypeToken<RegistryEntryValueParser<C, ?>>() {},
            new TypeToken<RegistryEntryIdentifierParser<C, ?>>() {}
        ).forEach(type -> {
            brigadierManager.registerMapping(
                type,
                builder -> {
                    builder.cloudSuggestions();
                    try {
                        builder.toConstant(resourceLocation.newInstance());
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
        });


        System.out.println("s");
    }

    @UtilityClass
    @FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
    public static class Captions {
        Caption ARGUMENT_PARSE_FAILURE_REGISTRY_ENTRY = Caption.of("argument.parse.failure.registry_entry");
    }
}
