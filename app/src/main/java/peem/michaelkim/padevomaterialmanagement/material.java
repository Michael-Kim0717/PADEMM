package peem.michaelkim.padevomaterialmanagement;

/**
 * Created by Michael Kim on 1/22/2018.
 */

public class material {

    String id, link;
    int owned, needed;

    public material (String id, String link, int owned, int needed) {
        this.id = id;
        this.link = link;
        this.owned = owned;
        this.needed = needed;
    }

    public static abstract class materialDB {
        public static final String MATERIAL_ID = "material_id";
        public static final String MATERIAL_COUNT = "material_count";
        public static final String MATERIAL_TABLE = "material_table";
    }

}
