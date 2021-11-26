package gregtech.common.items.behaviors;

import gregtech.api.cover.CoverDefinition;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.common.metatileentities.multi.electric.centralmonitor.MetaTileEntityCentralMonitor;
import gregtech.common.render.WorldRenderEventRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class CoverDigitalInterfaceWirelessPlaceBehaviour extends CoverPlaceBehavior {
    public CoverDigitalInterfaceWirelessPlaceBehaviour(CoverDefinition coverDefinition) {
        super(coverDefinition);
    }

    public static BlockPos getRemotePos(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag != null) {
            return NBTUtil.getPosFromTag(tag);
        }
        return null;
    }

    @Override
    public void onUpdate(ItemStack itemStack, Entity entity) {
        if (entity.world.isRemote && entity instanceof EntityPlayer) {
            ItemStack held = ((EntityPlayer) entity).getHeldItemMainhand();
            if (held == itemStack) {
                BlockPos pos = getRemotePos(itemStack);
                if (pos != null) {
                    WorldRenderEventRenderer.renderBlockBoxHighLight(pos, 1500);
                }
            }
        }
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof MetaTileEntityHolder && ((MetaTileEntityHolder) tileEntity).getMetaTileEntity() instanceof MetaTileEntityCentralMonitor) {
            ItemStack itemStack = player.getHeldItem(hand);
            itemStack.setTagCompound(NBTUtil.createPosTag(pos));
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            player.getHeldItem(hand).setTagCompound(null);
            return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack itemStack, List<String> lines) {
        BlockPos pos = getRemotePos(itemStack);
        String binding = pos == null ? "---" : String.format("%d, %d, %d", pos.getX(), pos.getY(), pos.getZ());
        lines.add(I18n.format("metaitem.cover.digital.wireless.tooltip.1"));
        lines.add(I18n.format("metaitem.cover.digital.wireless.tooltip.2"));
        lines.add(I18n.format("metaitem.cover.digital.wireless.tooltip.3", binding));
    }
}