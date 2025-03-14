package me.nitkanikita21.registry.cloud;

import io.vavr.collection.Iterator;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.nitkanikita21.registry.Identifier;
import me.nitkanikita21.registry.Registry;
import me.nitkanikita21.registry.RegistryEntry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistryEntryIdentifierParser<C, T> implements ArgumentParser<C, Identifier>, SuggestionProvider<C> {
    final Registry<T> registry;
    final @NonNull ParserDescriptor<C, Identifier> identifierParser = IdentifierParser.identifierParser();

    public static <C, T> @NonNull ParserDescriptor<C, Identifier> registryEntryIdentifierParser(Registry<T> registry) {
        return ParserDescriptor.of(new RegistryEntryIdentifierParser<>(registry), Identifier.class);
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull Identifier> parse(@NonNull CommandContext<@NonNull C> commandContext, @NonNull CommandInput commandInput) {

        final String input = commandInput.peekString();
        return this.identifierParser.parser().parse(commandContext, commandInput).flatMapSuccess(id -> {
            Option<RegistryEntry<T>> optionEntry = registry.getEntry(id);

            return optionEntry.map(entry -> ArgumentParseResult.success(entry.getIdentifier()))
                .getOrElse(ArgumentParseResult.failure(new RegistryEntryParser.RegistryEntryParseException(commandContext, input)));
        });
    }

    @Override
    public @NonNull CompletableFuture<? extends @NonNull Iterable<? extends @NonNull Suggestion>> suggestionsFuture(
        final @NonNull CommandContext<C> commandContext,
        final @NonNull CommandInput commandInput
    ) {
        String input = commandInput.peekString();
        return CompletableFuture.completedFuture(Iterator.ofAll(registry.getAll())
            .filter(entry -> entry.getIdentifier().toString().contains(input))
            .map(e -> Suggestion.suggestion(e.getIdentifier().toString()))
        );
    }
}
