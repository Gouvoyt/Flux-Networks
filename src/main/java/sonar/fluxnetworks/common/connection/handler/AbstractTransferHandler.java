package sonar.fluxnetworks.common.connection.handler;

import net.minecraft.nbt.CompoundNBT;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.api.network.ITransferHandler;
import sonar.fluxnetworks.api.tiles.IFluxConnector;

public abstract class AbstractTransferHandler<C extends IFluxConnector>  implements ITransferHandler {

    public long buffer;

    public final C fluxConnector;

    protected long addedToBuffer;
    protected long removedFromBuffer;

    /**the actual energy transfer change - has no relation to the buffer's usage*/
    protected long change;

    public AbstractTransferHandler(C fluxConnector) {
        this.fluxConnector = fluxConnector;
    }

    public IFluxNetwork getNetwork() {
        return fluxConnector.getNetwork();
    }

    public void onStartCycle(){
        change = 0;
        addedToBuffer = 0;
        removedFromBuffer = 0;
    }

    public void onEndCycle(){
        //do
    }

    @Override
    public long addEnergyToBuffer(long energy, boolean simulate) {
        long add = getMaxAdd(energy);
        if(add > 0) {
            if(!simulate) {
                buffer += add;
                addedToBuffer += add;
            }
            return add;
        }
        return 0;
    }

    @Override
    public long removeEnergyFromBuffer(long energy, boolean simulate) {
        long remove = getMaxRemove(energy);
        if(remove > 0) {
            if(!simulate) {
                buffer -= remove;
                removedFromBuffer += remove;
            }
            return remove;
        }
        return 0;
    }
    @Override
    public long getBuffer() {
        return buffer;
    }

    @Override
    public long getRequest(){
        return 0;
    }

    @Override
    public long getChange() {
        return change;
    }

    public long getAddLimit(){
        return fluxConnector.getCurrentLimit();
    }

    public long getRemoveLimit(){
        return fluxConnector.getCurrentLimit();
    }

    public long getMaxAdd(long toAdd){
        return Math.max(Math.min(getAddLimit() - addedToBuffer, toAdd), 0);
    }

    public long getMaxRemove(long toRemove) {
        return Math.max(Math.min(getRemoveLimit() - removedFromBuffer, Math.min(toRemove, getBuffer())), 0);
    }

    public CompoundNBT writeNetworkedNBT(CompoundNBT tag) {
        tag.putLong("71", change);
        tag.putLong("72", buffer);
        return tag;
    }

    public void readNetworkedNBT(CompoundNBT tag) {
        change = tag.getLong("71");
        buffer = tag.getLong("72");
    }
}
