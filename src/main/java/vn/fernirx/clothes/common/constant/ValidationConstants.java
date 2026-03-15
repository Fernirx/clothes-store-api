package vn.fernirx.clothes.common.constant;

public final class ValidationConstants {
    private ValidationConstants() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }

    public static final class Patterns {
        public static final String PHONE = "^\\+[1-9]\\d{1,14}$";
        public static final String OTP = "^\\d{6}$";
        public static final String ISO_ZONED_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ssXXX";
        public static final String POSTAL_CODE = "^[0-9]{6}$";

        private Patterns() {}
    }
}
