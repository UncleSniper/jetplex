package org.unclesniper.jetplex.server;

import org.unclesniper.jetplex.ServerState;

public abstract class AbstractIdentifiable extends AbstractOwned implements Identifiable {

	private int id;

	public AbstractIdentifiable(ServerState server) {
		super(server);
		id = -1;
	}

	public AbstractIdentifiable(ServerState server, int id) {
		super(server);
		this.id = id;
	}

	@Override
	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

}
