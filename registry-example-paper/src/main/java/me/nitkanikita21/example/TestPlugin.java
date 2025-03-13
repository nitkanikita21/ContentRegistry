package me.nitkanikita21.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.nitkanikita21.example.config.TestConfig;
import me.nitkanikita21.registry.configurate.RegistryConfigurate;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.io.IOException;

@Slf4j
public class TestPlugin extends JavaPlugin {

    @Override
    @SneakyThrows
    public void onEnable() {
        File configFile = new File(
            getDataFolder(),
            "config.conf"
        );

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .file(configFile)
            .defaultOptions(opts ->
                opts.serializers(RegistryConfigurate.typeSerializerCollection())
            )
            .build();

        CommentedConfigurationNode root = loader.createNode().set(new TestConfig());

        if (!configFile.exists()) {
            loader.save(root);
        }

        try {
            root = loader.load();
        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        }

        TestConfig config = root.get(TestConfig.class);
        loader.save(root);

        assert config != null;
        config.getItemsDeferredRegistry().registerAll((itemMaterial, id, registry) -> {
            log.info("{} registered to items registry", itemMaterial.toString());
        });

        freezeRegistries();

        var sender = getServer().getConsoleSender();
        ItemRegistry.REGISTRY.getAll().forEach((entry) -> {
            sender.sendMessage(entry.getIdentifier().toString());
            entry.getTags().forEach(t -> {
                sender.sendMessage("   # " + t.toString());
            });
        });
    }

    private void freezeRegistries() {
        ItemRegistry.REGISTRY.freeze();
    }

}
