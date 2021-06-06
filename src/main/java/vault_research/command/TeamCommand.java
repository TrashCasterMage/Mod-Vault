package vault_research.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import vault_research.research.InviteHandler;
import vault_research.research.ResearchTree;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class TeamCommand extends Command{

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
			sender.sendMessage(new StringTextComponent("Tried to invite yourself!"), null);
			return 0;
		}
		
		boolean inviteCreated = InviteHandler.invitePlayer(sender.getUniqueID(), receiver.getUniqueID());
		
		if (!inviteCreated) {
			sender.sendMessage(new StringTextComponent("Could not send invite. Player already has a pending invite!"), null);
		} else {
			sender.sendMessage(new StringTextComponent("Invite sent"), null);
			
			receiver.sendMessage(new StringTextComponent("You've received a team invite from " + sender.getScoreboardName() + ". Do \"/vault_research team accept\" to accept, or \"/vaultresearch team decline\" to decline"), sender.getUniqueID());
		}
		
		return 0;
	}
	
	private int acceptInvite(CommandContext<CommandSource> context) throws CommandSyntaxException {
		boolean success = InviteHandler.acceptInvite(context.getSource().asPlayer());
		if (success) {
			context.getSource().asPlayer().sendMessage(new StringTextComponent("Successfully joined team."), null);
		} else {
			context.getSource().asPlayer().sendMessage(new StringTextComponent("Failed to join team. Did your invite expire?"), null);
		}
		return 0;
	}
	
	private int declineInvite(CommandContext<CommandSource> context) throws CommandSyntaxException {
		boolean success = InviteHandler.declineInvite(context.getSource().asPlayer().getUniqueID());
		
		if(success) {
			context.getSource().asPlayer().sendMessage(new StringTextComponent("Declined team invite."), null);
		} else {
			context.getSource().asPlayer().sendMessage(new StringTextComponent("No invite available to decline. Did it expire?"), null);
		}
		return 0;
	}
	
	private int leaveTeam(CommandContext<CommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().asPlayer();
		
		boolean success = ResearchTree.leaveTeam(player);
		
		if (success) {
			player.sendMessage(new StringTextComponent("Successfully left your team!"), null);
		} else {
			player.sendMessage(new StringTextComponent("Failed to leave team: You're not on a team."), null);
		}
		return 0;
	}

	@Override
	public boolean isDedicatedServerOnly() {
		return false;
	}

}
