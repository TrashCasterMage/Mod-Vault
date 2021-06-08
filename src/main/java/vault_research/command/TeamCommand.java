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
				
		ServerPlayerEntity receiver = EntityArgument.getPlayer(context, "target");
		ServerPlayerEntity sender = context.getSource().asPlayer();
				
		if (sender.getUniqueID().equals(receiver.getUniqueID())) {
			throw new CommandException(new StringTextComponent("Can't invite yourself to your own party."));
			//messageableSender.sendFeedback(new StringTextComponent("Tried to invite yourself!"), true);
			//return 0;
		}
		
		boolean inviteCreated = InviteHandler.invitePlayer(sender.getUniqueID(), receiver.getUniqueID());
		
		//ADD CHECK FOR IF PLAYERS ARE ALREADY SAME TEAM
		
		if (!inviteCreated) {			
			throw new CommandException(new StringTextComponent("Could not send invite: Player already has a pending invite. Ask them to decline their current invite."));
			//messageableSender.sendMessage(new StringTextComponent("Could not send invite. Player already has a pending invite!"), null);
		} else {
			sendMessage(sender, "Invite sent!");
			sendMessage(receiver, "You've received a team invite from " + sender.getScoreboardName() + ".\nDo \"/vault_research team accept\" to accept, or \"/vault_research team decline\" to decline");
			//sender.sendMessage(new StringTextComponent("Invite sent!"), nid);
			//receiver.sendMessage(new StringTextComponent("You've received a team invite from " + sender.getScoreboardName() + ". Do \"/vault_research team accept\" to accept, or \"/vaultresearch team decline\" to decline"), sender.getUniqueID());
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
			//context.getSource().asPlayer().sendMessage(new StringTextComponent("Successfully joined team."), null);
		} else {
			throw new CommandException(new StringTextComponent("Failed to join team: You have no pending invites."));
			//context.getSource().asPlayer().sendMessage(new StringTextComponent("Failed to join team. Did your invite expire?"), null);
		}
		return 0;
	}
	
	private int declineInvite(CommandContext<CommandSource> context) throws CommandSyntaxException {
		boolean success = InviteHandler.declineInvite(context.getSource().asPlayer().getUniqueID());
		
		if(success) {
			sendMessage(context.getSource().asPlayer(), "Declined team invite.");
			//context.getSource().asPlayer().sendMessage(new StringTextComponent("Declined team invite."), null);
		} else {
			throw new CommandException(new StringTextComponent("Failed to decline invite: You have no pending invites."));
			//context.getSource().asPlayer().sendMessage(new StringTextComponent("No invite available to decline. Did it expire?"), null);
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
			//player.sendMessage(new StringTextComponent("Successfully left your team!"), null);
		} else {
			throw new CommandException(new StringTextComponent("Failed to leave team: You're the only one on your team."));
			//player.sendMessage(new StringTextComponent("Failed to leave team: You're not on a team."), null);
		}
		return 0;
	}

	@Override
	public boolean isDedicatedServerOnly() {
		return false;
	}

}
