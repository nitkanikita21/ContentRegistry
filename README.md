# 🗃️ ContentRegistry
[![Build Status](https://ci.codemc.io/job/nitkanikita21/job/ContentRegistry/badge/icon)](https://ci.codemc.io/job/nitkanikita21/job/ContentRegistry/)
![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/me.nitkanikita21/registry-core?server=https%3A%2F%2Frepo.codemc.io)

**ContentRegistry** is a lightweight and extensible library for using registry system. Inspired by systems like
Minecraft's `Registry`, it provides a flexible API for registering, organizing, and retrieving content. The library
supports features such as lazy-loaded registry entries, deferred registration, tagging, and dynamic registries.

🙂 I appreciate constructive feedback and suggestions for improving the project. Feel free to open an issue or submit a
pull request with your ideas.

## ❓ What is Registry

This is a system that allows you to store any content in a single repository indexed by an identifier of the “namespace:
path” type. At its core, it is a dictionary with additional functionality for convenient work.

## 📌 Use it

### Gradle (Kotlin dsl)

```kt
repositories {
    maven("https://repo.codemc.io/repository/nitkanikita21/")
}
dependencies {
    val registryVer = "1.2.0-SNAPSHOT"
    implementation("me.nitkanikita21:registry-core:$registryVer")
    implementation("me.nitkanikita21:registry-configurate:$registryVer") // SpongePowered/Configurate integration
    implementation("me.nitkanikita21:registry-cloud-framework:$registryVer") // Cloud Command Framework integration
}
```

> [!IMPORTANT]  
> You need to ensure that classes are available at runtime. You can use
> the [Shadow Gradle Plugin](https://gradleup.com/shadow/) plugin for Gradle or load
> dependencies at server startup using
> the [PluginLoader system in Paper plugins](https://docs.papermc.io/paper/dev/getting-started/paper-plugins#loaders)

## 🧩 Features

* **Registry Management**: Create and manage registries for any type of content.
* **Deferred Registration**: Register content at a later stage in the lifecycle.
* **Tagging System**: Categorize and group content using a tag-based system.
* **Frozen Registries**: You can create registries that will be blocked for registration of new entries.
* **Lazy Entries**: Access registry entries only when needed to optimize performance.
* **Deferred Content Registration from Configuration**: Register content that is loaded dynamically from
  configuration files (e.g., JSON, YAML).

## ✨ Basic Examples:

### 🔹 Defining a Registry for Items

```java
// Define a registry for items
public class Items {
    public static final Registry<Item> REGISTRY = Registry.create(new Identifier("my:items"));

    // Register items statically
    public static final Item APPLE = register("apple", new Item("Apple"));
    public static final Item ORANGE = register("orange", new Item("Orange"));

    private static Item register(String path, Item item) {
        REGISTRY.register(new Identifier("my", path), item);
        return item;
    }
}
```

### 🔹 Accessing Items from the Registry

```java
void main() {
    Option<Item> itemOption = Items.REGISTRY.get(new Identifier("my:apple")); // vavr option
    itemOption.peek(item -> {
        // Process the item if it exists
    });
}
```

### 🔹 Grouping Items into Tags

```java
public class Tags {
    public static final Key FRUITS = new Identifier("example:fruits");

    static {
        ItemRegistry.ITEMS.addToTag(
            FRUITS,
            new Identifier("example:apple"),
            new Identifier("example:orange")
        );
    }
}

// Accessing items by tag
void main() {
    List<Item> fruits = ItemRegistry.ITEMS.getAllByTag(Tags.FRUITS);

    println("Fruits in the registry:");
    fruits.forEach(entry -> println(entry.getName())); // Process all items with tag FRUIT
}
```

### 🔹 Using Deferred Registration

```java
// Finalize and use the deferred registry
void main() {
    Register.finalizeRegistry();

    println("Registered deferred items:");

    println(Register.BANANA.getName()); // Output: Banana
    println(Register.GRAPE.getName());  // Output: Grape
}

public class Register {
    public static final DeferredRegistry<Item> DEFERRED_ITEMS = new DeferredRegistry<>("my", ItemRegistry.ITEMS);

    // Register items with deferred registration
    public static final Item BANANA = DEFERRED_ITEMS.register("banana", new Item("Banana"));
    public static final Item GRAPE = DEFERRED_ITEMS.register("grape", new Item("Grape"));

    public static void finalizeRegistry() {
        DEFERRED_ITEMS.registerAll();
    }
}
```

# 📜 License

This project is licensed under the [MIT License](./LICENSE). You are free to use, modify, and distribute the code as
long as proper attribution is given. 
