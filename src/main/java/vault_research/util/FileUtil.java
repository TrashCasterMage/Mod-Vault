package vault_research.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vault_research.Vault;

@OnlyIn(Dist.CLIENT)
public class FileUtil {
	
	public static void createFolder(String name) {
		try {
			Files.createDirectories(Paths.get(name));
		} catch (IOException e) {
			Vault.LOGGER.warn("Could not create new directory: {}", name);
		}
	}
}
