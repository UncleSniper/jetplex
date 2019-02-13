package org.unclesniper.jetplex.server;

import org.unclesniper.jetplex.ServerState;

public class PlexProcess extends AbstractIdentifiable implements Poolable {

	private Process process;

	public PlexProcess(ServerState server) {
		super(server);
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	@Override
	public void destroy() {
		//TODO
	}

}
