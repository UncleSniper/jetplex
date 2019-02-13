package org.unclesniper.jetplex;

import java.io.File;
import java.util.Map;
import java.io.IOException;
import org.unclesniper.jetplex.server.Pool;
import org.unclesniper.jetplex.server.PlexStream;
import org.unclesniper.jetplex.server.PlexSocket;
import org.unclesniper.jetplex.server.PlexProcess;

public class ServerState {

	public interface Client {

		void sendPacket(ServerProto.Packet packet) throws IOException;

	}

	private Client client;

	private final Pool<PlexStream> streams = new Pool<PlexStream>();

	private final Pool<PlexSocket> sockets = new Pool<PlexSocket>();

	private final Pool<PlexProcess> processes = new Pool<PlexProcess>();

	public ServerState(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void handleClientPacket(ClientProto.Packet packet) throws IOException {
		switch(packet.getPacketCase()) {
			case EXEC:
				handleExec(packet.getExec());
				break;
			case CONNECT:
				handleConnect(packet.getConnect());
				break;
			case DISCONNECT:
				handleDisconnect(packet.getDisconnect());
				break;
			case LISTEN:
				handleListen(packet.getListen());
				break;
			case SEND:
				handleSend(packet.getSend());
				break;
			case RECV:
				handleRecv(packet.getRecv());
				break;
			case WRITE:
				handleWrite(packet.getWrite());
				break;
			case CLOSE:
				handleClose(packet.getClose());
				break;
			case PACKET_NOT_SET:
				handlePing();
				break;
			default:
				throw new Doom("Unrecognized client packet type: " + packet.getPacketCase().name());
		}
	}

	private static ServerProto.Packet.Failed makeError(Throwable error) {
		return ServerProto.Packet.Failed.newBuilder()
				.setExceptionType(error.getClass().getName())
				.setErrorMessage(error.getMessage())
				.build();
	}

	private void handleExec(ClientProto.Packet.Exec packet) throws IOException {
		final PlexProcess xproc;
		final int procID;
		final Process proc;
		try {
			final ProcessBuilder builder = new ProcessBuilder(packet.getArgvList());
			final int flags = packet.getFlags();
			final Map<String, String> environ = builder.environment();
			if((flags & ProtoConstants.C_EXEC_FL_CLEAR_ENV) != 0)
				environ.clear();
			for(String evspec : packet.getEnvironList()) {
				int pos = evspec.indexOf('=');
				if(pos < 0)
					environ.remove(evspec);
				else
					environ.put(evspec.substring(0, pos), evspec.substring(pos + 1));
			}
			if(packet.hasWorkDir()) {
				String wd = packet.getWorkDir();
				if(wd != null && wd.length() > 0)
					builder.directory(new File(wd));
			}
			xproc = new PlexProcess(this);
			procID = processes.alloc(xproc);
			xproc.setID(procID);
			try {
				proc = builder.start();
			}
			catch(IOException e) {
				processes.free(procID, false);
				throw e;
			}
		}
		catch(RuntimeException | IOException | Error e) {
			client.sendPacket(ServerProto.Packet.newBuilder().setExecFailed(ServerState.makeError(e)).build());
			return;
		}
		xproc.setProcess(proc);
		//TODO
	}

	private void handleConnect(ClientProto.Packet.Connect packet) throws IOException {
		//TODO
	}

	private void handleDisconnect(ClientProto.Packet.Disconnect packet) throws IOException {
		//TODO
	}

	private void handleListen(ClientProto.Packet.Listen packet) throws IOException {
		//TODO
	}

	private void handleSend(ClientProto.Packet.Send packet) throws IOException {
		//TODO
	}

	private void handleRecv(ClientProto.Packet.Recv packet) throws IOException {
		//TODO
	}

	private void handleWrite(ClientProto.Packet.Write packet) throws IOException {
		//TODO
	}

	private void handleClose(ClientProto.Packet.Close packet) throws IOException {
		//TODO
	}

	private void handlePing() throws IOException{
		client.sendPacket(ServerProto.Packet.newBuilder().build());
	}

}
