package com.etz.fraudeagleeyemanager.enums;

public enum CrudOperation implements PersistableEnum<String> {

    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    String value;

    CrudOperation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static class Converter extends EnumValueTypeConverter<CrudOperation, String> {
        public Converter() {
            super(CrudOperation.class);
        }
    }
}
