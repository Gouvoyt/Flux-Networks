package sonar.fluxnetworks.common.connection.transfer;

import com.google.common.collect.Lists;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import sonar.fluxnetworks.api.energy.ITileEnergyHandler;
import sonar.fluxnetworks.api.network.IFluxTransfer;
import sonar.fluxnetworks.common.handler.TileEntityHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SidedTransfers {

    protected final TileEntity host;
    protected Map<Direction, IFluxTransfer> transfers;

    public SidedTransfers(TileEntity host){
        this.host = host;
        this.transfers = new HashMap<>();
        for(Direction facing : Direction.values()) {
            transfers.put(facing, null);
        }
    }

    public List<IFluxTransfer> getTransfers() {
        return Lists.newArrayList(transfers.values());
    }

    public IFluxTransfer getTransfer(Direction dir){
        return transfers.get(dir);
    }

    public void updateTransfers(Direction... faces) {
        for(Direction dir : faces) {
            TileEntity tile = host.getWorld().getTileEntity(host.getPos().offset(dir));
            IFluxTransfer transfer = transfers.get(dir);
            ITileEnergyHandler handler;

            if(tile == null || (handler = TileEntityHandler.getEnergyHandler(tile, dir.getOpposite())) == null) {
                transfers.put(dir, null);
            } else if (transfer == null || transfer.getTile() != tile) {
                transfers.put(dir, new ConnectionTransfer(handler, tile, dir));
            } else if (transfer.isInvalid()) {
                transfers.put(dir, null);
            }
        }
    }
}
