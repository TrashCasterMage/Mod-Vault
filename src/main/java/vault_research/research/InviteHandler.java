package vault_research.research;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.ServerPlayerEntity;
import vault_research.world.data.PlayerResearchesData;
import vault_research.world.data.PlayerVaultStatsData;

public class InviteHandler {
	private static Map<UUID, Invite> inviteMap = new HashMap<>();
	
	private static class Invite {
		private UUID sender;
		private UUID receiver;
		
		public Invite(UUID sender, UUID receiver) {
			this.sender = sender;
			this.receiver = receiver;
			
			inviteMap.put(receiver, this);
		}

	}
	
	public static boolean getOrCreateInvite(UUID sender, UUID receiver) {
		if (inviteMap.containsKey(receiver)) return false; //Invite not created
		
		new Invite(sender, receiver);
		return true;
	}
	
	public static boolean invitePlayer(UUID sender, UUID receiver) {
		return getOrCreateInvite(sender, receiver);
	}
	
	public static boolean acceptInvite(ServerPlayerEntity receiver) {
		
		UUID receiverId = receiver.getUniqueID();
		
		if (!inviteMap.containsKey(receiverId)) return false;
		
		Invite invite = inviteMap.remove(receiverId);
		
		UUID teamId = ResearchTree.joinTeam(invite.sender, receiverId);
		
		if (teamId == null) return true;
		
		PlayerResearchesData.get(receiver.getServerWorld()).teamDeleted(teamId);
		PlayerVaultStatsData.get(receiver.getServerWorld()).teamDeleted(teamId);
		
		return true;
	}
	
	public static boolean declineInvite(UUID receiver) {
		if (!inviteMap.containsKey(receiver)) return false;
		
		inviteMap.remove(receiver);
		return true;
	}

}
