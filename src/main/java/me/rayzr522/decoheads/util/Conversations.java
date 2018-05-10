package me.rayzr522.decoheads.util;

import me.rayzr522.decoheads.DecoHeads;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Conversations {
    private static <T> void get(Player player, String prompt, Predicate<String> validator, Function<String, String> errorMessage, Function<String, T> mapper, Consumer<T> callback) {
        new ConversationFactory(DecoHeads.getInstance())
                .withFirstPrompt(new StringPrompt() {
                    @Override
                    public String getPromptText(ConversationContext conversationContext) {
                        return prompt;
                    }

                    @Override
                    public Prompt acceptInput(ConversationContext conversationContext, String query) {
                        if (!validator.test(query)) {
                            player.sendMessage(errorMessage.apply(query));
                            return this;
                        }

                        callback.accept(mapper.apply(query));
                        return null;
                    }
                })
                .withLocalEcho(true)
                .withModality(false)
                .buildConversation(player)
                .begin();
    }

    public static void getString(Player player, String prompt, Consumer<String> callback) {
        get(
                player,
                prompt,
                query -> true,
                query -> null,
                query -> query,
                callback
        );
    }

    public static void getDouble(Player player, String prompt, Consumer<Double> callback) {
        get(
                player,
                prompt,
                query -> {
                    try {
                        double v = Double.parseDouble(query);
                        return v == v; // essentially return true, but this way IntelliJ doesn't kill me
                    } catch (NumberFormatException e) {
                        return false;
                    }
                },
                query -> DecoHeads.getInstance().tr("command.not-a-decimal", query),
                Double::parseDouble,
                callback
        );
    }
}
