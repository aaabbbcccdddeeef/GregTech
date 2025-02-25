package gregtech.common.blocks;

import gregtech.api.GregTechAPI;
import gregtech.api.items.toolitem.ToolClasses;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconType;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.client.model.MaterialStateMapper;
import gregtech.client.model.modelfactories.MaterialBlockModelLoader;
import gregtech.common.ConfigHolder;
import gregtech.common.blocks.properties.PropertyMaterial;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockCompressed extends BlockMaterialBase {

    public static BlockCompressed create(Material[] materials) {
        PropertyMaterial property = PropertyMaterial.create("variant", materials);
        return new BlockCompressed() {
            @Nonnull
            @Override
            public PropertyMaterial getVariantProperty() {
                return property;
            }
        };
    }

    private BlockCompressed() {
        super(net.minecraft.block.material.Material.IRON);
        setTranslationKey("compressed");
        setHardness(5.0f);
        setResistance(10.0f);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public net.minecraft.block.material.Material getMaterial(IBlockState state) {
        Material material = getGtMaterial(state);
        if (material.hasProperty(PropertyKey.GEM)) {
            return net.minecraft.block.material.Material.ROCK;
        } else if (material.hasProperty(PropertyKey.INGOT)) {
            return net.minecraft.block.material.Material.IRON;
        } else if (material.hasProperty(PropertyKey.DUST)) {
            return net.minecraft.block.material.Material.SAND;
        }
        return net.minecraft.block.material.Material.ROCK;
    }

    @Nonnull
    @Override
    public SoundType getSoundType(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nullable Entity entity) {
        Material material = getGtMaterial(state);
        if (material.hasProperty(PropertyKey.GEM)) {
            return SoundType.STONE;
        } else if (material.hasProperty(PropertyKey.INGOT)) {
            return SoundType.METAL;
        } else if (material.hasProperty(PropertyKey.DUST)) {
            return SoundType.SAND;
        }
        return SoundType.STONE;
    }

    @Override
    public String getHarvestTool(IBlockState state) {
        Material material = getGtMaterial(state);
        if (material.isSolid()) {
            return ToolClasses.PICKAXE;
        } else if (material.hasProperty(PropertyKey.DUST)) {
            return ToolClasses.SHOVEL;
        }
        return ToolClasses.PICKAXE;
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        Material material = getGtMaterial(state);
        if (material.hasProperty(PropertyKey.DUST)) {
            return material.getBlockHarvestLevel();
        }
        return 0;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (ConfigHolder.misc.debug) {
            tooltip.add("MetaItem Id: block" + getGtMaterial(stack).toCamelCaseString());
        }
    }

    @SideOnly(Side.CLIENT)
    public void onModelRegister() {
        ModelLoader.setCustomStateMapper(this, new MaterialStateMapper(
                MaterialIconType.block, s -> getGtMaterial(s).getMaterialIconSet()));
        for (IBlockState state : this.getBlockState().getValidStates()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), this.getMetaFromState(state),
                    MaterialBlockModelLoader.registerItemModel(
                            MaterialIconType.block,
                            getGtMaterial(state).getMaterialIconSet()));
        }
    }
}
