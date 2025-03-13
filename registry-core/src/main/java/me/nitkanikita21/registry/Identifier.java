package me.nitkanikita21.registry;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

@Data
@RequiredArgsConstructor
public class Identifier implements Identifiable {
    private static Pattern pattern = Pattern.compile("[^a-z0-9_.-]");

    private final @NonNull @NotNull String namespace;
    private final @NonNull @NotNull String path;

    public Identifier(@NonNull @NotNull String string) {
        String[] split = string.split(":");

        if(pattern.matcher(split[0]).find() || pattern.matcher(split[1]).find()) {
            throw new RuntimeException("Invalid identifier format");
        }

        this.namespace = split[0];
        this.path = split[1];
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }


    @Override
    public @NotNull Identifier getIdentifier() {
        return this;
    }
}
