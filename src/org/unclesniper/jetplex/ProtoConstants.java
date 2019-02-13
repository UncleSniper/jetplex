package org.unclesniper.jetplex;

public class ProtoConstants {

	public static final int C_EXEC_FL_ATTACH_STDIN  = 001;
	public static final int C_EXEC_FL_ATTACH_STDOUT = 002;
	public static final int C_EXEC_FL_ATTACH_STDERR = 004;
	public static final int C_EXEC_FL_CLEAR_ENV     = 010;

	public static final int C_SEND_FL_APPEND = 001;
	public static final int C_SEND_FL_CREATE = 002;
	public static final int C_SEND_FL_EXCL   = 004;
	public static final int C_SEND_FL_NOCTTY = 010;
	public static final int C_SEND_FL_SYNC   = 020;
	public static final int C_SEND_FL_TRUNC  = 040;

	private ProtoConstants() {}

}
