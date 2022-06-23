package bot.commands;

import bot.JDASingleton;
import bot.commands.abstracts.CommandContext;
import bot.commands.abstracts.CommandHandler;
import bot.services.StatsService;
import bot.dtos.PlayerDto;
import bot.dtos.StatsDto;
import bot.messages.stats.StatsMessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.logging.Logger;

public class StatsCommandHandler extends CommandHandler
{
    private final Logger logger = Logger.getLogger("command.stats");
    private final StatsService statsService;

    public StatsCommandHandler(StatsService statsService) {
        super(
            "Retrieves the stats profile for a player",
            0,
            "player"
        );
        this.statsService = statsService;
    }

    @Override
    protected void doCommand(CommandContext ctx) {
        MessageReceivedEvent event = ctx.getEvent();
        MessageChannel channel = event.getChannel();

        User user = event.getAuthor();
        String playerId = ctx.getParam("player");
        // check if player param is included
        if (playerId != null) {
            // if so, fetch player profile from discord
            user = JDASingleton.fetchUserFromDirect(playerId);
            if (user == null) {
                channel.sendMessage("Can't find a discord user with that id. Try using @ directly.").queue();
                return;
            }
        }

        PlayerDto player = new PlayerDto(user);

        StatsDto stats = statsService.getStats(player);

        new StatsMessageBuilder()
            .setStats(stats)
            .setAuthor(user)
            .sendMessage(channel);

        logger.info("Retrieved stats for " + stats.getPlayer());
    }
}
