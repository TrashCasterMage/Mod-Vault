## Vault Research

Vault Research is a mod that allows modpack makers to lock mods behind a progression system. Currently, that progression system is difficult for the modpack maker to change. There is a single next-level equation that dictates when a player "levels" up, granting them a research point which they can use to unlock mods.

## For Players

You get levels in Vault Research the same way you get levels in vanilla minecraft. When you have enough levels to unlock a new mod, you can spend your skill points to do so!

## For Modpack Developers

Currently, Vault Research is slightly lacking in configurability. Here is the list of what you can easily change in your modpack:

* Mods locks behind a single research
* Cost of a single research
* Aspects locked (i.e. crafting, right-click, etc)
* Individual item locking (can lock items behind a research rather than a whole mod)
* Prerequisite (You can have a Research remain hidden and un-researchable until they research its prerequisite)
* Mutually exclusive research (researching one makes others un-researchable)

Currently, there are some very basic things that you can't configure. These are coming in the near future, but are currently unavailable:
* Custom icons for research
* Gating research by amount researched (i.e. a Research that can only be researched once you've spent 30 research points)
* Multiple research tabs for organization
* Dimension-locking based on Research

For information on how to configure this mod, download the mod and go to the config folder. In there is a vault_research folder, and you can look at the following files to see examples on how to configure the mod:
* researches.json (defines cost of research, modIDs restricted, individual restrictions, and research name)
* researches_gui_styles.json (defines position on the screen, shape (rectangle or star), and icon (u and v))
* skill_descriptions.json (descriptions for each research)
* skill_gates.json (define prerequisites and mutual exclusivity)