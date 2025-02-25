package gregtech.common.metatileentities.steam;

import gregtech.api.GTValues;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ProgressWidget.MoveType;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SteamMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;

public class SteamAlloySmelter extends SteamMetaTileEntity {

    public SteamAlloySmelter(ResourceLocation metaTileEntityId, boolean isHighPressure) {
        super(metaTileEntityId, RecipeMaps.ALLOY_SMELTER_RECIPES, Textures.ALLOY_SMELTER_OVERLAY, isHighPressure);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new SteamAlloySmelter(metaTileEntityId, isHighPressure);
    }

    @Override
    protected boolean isBrickedCasing() {
        return true;
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        return new NotifiableItemStackHandler(this, 2, this, false);
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        return new NotifiableItemStackHandler(this, 1, this, true);
    }

    @Override
    public ModularUI createUI(EntityPlayer player) {
        return createUITemplate(player)
                .slot(this.importItems, 0, 53, 25, GuiTextures.SLOT_STEAM.get(isHighPressure), GuiTextures.FURNACE_OVERLAY_STEAM.get(isHighPressure))
                .slot(this.importItems, 1, 35, 25, GuiTextures.SLOT_STEAM.get(isHighPressure), GuiTextures.FURNACE_OVERLAY_STEAM.get(isHighPressure))
                .progressBar(workableHandler::getProgressPercent, 79, 26, 20, 16,
                        GuiTextures.PROGRESS_BAR_ARROW_STEAM.get(isHighPressure), MoveType.HORIZONTAL, workableHandler.getRecipeMap())
                .slot(this.exportItems, 0, 107, 25, true, false, GuiTextures.SLOT_STEAM.get(isHighPressure))
                .build(getHolder(), player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void randomDisplayTick(float x, float y, float z, EnumParticleTypes flame, EnumParticleTypes smoke) {
        super.randomDisplayTick(x, y, z, flame, smoke);
        if (GTValues.RNG.nextBoolean()) {
            getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y + 0.5F, z, 0, 0, 0);
        }
    }
}
