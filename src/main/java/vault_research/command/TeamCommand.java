package vault_research.command;

import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.server.command.TextComponentHelper;
import vault_research.research.InviteHandler;
import vault_research.research.ResearchTree;
import vault_research.world.data.PlayerResearchesData;
import vault_research.world.data.PlayerVaultStatsData;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.Color;
import vault_research.config.MiscConfig;

public class TeamCommand extends Command {

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

		builder.then(Commands.literal("invite")
				.then(Commands.argument("target", EntityArgument.player()).executes(this::invitePlayer)));

		builder.then(Commands.literal("accept").executes(this::acceptInvite));

		builder.then(Commands.literal("decline").executes(this::declineInvite));

		builder.then(Commands.literal("leave").executes(this::leaveTeam));

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
			throw new CommandException(
					new StringTextComponent("Could not send invite: Player is already on your team."));
		}

		boolean inviteCreated = InviteHandler.invitePlayer(senderID, receiverID);

		if (!inviteCreated) {
			throw new CommandException(new StringTextComponent(
					"Could not send invite: Player already has a pending invite. Ask them to decline their current invite."));
		} else {
			sendMessage(sender, "Invite sent!");

			
			// Make a nice, beautiful message
			StringTextComponent acceptButton = (StringTextComponent) new StringTextComponent("[Accept]");
			Style acceptStyle = Style.EMPTY.setColor(Color.fromHex("#2DED5B")).setBold(true)
					.setHoverEvent(
							new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Accept team invite")))
					.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vault_research team accept"));
			acceptButton.setStyle(acceptStyle);

			StringTextComponent declineButton = (StringTextComponent) new StringTextComponent("[Decline]");
			Style declineStyle = Style.EMPTY.setColor(Color.fromHex("#EC1919")).setBold(true)
					.setHoverEvent(
							new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Decline team invite")))
					.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vault_research team decline"));
			declineButton.setStyle(declineStyle);

			String borderString = "============================================";
			String inviteLine = "You've received a team invite from " + sender.getScoreboardName() + ".";

			receiver.sendMessage(new TranslationTextComponent("chat.message.team.invite", borderString, inviteLine,
					acceptButton, declineButton, borderString), nid);


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

		if (success) {
			sendMessage(context.getSource().asPlayer(), "Declined team invite.");
		} else {
			throw new CommandException(
					new StringTextComponent("Failed to decline invite: You have no pending invites."));
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
			throw new CommandException(
					new StringTextComponent("Failed to leave team: You're the only one on your team."));
		}
		return 0;
	}

	@Override
	public boolean isDedicatedServerOnly() {
		return false;
	}

}
