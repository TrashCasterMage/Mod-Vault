package vault_research.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import vault_research.init.ModConfigs;

public class ReloadConfigsCommand extends Command {

    @Override
    public String getName() {
        return "reloadcfg";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(this::reloadConfigs);
    }

    private int reloadConfigs(CommandContext<CommandSource> context) {
        try { ModConfigs.register(); } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }

}
