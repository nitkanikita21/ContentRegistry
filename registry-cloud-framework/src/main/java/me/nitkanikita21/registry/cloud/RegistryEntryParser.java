package me.nitkanikita21.registry.cloud;

import io.leangen.geantyref.TypeToken;
import io.vavr.collection.Iterator;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.nitkanikita21.registry.Identifier;
import me.nitkanikita21.registry.Registry;
import me.nitkanikita21.registry.RegistryEntry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistryEntryParser<C, T> implements ArgumentParser<C, RegistryEntry<T>>, SuggestionProvider<C> {
    final Registry<T> registry;
    final @NonNull ParserDescriptor<C, Identifier> identifierParser = IdentifierParser.identifierParser();


    public static <C, T> @NonNull ParserDescriptor<C, RegistryEntry<T>> registryEntryParser(Registry<T> registry) {
        return ParserDescriptor.of(new RegistryEntryParser<>(registry), new TypeToken<>() {});
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull RegistryEntry<T>> parse(@NonNull CommandContext<@NonNull C> commandContext, @NonNull CommandInput commandInput) {
        final String input = commandInput.peekString();
        return this.identifierParser.parser().parse(commandContext, commandInput).flatMapSuccess(id -> {
            Option<RegistryEntry<T>> optionEntry = registry.getEntry(id);

            return optionEntry.map(ArgumentParseResult::success)
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

    public static class RegistryEntryParseException extends ParserException {

        protected RegistryEntryParseException(@NonNull CommandContext<?> context, String input) {
            super(
                RegistryEntryParser.class,
                context,
                RegistryCloudFrameworkIntegration.Captions.ARGUMENT_PARSE_FAILURE_REGISTRY_ENTRY,
                CaptionVariable.of("input", input)
            );
        }
    }
}
