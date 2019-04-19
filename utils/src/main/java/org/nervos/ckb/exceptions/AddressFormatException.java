package org.nervos.ckb.exceptions;

/**
 * Created by duanyytop on 2019-04-18.
 * Copyright © 2018 Nervos Foundation. All rights reserved.
 */
public class AddressFormatException extends IllegalArgumentException {
    public AddressFormatException() {
        super();
    }

    public AddressFormatException(String message) {
        super(message);
    }

    /**
     * This exception is thrown by Bech32 when you try to decode data and a character isn't valid.
     * You shouldn't allow the user to proceed in this case.
     */
    public static class InvalidCharacter extends AddressFormatException {
        public final char character;
        public final int position;

        public InvalidCharacter(char character, int position) {
            super("Invalid character '" + Character.toString(character) + "' at position " + position);
            this.character = character;
            this.position = position;
        }
    }

    /**
     * This exception is thrown by Bech32 when you try to decode data and a character isn't valid.
     * You shouldn't allow the user to proceed in this case.
     */
    public static class InvalidDataLength extends AddressFormatException {
        public InvalidDataLength() {
            super();
        }

        public InvalidDataLength(String message) {
            super(message);
        }
    }

    /**
     * This exception is thrown by Bech32 when you try to decode data and a character isn't valid.
     * You shouldn't allow the user to proceed in this case.
     */
    public static class InvalidChecksum extends AddressFormatException {
        public InvalidChecksum() {
            super("Checksum does not validate");
        }

        public InvalidChecksum(String message) {
            super(message);
        }
    }

    /**
     * This exception is thrown by the hierarchy of classes when you try and decode an
     * address or private key with an invalid prefix (version header or human-readable part). You shouldn't allow the
     * user to proceed in this case.
     */
    public static class InvalidPrefix extends AddressFormatException {
        public InvalidPrefix() {
            super();
        }

        public InvalidPrefix(String message) {
            super(message);
        }
    }

}
