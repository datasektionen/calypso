package se.datasektionen.calypso.models.enums;

public enum ItemType {
	POST, EVENT;

	public static ItemType valueOfIgnoreCase(String string) {
		if (string == null || string.isEmpty()) return null;
		return ItemType.valueOf(string.toUpperCase());
	}
}
