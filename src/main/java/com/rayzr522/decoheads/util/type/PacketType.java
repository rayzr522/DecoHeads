package com.rayzr522.decoheads.util.type;

public enum PacketType {

    PlayOut("PlayOut"),
    PlayIn("PlayIn");

    private final String prefix;

    PacketType(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the packet prefix
     */
    public String getPrefix() {
        return prefix;
    }

}
