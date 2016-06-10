package com.rayzr522.decoheads.type;

public enum PacketType {

	PlayOut("PlayOut"), PlayIn("PlayIn");

	public String prefix;

	PacketType(String prefix) {

		this.prefix = prefix;

	}

}
