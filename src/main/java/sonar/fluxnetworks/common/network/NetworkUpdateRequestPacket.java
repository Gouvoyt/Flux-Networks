package sonar.fluxnetworks.common.network;

import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import sonar.fluxnetworks.api.network.IFluxNetwork;
import sonar.fluxnetworks.common.connection.FluxNetworkCache;
import sonar.fluxnetworks.api.utils.NBTType;

import java.util.List;

public class NetworkUpdateRequestPacket extends AbstractPacket {

    public List<Integer> networkIDs = Lists.newArrayList();
    public NBTType type;

    public NetworkUpdateRequestPacket(int networkID, NBTType type) {
        this.networkIDs.add(networkID);
        this.type = type;
    }

    public NetworkUpdateRequestPacket(List<IFluxNetwork> networks, NBTType type) {
        networks.forEach(n -> this.networkIDs.add(n.getNetworkID()));
        this.type = type;
    }

    public NetworkUpdateRequestPacket(PacketBuffer buf) {
        int size = buf.readInt();
        for(int i = 0; i < size; i++){
            networkIDs.add(buf.readInt());
        }
        type = NBTType.values()[buf.readInt()];
    }

    @Override
    public void encode(PacketBuffer buf) {
        buf.writeInt(networkIDs.size());

        networkIDs.forEach(buf::writeInt);
        buf.writeInt(type.ordinal());
    }

    @Override
    public Object handle(NetworkEvent.Context ctx) {
        List<IFluxNetwork> networks = Lists.newArrayList();

        for(Integer i : networkIDs){
            IFluxNetwork network = FluxNetworkCache.INSTANCE.getNetwork(i);
            if(!network.isInvalid()) {
                networks.add(network);
            }
        }
        if(!networks.isEmpty()) {
            return new NetworkUpdatePacket(Lists.newArrayList(networks), type);
        }
        return null;
    }
}
