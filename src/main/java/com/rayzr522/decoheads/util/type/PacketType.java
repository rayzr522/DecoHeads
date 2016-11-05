package com.rayzr522.decoheads.util.type;

public enum PacketType {

    PlayOut("PlayOut"),
    PlayIn("PlayIn");

    public String prefix;

    PacketType(String prefix) {

        this.prefix = prefix;

    }

}
