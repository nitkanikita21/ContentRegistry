# 🗃️ ContentRegistry

**ContentRegistry** is a lightweight and extensible library for using registry system. Inspired by systems like
Minecraft's `Registry`, it provides a flexible API for registering, organizing, and retrieving content. The library
supports features such as lazy-loaded registry entries, deferred registration, tagging, and dynamic registries.

🙂 I appreciate constructive feedback and suggestions for improving the project. Feel free to open an issue or submit a
pull request with your ideas.

---

## 🧩 Features

- [x] **Registry Management**: Create and manage registries for any type of content.
- [x] **Lazy Entries**: Access registry entries only when needed to optimize performance.
- [x] **Deferred Registration**: Register content at a later stage in the lifecycle.
- [x] **Tagging System**: Categorize and group content using a tag-based system.
- [x] **Dynamic Registries**: Support for registries that can be modified at runtime.
- [ ] **Deferred Content Registration from Configuration** (WIP): Register content that is loaded dynamically from
  configuration files (e.g., JSON, YAML).

## ✨ Basic Examples:

### Defining a Registry for Items

```java
// Define a registry for items
public class Items {
    public static final Registry<Item> REGISTRY = Registry.create(Key.key("my:items"));

    // Register items statically
    public static final Item APPLE = register("apple", new Item("Apple"));
    public static final Item ORANGE = register("orange", new Item("Orange"));

    private static Item register(String path, Item item) {
        REGISTRY.register(Key.key("my", path), item);
        return item;
    }
}
```

### Accessing Items from the Registry

```java
Option<Item> itemOption = Items.REGISTRY.get(Key.key("my:apple")); // vavr option
if(!itemOption.isEmpty()){
    // If the item is found in the registry
}
```

### Grouping Items into Tags

```java
public class Tags {
    public static final Key FRUITS = Key.key("example", "fruits");

    static {
        ItemRegistry.ITEMS.addToTag(
            FRUITS,
            Key.key("example", "apple"),
            Key.key("example", "orange")
        );
    }
}

// Accessing items by tag
List<Item> fruits = ItemRegistry.ITEMS.getAllByTag(Tags.FRUITS);

println("Fruits in the registry:");
fruits.forEach(entry -> println(entry.getName()));
```

### Using Deferred Registration

```java
public class Register {
    public static final DeferredRegistry<Item> DEFERRED_ITEMS = new DeferredRegistry<>("my", ItemRegistry.ITEMS);

    // Register items with deferred registration
    public static final Item BANANA = DEFERRED_ITEMS.register("banana", new Item("Banana"));
    public static final Item GRAPE = DEFERRED_ITEMS.register("grape", new Item("Grape"));

    public static void finalizeRegistry() {
        DEFERRED_ITEMS.registerAll();
    }
}

// Finalize and use the deferred registry
Register.finalizeRegistry();

println("Registered deferred items:");

println(Register.BANANA.getName()); // Output: Banana

println(Register.GRAPE.getName());  // Output: Grape
```

# License

This project is licensed under the [MIT License](./LICENSE). You are free to use, modify, and distribute the code as
long as proper attribution is given. 
