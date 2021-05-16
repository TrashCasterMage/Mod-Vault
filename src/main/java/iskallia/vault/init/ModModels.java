package iskallia.vault.init;

import iskallia.vault.Vault;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ModModels {

    @SuppressWarnings({"unchecked"})
    public static void registerItemColors(ItemColors colors) {

    }

    private static class CustomRenderType extends RenderType {
        // TODO: Do dis, so Cryo Chamber renders correctly :c
        private static final RenderType INSTANCE = makeType("cutout_ignoring_normals",
                DefaultVertexFormats.BLOCK, 7, 131072,
                true, false,
                RenderType.State.getBuilder()
                        .shadeModel(SHADE_ENABLED)
                        .lightmap(LIGHTMAP_ENABLED)
                        .texture(BLOCK_SHEET)
                        .alpha(HALF_ALPHA)
                        .build(true)
        );

        public CustomRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
            super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
        }
    }

    public static class ItemProperty {

        public static void register() {

        }

        public static void registerItemProperty(Item item, String name, IItemPropertyGetter property) {
            ItemModelsProperties.registerProperty(item, Vault.id(name), property);
        }

    }

    public static class GearModel {
        public static Map<Integer, GearModel> REGISTRY;
        public static GearModel SCRAPPY;
        public static GearModel SAMURAI;
        public static GearModel KNIGHT;
        public static GearModel GUARD;
        public static GearModel DRAGON;
        public static GearModel PLATED_1;
        public static GearModel PLATED_1_DARK;
        public static GearModel PLATED_2;
        public static GearModel PLATED_2_DARK;
        public static GearModel PLATED_3;
        public static GearModel PLATED_3_DARK;
        public static GearModel PLATED_4;
        public static GearModel PLATED_4_DARK;

        public static void register() {
            REGISTRY = new HashMap<>();
        }

        String displayName;

        public String getDisplayName() {
            return displayName;
        }

        public String getTextureName(EquipmentSlotType slotType, String type) {
            String base = Vault.sId("textures/models/armor/" + this.displayName.toLowerCase().replace(" ", "_") + "_armor")
                    + (slotType == EquipmentSlotType.LEGS ? "_layer2" : "_layer1");
            return (type == null ? base : base + "_" + type) + ".png";
        }

    }

}
