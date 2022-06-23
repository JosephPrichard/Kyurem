package bot.commands;

import bot.commands.abstracts.CommandContext;
import bot.commands.abstracts.CommandHandler;
import bot.dtos.GameDto;
import bot.services.GameService;
import bot.dtos.PlayerDto;
import bot.messages.game.GameViewMessageBuilder;
import bot.imagerenderers.OthelloBoardRenderer;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import othello.board.OthelloBoard;
import othello.board.Tile;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Logger;

public class ViewCommandHandler extends CommandHandler
{
    private final Logger logger = Logger.getLogger("command.view");
    private final GameService gameService;
    private final OthelloBoardRenderer boardRenderer;

    public ViewCommandHandler(GameService gameService, OthelloBoardRenderer boardRenderer) {
        super("Displays the game state including all the moves that can be made this turn");
        this.gameService = gameService;
        this.boardRenderer = boardRenderer;
    }

    @Override
    public void doCommand(CommandContext ctx) {
        MessageReceivedEvent event = ctx.getEvent();
        MessageChannel channel = event.getChannel();

        PlayerDto player = new PlayerDto(event.getAuthor());

        GameDto game = gameService.getGame(player);
        if (game == null) {
            channel.sendMessage("You're not currently in a game.").queue();
            return;
        }

        OthelloBoard board = game.getBoard();
        List<Tile> potentialMoves = board.findPotentialMoves();

        BufferedImage image = boardRenderer.drawBoard(board, potentialMoves);
        new GameViewMessageBuilder()
            .setGame(game)
            .setImage(image)
            .sendMessage(channel);

        logger.info("Player " + player + " view moves in game");
    }
}
