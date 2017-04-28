package no.hvl.dowhile.utility;

public enum Constant {
    OPERATION_NAME_LABEL("Test"),
    OPERATION_NAME_INPUT(""),
    OPERATION_DATE_LABEL(""),
    DATE_PICKER(""),
    TIME_PICKER("");

    private String text;

    Constant(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
