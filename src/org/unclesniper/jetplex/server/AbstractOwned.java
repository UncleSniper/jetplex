package org.unclesniper.jetplex.server;

import org.unclesniper.jetplex.ServerState;

public abstract class AbstractOwned implements Owned {

	private final ServerState server;

	public AbstractOwned(ServerState server) {
		this.server = server;
	}

	@Override
	public ServerState getServer() {
		return server;
	}

}
