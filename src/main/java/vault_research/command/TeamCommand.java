package vault_research.command;

import java.util.UUID;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import vault_research.research.InviteHandler;
import vault_research.research.ResearchTree;
import vault_research.world.data.PlayerResearchesData;
import vault_research.world.data.PlayerVaultStatsData;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import vault_research.config.MiscConfig;

public class TeamCommand extends Command{
	
	public static UUID nid = Util.DUMMY_UUID;

	@Override
	public String getName() {
		return "team";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {

		builder.then(
			Commands.literal("invite")
				.then(Commands.argument("target", EntityArgument.player())
					.executes(this::invitePlayer))
		);
		
		builder.then(
			Commands.literal("accept")
				.executes(this::acceptInvite)
		);
		
		builder.then(
				Commands.literal("decline")
					.executes(this::declineInvite)
		);
		
		builder.then(
				Commands.literal("leave")
					.executes(this::leaveTeam)
		);
				
	}
	
	private int invitePlayer(CommandContext<CommandSource> context) throws CommandSyntaxException {
				
		if (!MiscConfig.teamsEnabled) {
			throw new CommandException(new StringTextComponent("Teams have been disabled."));
		}
		
		ServerPlayerEntity receiver = EntityArgument.getPlayer(context, "target");
		ServerPlayerEntity sender = context.getSource().asPlayer();
		
		UUID senderID = sender.getUniqueID();
		UUID receiverID = receiver.getUniqueID();
						
		if (senderID.equals(receiverID)) {
			throw new CommandException(new StringTextComponent("Can't invite yourself to your own party."));
		}
		
		if (ResearchTree.onSameTeam(senderID, receiverID)) {
			throw new CommandException(new StringTextComponent("Could not send invite: Player is already on your team."));
		}
		
		boolean inviteCreated = InviteHandler.invitePlayer(senderID, receiverID);
		
		//ADD CHECK FOR IF PLAYERS ARE ALREADY SAME TEAM
		
		if (!inviteCreated) {			
			throw new CommandException(new StringTextComponent("Could not send invite: Player already has a pending invite. Ask them to decline their current invite."));
		} else {
			sendMessage(sender, "Invite sent!");
			sendMessage(receiver, "============================================" +
					"\nYou've received a team invite from " + sender.getScoreboardName() + "." +
					"\n  Do \"/vault_research team accept\" to accept, or \n  \"/vault_research team decline\" to decline" +
					"\n============================================");
		}
		
		return 0;
	}
	
	private void sendMessage(PlayerEntity player, String msg) {
		player.sendMessage(new StringTextComponent(msg), nid);
	}
	
	private int acceptInvite(CommandContext<CommandSource> context) throws CommandSyntaxException {
		boolean success = InviteHandler.acceptInvite(context.getSource().asPlayer());
		if (success) {
			sendMessage(context.getSource().asPlayer(), "Successfully joined team.");
		} else {
			throw new CommandException(new StringTextComponent("Failed to join team: You have no pending invites."));
		}
		return 0;
	}
	
	private int declineInvite(CommandContext<CommandSource> context) throws CommandSyntaxException {
		boolean success = InviteHandler.declineInvite(context.getSource().asPlayer().getUniqueID());
		
		if(success) {
			sendMessage(context.getSource().asPlayer(), "Declined team invite.");
		} else {
			throw new CommandException(new StringTextComponent("Failed to decline invite: You have no pending invites."));
		}
		return 0;
	}
	
	private int leaveTeam(CommandContext<CommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().asPlayer();
		
		boolean success = ResearchTree.leaveTeam(player);
		
		if (success) {
			PlayerResearchesData researches = PlayerResearchesData.get(player.getServerWorld());
			PlayerVaultStatsData vaultStats = PlayerVaultStatsData.get(player.getServerWorld());

			researches.getResearches(player).sync(player.getServer());
			vaultStats.getVaultStats(player).sync(player.getServer());
			
			researches.markDirty();
			vaultStats.markDirty();
			
			sendMessage(player, "Successfully left your team!");
		} else {
			throw new CommandException(new StringTextComponent("Failed to leave team: You're the only one on your team."));
		}
		return 0;
	}

	@Override
	public boolean isDedicatedServerOnly() {
		return false;
	}

}
