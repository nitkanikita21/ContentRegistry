package me.nitkanikita21.registry.cloud;

import io.leangen.geantyref.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.nitkanikita21.registry.Identifier;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.bukkit.BukkitCaptionKeys;
import org.incendo.cloud.caption.Caption;
import org.incendo.cloud.caption.CaptionVariable;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.exception.parsing.ParserException;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

public final class IdentifierParser<C> implements ArgumentParser<C, Identifier>,
        BlockingSuggestionProvider.Strings<C> {

    public static <C> @NonNull ParserDescriptor<C, Identifier> identifierParser() {
        return ParserDescriptor.of(
                new IdentifierParser<>(),
                Identifier.class
        );
    }

    public static <C> CommandComponent.@NonNull Builder<C, Identifier> identifierComponent() {
        return CommandComponent.<C, Identifier>builder().parser(identifierParser());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NonNull ArgumentParseResult<Identifier> parse(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput commandInput
    ) {
        final String input = commandInput.peekString();
        final String[] split = input.split(":");
        final int maxSemi = split.length > 1 ? 1 : 0;
        if (input.length() - input.replace(":", "").length() > maxSemi) {
            // Wrong number of ':'
            return ArgumentParseResult.failure(new IdentifierParseException(
                    BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_NAMESPACED_KEY_KEY, input, commandContext
            ));
        }
        try {
            final Identifier ret;
            if (split.length == 2) {
                ret = new Identifier(commandInput.readUntilAndSkip(':'), commandInput.readString());
            }  else {
                // Too many parts, ie not:valid:input
                return ArgumentParseResult.failure(new IdentifierParseException(
                        BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_NAMESPACED_KEY_KEY, input, commandContext
                ));
            }

            // Success!
            return ArgumentParseResult.success(ret);
        } catch (final IllegalArgumentException ex) {
            final Caption caption = ex.getMessage().contains("namespace") // stupid but works
                    ? BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_NAMESPACED_KEY_NAMESPACE
                    // this will also get used if someone puts >256 chars
                    : BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_NAMESPACED_KEY_KEY;
            return ArgumentParseResult.failure(new IdentifierParseException(
                    caption,
                    input,
                    commandContext
            ));
        }
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(
            final @NonNull CommandContext<C> commandContext,
            final @NonNull CommandInput input
    ) {
        final List<String> ret = new ArrayList<>();

        final String token = input.peekString();
        if (!token.contains(":") && !token.isEmpty()) {
            ret.add(token + ":");
        }

        return ret;
    }

    static <C> void registerParserSupplier(final @NonNull CommandManager<C> commandManager) {
        commandManager.parserRegistry()
                .registerParserSupplier(TypeToken.get(Identifier.class), params -> new IdentifierParser<>());
    }


    public static final class IdentifierParseException extends ParserException {

        private final String input;

        public IdentifierParseException(
                final @NonNull Caption caption,
                final @NonNull String input,
                final @NonNull CommandContext<?> context
        ) {
            super(
                    IdentifierParser.class,
                    context,
                    caption,
                    CaptionVariable.of("input", input)
            );
            this.input = input;
        }

        public @NonNull String input() {
            return this.input;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final IdentifierParseException that = (IdentifierParseException) o;
            return this.input.equals(that.input) && this.errorCaption().equals(that.errorCaption());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.input, this.errorCaption());
        }
    }
}
