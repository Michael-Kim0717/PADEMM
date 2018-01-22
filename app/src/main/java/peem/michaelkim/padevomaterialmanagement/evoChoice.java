package peem.michaelkim.padevomaterialmanagement;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Michael Kim on 11/23/2017.
 */

public class evoChoice {

    String evoMonster;
    ArrayList<String> evoMaterials;
    ArrayList<String> evoMaterialsLinks;

    public evoChoice(String evoMonster, ArrayList<String> evoMaterials, ArrayList<String> evoMaterialsLinks) {
        this.evoMonster = evoMonster;
        this.evoMaterials = evoMaterials;
        this.evoMaterialsLinks = evoMaterialsLinks;
    }

}
