package me.nitkanikita21.registry;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Identifier implements Identifiable {
    private final String namespace;
    private final String path;

    public Identifier(String string) {
        String[] split = string.split(":");
        this.namespace = split[0];
        this.path = split[1];
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }


    @Override
    public Identifier getId() {
        return this;
    }
}
