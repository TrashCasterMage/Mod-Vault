package vault_research.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.TextComponentMessageFormatHandler;
import vault_research.world.data.PlayerResearchesData;
import vault_research.world.data.PlayerVaultStatsData;

public class VaultLevelCommand extends Command {

    @Override
    public String getName() {
        return "vault_level";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                Commands.literal("add_exp")
                        .then(Commands.argument("exp", IntegerArgumentType.integer())
                                .executes(this::addExp))
        );

        builder.then(
                Commands.literal("set_level")
                        .then(Commands.argument("level", IntegerArgumentType.integer())
                                .executes(this::setLevel))
        );

        builder.then(
                Commands.literal("reset_all")
                        .executes(this::resetAll)
        );
        
        //builder.then(
        		//Commands.literal("count_spent_pts").executes(this::countSpentPoints));
    }

    private int setLevel(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int level = IntegerArgumentType.getInteger(context, "level");
        CommandSource source = context.getSource();
        PlayerVaultStatsData.get(source.getWorld()).setVaultLevel(source.asPlayer(), level);
        return 0;
    }

    private int addExp(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int exp = IntegerArgumentType.getInteger(context, "exp");
        CommandSource source = context.getSource();
        PlayerVaultStatsData.get(source.getWorld()).addVaultExp(source.asPlayer(), exp);
        return 0;
    }

    private int resetAll(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        PlayerVaultStatsData.get(source.getWorld()).reset(source.asPlayer());
        PlayerResearchesData.get(source.getWorld()).resetResearchTree(source.asPlayer());
        return 0;
    }
    
    private int countSpentPoints(CommandContext<CommandSource> context) throws CommandSyntaxException {
    	CommandSource source = context.getSource();
    	int spent = PlayerVaultStatsData.get(source.getWorld()).getSpentSkillPts(source.asPlayer());
    	source.asPlayer().sendMessage(new StringTextComponent(String.valueOf(spent)), null);
    	return 0;
    }

}
