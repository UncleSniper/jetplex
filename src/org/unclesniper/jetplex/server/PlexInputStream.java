package org.unclesniper.jetplex.server;

import java.io.InputStream;

public interface PlexInputStream extends PlexStream {

	InputStream getActualStream();

}
