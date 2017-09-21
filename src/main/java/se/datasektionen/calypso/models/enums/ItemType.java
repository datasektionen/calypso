package se.datasektionen.calypso.models.enums;

public enum ItemType {
	POST, EVENT;

	public static ItemType valueOfIgnoreCase(String string) {
		return ItemType.valueOf(string.toUpperCase());
	}
}
