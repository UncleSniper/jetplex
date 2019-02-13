package org.unclesniper.jetplex.server;

import java.io.Closeable;

public interface PlexStream extends Poolable, Identifiable, Closeable {

	boolean isInputStream();

}
