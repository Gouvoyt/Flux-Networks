package sonar.fluxnetworks.common.connection.handler;

import net.minecraft.util.Direction;
import sonar.fluxnetworks.api.network.IFluxTransfer;
import sonar.fluxnetworks.common.connection.transfer.SidedTransfers;
import sonar.fluxnetworks.common.tileentity.TileFluxPoint;

import java.util.Objects;

public class FluxPointHandler extends AbstractPointHandler<TileFluxPoint> {

    public SidedTransfers transfers;

    public FluxPointHandler(TileFluxPoint fluxPoint) {
        super(fluxPoint);
        this.transfers = new SidedTransfers(fluxPoint);
    }

    @Override
    public void onStartCycle() {
        super.onStartCycle();
        transfers.getTransfers().stream().filter(Objects::nonNull).forEach(IFluxTransfer::onStartCycle);

    }

    @Override
    public void onEndCycle() {
        super.onEndCycle();
        transfers.getTransfers().stream().filter(Objects::nonNull).forEach(IFluxTransfer::onEndCycle);
    }

    @Override
    public long removeEnergy(long energy, boolean simulate) {
        if(!fluxConnector.isActive()) {
            return 0;
        }
        long remove = 0;
        for(IFluxTransfer transfer : transfers.getTransfers()) {
            if(transfer != null) {
                long toTransfer = energy - remove;
                remove += transfer.addEnergy(toTransfer, simulate);
            }
        }
        if(!simulate) {
            removedFromBuffer += remove;
        }
        return remove;
    }

    @Override
    public void updateTransfers(Direction... faces) {
        transfers.updateTransfers(faces);
    }

}
