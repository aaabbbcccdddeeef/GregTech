package gregtech.common.pipelike.optical;

import gregtech.api.pipenet.block.IPipeType;

import javax.annotation.Nonnull;

public enum OpticalPipeType implements IPipeType<OpticalPipeProperties> {
    NORMAL;

    @Override
    public float getThickness() {
        return 0.375F;
    }

    @Override
    public OpticalPipeProperties modifyProperties(OpticalPipeProperties baseProperties) {
        return baseProperties;
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Nonnull
    @Override
    public String getName() {
        return "normal";
    }
}
