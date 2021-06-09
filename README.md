## Vault Research

Vault Research is a mod that allows modpack makers to lock mods behind a progression system.

## For Players

You get levels in Vault Research the same way you get levels in vanilla minecraft. When you have enough levels to unlock a new mod, you can spend your skill points to do so!

## For Modpack Developers

Currently, Vault Research has what I'd consider to be all the basic configurability. Here is the list of what you can easily change in your modpack:

* Mods locked behind a single research
* Cost of a single research
* Aspects locked (i.e. crafting, right-click, etc)
* Individual item locking (can lock items behind a research rather than a whole mod)
* Prerequisite (You can have a Research remain hidden and un-researchable until they research its prerequisite)
* Mutually exclusive research (researching one makes others un-researchable)
* Custom icons for research (16x16)
* Gating research by amount researched (i.e. a Research that can only be researched once you've spent 30 research points)
* Locking dimensions behind research.
* Teams System
* Change all visible text from the mod to flavor it as you wish!

There are, however, some things that you can't configure. These are coming in the (hopefully) near future, but are currently unavailable:
* Multiple research tabs for organization

At the moment, there isn't a wiki or other guide on how to configure this mod. For information on how to configure this mod, download the mod and go to the config folder. In there is a vault_research folder, and you can look at the following files to see examples on how to configure the mod:
* researches.json (defines cost of research, modIDs restricted, individual restrictions, and research name)
* researches_gui_styles.json (defines position on the screen, shape (rectangle or star), and icon (texture))
* skill_descriptions.json (descriptions for each research)
* skill_gates.json (define prerequisites and mutual exclusivity)
* For the resource pack, you'll want the following directory structure: assets/vault_research/textures/gui/research_icons/ In this research_icons folder you can put your textures as png files. This should follow what you put for the texture in researches_gui_styles.json. So if you have a research and you put "pizza" as the texture entry in the json, your icon should be "pizza.png."

Huge thanks to the Iskallia dev team for creating the "Vault Hunters" mod, of which this mod is itself a modification. And also thanks for releasing the mod under the GNU General Public License, so that I could release this for modpack makers to use!

[Original Repo Here]((https://github.com/Iskallia/Vault-public))
