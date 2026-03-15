package vn.fernirx.clothes.common.constant;

public final class PaginationConstants {

    private PaginationConstants() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }

    /* Default page if the client does not provide 'page' or page < 0 */
    public static final int DEFAULT_PAGE = 0;

    /* Default number of items per page if the client does not provide 'size' or size <= 0 */
    public static final int DEFAULT_SIZE = 20;

    /* Maximum number of items per page to prevent clients from requesting too much data */
    public static final int MAX_SIZE = 100;
}
