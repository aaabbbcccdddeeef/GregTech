package gregtech.common.blocks.wood;

import gregtech.api.GregTechAPI;
import gregtech.api.items.toolitem.ToolClasses;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class BlockGregWoodSlab extends BlockSlab {

    private static final PropertyEnum<BlockGregPlanks.BlockType> VARIANT = PropertyEnum.create("variant", BlockGregPlanks.BlockType.class);

    public BlockGregWoodSlab() {
        super(Material.WOOD);
        setTranslationKey("wood_slab");
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.WOOD);
        setHarvestLevel(ToolClasses.AXE, 0);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_DECORATIONS);
        this.useNeighborBrightness = true;
    }

    @Nonnull
    @Override
    public IProperty<BlockGregPlanks.BlockType> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Nonnull
    @Override
    public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune) {
        return Item.getItemFromBlock(MetaBlocks.WOOD_SLAB);
    }

    @Nonnull
    @Override
    public String getTranslationKey(int meta) {
        return super.getTranslationKey() + "." + blockTypeFromMeta(meta).getName();
    }

    @Nonnull
    @Override
    public BlockGregPlanks.BlockType getTypeForItem(ItemStack stack) {
        return blockTypeFromMeta(stack.getMetadata());
    }

    @Override
    public void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        for (BlockGregPlanks.BlockType type : BlockGregPlanks.BlockType.values()) {
            items.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    private static BlockGregPlanks.BlockType blockTypeFromMeta(int meta) {
        return (meta & 1) != 0 ? BlockGregPlanks.BlockType.TREATED_PLANK : BlockGregPlanks.BlockType.RUBBER_PLANK;
    }

    public static class Half extends BlockGregWoodSlab {

        public Half() {
            this.setDefaultState(this.blockState.getBaseState()
                    .withProperty(HALF, EnumBlockHalf.BOTTOM)
                    .withProperty(VARIANT, BlockGregPlanks.BlockType.RUBBER_PLANK));
        }

        @Override
        public boolean isDouble() {
            return false;
        }

        @Nonnull
        @SuppressWarnings("deprecation")
        @Override
        public IBlockState getStateFromMeta(int meta) {
            return this.getDefaultState()
                    .withProperty(VARIANT, blockTypeFromMeta(meta))
                    .withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }

        @Override
        public int getMetaFromState(IBlockState state) {
            int i = state.getValue(VARIANT).ordinal();
            if (state.getValue(HALF) == EnumBlockHalf.TOP) {
                i |= 8;
            }
            return i;
        }

        @Nonnull
        @Override
        protected BlockStateContainer createBlockState() {
            return new BlockStateContainer(this, HALF, VARIANT);
        }

        @Override
        public boolean doesSideBlockChestOpening(@Nonnull IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
            return false;
        }
    }

    public static class Double extends BlockGregWoodSlab {

        public Double() {
            this.setDefaultState(this.blockState.getBaseState()
                    .withProperty(VARIANT, BlockGregPlanks.BlockType.RUBBER_PLANK));
        }

        @Override
        public boolean isDouble() {
            return true;
        }

        @Nonnull
        @SuppressWarnings("deprecation")
        @Override
        public IBlockState getStateFromMeta(int meta) {
            return this.getDefaultState().withProperty(VARIANT, blockTypeFromMeta(meta));
        }

        @Override
        public int getMetaFromState(IBlockState state) {
            return state.getValue(VARIANT).ordinal();
        }

        @Nonnull
        @Override
        protected BlockStateContainer createBlockState() {
            return new BlockStateContainer(this, VARIANT);
        }
    }
}
