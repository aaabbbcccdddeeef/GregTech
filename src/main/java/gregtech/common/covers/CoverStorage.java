package gregtech.common.covers;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.cover.CoverBase;
import gregtech.api.cover.CoverDefinition;
import gregtech.api.cover.CoverWithUI;
import gregtech.api.cover.CoverableView;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class CoverStorage extends CoverBase implements CoverWithUI {

    private final ItemStackHandler storageHandler = new ItemStackHandler(9);
    private static final int MAX_WIDTH = 176;
    private static final int MAX_HEIGHT = 126;
    private static final int SLOT_SIZE = 18;

    public CoverStorage(@NotNull CoverDefinition definition, @NotNull CoverableView coverableView, @NotNull EnumFacing attachedSide) {
        super(definition, coverableView, attachedSide);
    }

    @Override
    public boolean canAttach(@NotNull CoverableView coverable, @NotNull EnumFacing side) {
        return true;
    }

    @Override
    public void renderCover(@NotNull CCRenderState renderState, @NotNull Matrix4 translation, IVertexOperation[] pipeline, @NotNull Cuboid6 plateBox, @NotNull BlockRenderLayer layer) {
        Textures.STORAGE.renderSided(getAttachedSide(), plateBox, renderState, pipeline, translation);
    }

    @Override
    public void onRemoval() {
        dropInventoryContents(storageHandler);
    }

    @Override
    public @NotNull EnumActionResult onRightClick(@NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull CuboidRayTraceResult hitResult) {
        if (!getCoverableView().getWorld().isRemote) {
            openUI((EntityPlayerMP) player);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public @NotNull EnumActionResult onScrewdriverClick(@NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull CuboidRayTraceResult hitResult) {
        if (!getWorld().isRemote) {
            openUI((EntityPlayerMP) player);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public ModularUI createUI(EntityPlayer player) {
        ModularUI.Builder builder = new ModularUI.Builder(GuiTextures.BACKGROUND, MAX_WIDTH, MAX_HEIGHT);
        builder.label(5, 5, "cover.storage.title");
        for (int index = 0; index < storageHandler.getSlots(); index++) {
            builder.slot(storageHandler, index, (index * SLOT_SIZE) + 7, (MAX_HEIGHT - SLOT_SIZE * 5) / 2, true, true, GuiTextures.SLOT);
        }

        builder.bindPlayerInventory(player.inventory, (MAX_HEIGHT - SLOT_SIZE * 2) / 2 - 1);

        return builder.build(this, player);
    }

    /**
     * @deprecated Only exists for compatibility with the crafting table cover and will be removed in the future.
     * Do not depend on this method.
     */
    @ApiStatus.ScheduledForRemoval(inVersion = "2.9")
    @ApiStatus.Internal
    @Deprecated
    public @NotNull IItemHandler getStorageHandler() {
        return this.storageHandler;
    }

    @Override
    public void writeToNBT(@NotNull NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setTag("Storage", this.storageHandler.serializeNBT());
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.storageHandler.deserializeNBT(tagCompound.getCompoundTag("Storage"));
    }
}
