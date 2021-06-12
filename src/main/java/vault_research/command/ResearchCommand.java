package vault_research.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.UUID;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import vault_research.init.ModConfigs;
import vault_research.research.type.Research;
import vault_research.world.data.PlayerResearchesData;

public class ResearchCommand extends Command {
	
	public static UUID nid = Util.DUMMY_UUID;

	@Override
	public String getName() {
		return "research";
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
				Commands.literal("grant")
					.then(Commands.argument("researchName", StringArgumentType.greedyString())
							.executes(this::addResearch))
				);
		
		builder.then(
				Commands.literal("revoke")
					.then(Commands.argument("researchName", StringArgumentType.greedyString())
							.executes(this::removeResearch))
				);
	}
	
	private int addResearch(CommandContext<CommandSource> context) throws CommandSyntaxException{
		ServerPlayerEntity player = context.getSource().asPlayer();
		String researchName = StringArgumentType.getString(context, "researchName");
		Research research = ModConfigs.RESEARCHES.getByName(researchName);
		
		if (research == null) {
			throw new CommandException(new StringTextComponent("Can't add that research: There is no research with that name"));
		}
		
		PlayerResearchesData.get(player.getServerWorld()).research(player, research);
		
		player.sendMessage(new StringTextComponent("Successfully unlocked research: " + researchName), nid);
		
		return 0;
	}
	
	private int removeResearch(CommandContext<CommandSource> context) throws CommandSyntaxException {
		String researchName = StringArgumentType.getString(context, "researchName");
		Research research = ModConfigs.RESEARCHES.getByName(researchName);
		
		if (research == null) {
			throw new CommandException(new StringTextComponent("Can't remove that research: There is no research with that name"));
		}
		
		ServerPlayerEntity player = context.getSource().asPlayer();
		
		boolean hadResearch = PlayerResearchesData.get(player.getServerWorld()).getResearches(player).removeResearchIfResearched(researchName);

		if (!hadResearch) throw new CommandException(new StringTextComponent("Couldn't remove that research: Player did not have that research"));
		
		PlayerResearchesData.get(player.getServerWorld()).getResearches(player).sync(player.server);
				
		player.sendMessage(new StringTextComponent("Successfully revoked research: " + researchName), nid);
		return 0;
	}

}
