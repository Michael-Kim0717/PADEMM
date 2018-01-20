package peem.michaelkim.padevomaterialmanagement;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Michael Kim on 11/22/2017.
 */

public class monsterBase implements Comparable<monsterBase> {

    String monsterElement;
    String imageLink;
    String monsterID;
    String monsterName;
    String evoChoices;
    String evoMats;
    String evoChoice;
    String priority;

    public monsterBase(String imageLink, String monsterID, String monsterName, String evoChoice, String priority, String monsterElement, String evoChoices, String evoMats) {
        this.monsterElement = monsterElement;
        this.imageLink = imageLink;
        this.monsterID = monsterID;
        this.monsterName = monsterName;
        this.evoChoices = evoChoices;
        this.evoMats = evoMats;
        this.evoChoice = evoChoice;
        this.priority = priority;
    }

    public static abstract class monsterBaseDB {
        public static final String IMAGE_LINK = "image_link";
        public static final String MONSTER_ID = "monster_id";
        public static final String MONSTER_NAME = "monster_name";
        public static final String EVO_CHOICE = "evo_choice";
        public static final String PRIORITY = "priority";
        public static final String MONSTER_ELEMENT = "monster_element";
        public static final String EVO_CHOICES = "evo_choices";
        public static final String EVO_MATERIALS = "evo_mats";
        public static final String TABLE_NAME = "table_name";
    }


    @Override
    public int compareTo(monsterBase monster){
        int compareElement = findElement(((monsterBase) monster).monsterElement);

        return findElement(this.monsterElement) - compareElement;
    }

    private int findElement(String element) {
        switch(element) {
            case "Fire" :
                return 0;
            case "Water" :
                return 1;
            case "Wood" :
                return 2;
            case "Light" :
                return 3;
            default :
                return 4;
        }

    }
}
