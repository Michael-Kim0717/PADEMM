package peem.michaelkim.padevomaterialmanagement;

/**
 * Created by Michael Kim on 11/22/2017.
 */

public class monsterBase {

    String imageLink;
    String monsterID;
    String monsterName;
    String evoChoice;
    String priority;

    public monsterBase(String imageLink, String monsterID, String monsterName, String evoChoice, String priority) {
        this.imageLink = imageLink;
        this.monsterID = monsterID;
        this.monsterName = monsterName;
        this.evoChoice = evoChoice;
        this.priority = priority;
    }

    public static abstract class monsterBaseDB {
        public static final String IMAGE_LINK = "image_link";
        public static final String MONSTER_ID = "monster_id";
        public static final String MONSTER_NAME = "monster_name";
        public static final String EVO_CHOICE = "evo_choice";
        public static final String PRIORITY = "priority";
        public static final String TABLE_NAME = "table_name";
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getMonsterID() {
        return monsterID;
    }

    public void setMonsterID(String monsterID) {
        this.monsterID = monsterID;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public String getEvoChoice() {
        return evoChoice;
    }

    public void setEvoChoice(String evoChoice) {
        this.evoChoice = evoChoice;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
