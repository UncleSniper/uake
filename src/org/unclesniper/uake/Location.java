package org.unclesniper.uake;

public final class Location {

	public static final Location UNKNOWN = new Location(null, 0, 0);

	private final String file;

	private final int line;

	private final int column;

	public Location(String file, int line, int column) {
		this.file = file;
		this.line = line;
		this.column = column;
	}

	public String getFile() {
		return file;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public boolean isUnknown() {
		return (file == null || file.length() == 0) && line <= 0;
	}

	public String toString() {
		String f;
		if(file == null || file.length() == 0) {
			if(line <= 0)
				return "<unknown location>";
			f = "<unknown file>";
		}
		else
			f = file;
		StringBuilder builder = new StringBuilder(f);
		if(line > 0) {
			builder.append(':' + String.valueOf(line));
			if(column > 0)
				builder.append(':' + String.valueOf(column));
		}
		else
			builder.append(":<unknown line>");
		return builder.toString();
	}

}
