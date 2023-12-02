/*
 * Copyright (c) Joseph Prichard 2023.
 */

package messaging.senders;

import services.game.Game;

public class GameStartSender extends MessageSender {
    public GameStartSender setGame(Game game) {
        var desc = "Black: " + game.blackPlayer().getName() + "\n " +
            "White: " + game.whitePlayer().getName() + "\n " +
            "Use `/view` to view the game and use `/move` to make a move.";
        getEmbedBuilder().setTitle("Game started!").setDescription(desc);
        return this;
    }

    public GameStartSender setTag(Game game) {
        setMessage("<@" + game.blackPlayer() + ">" + " <@" + game.whitePlayer() + ">");
        return this;
    }
}
