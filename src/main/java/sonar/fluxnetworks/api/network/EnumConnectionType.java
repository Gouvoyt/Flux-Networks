package sonar.fluxnetworks.api.network;

public enum EnumConnectionType {
    POINT(0x882828),
    PLUG(0x609732),
    STORAGE(0x295e8a),
    CONTROLLER(0x647878);

    public int color;

    EnumConnectionType(int color) {
        this.color = color;
    }

    public boolean canAddEnergy() {
        return this == PLUG;
    }

    public boolean canRemoveEnergy() {
        return this == POINT;
    }

    public boolean isController() {
        return this == CONTROLLER;
    }

    public boolean isStorage() {
        return this == STORAGE;
    }
}
