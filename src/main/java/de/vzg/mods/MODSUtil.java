package de.vzg.mods;

import org.jdom2.Namespace;

public class MODSUtil {

    private static final String MODS_URL = "http://www.loc.gov/mods/v3";

    public static final Namespace MODS_NAMESPACE = Namespace.getNamespace("mods", MODS_URL);

    private static final String XLINK_URL = "http://www.w3.org/1999/xlink";

    public static final Namespace XLINK_NAMESPACE = Namespace.getNamespace("xlink", XLINK_URL);

    public static <T> T PASS(T with) {
        return with;
    }
}
