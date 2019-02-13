package org.unclesniper.jetplex.server;

import java.io.OutputStream;

public interface PlexOutputStream extends PlexStream {

	OutputStream getActualStream();

}
