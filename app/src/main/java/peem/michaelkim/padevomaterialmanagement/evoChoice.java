package peem.michaelkim.padevomaterialmanagement;

import java.util.ArrayList;

/**
 * Created by Michael Kim on 11/23/2017.
 */

public class evoChoice {

    String evoMonster;
    ArrayList<String> evoMaterials;

    public evoChoice(String evoMonster, ArrayList<String> evoMaterials) {
        this.evoMonster = evoMonster;
        this.evoMaterials = evoMaterials;
    }

    public String getEvoMonster() {
        return evoMonster;
    }

    public void setEvoMonster(String evoMonster) {
        this.evoMonster = evoMonster;
    }

    public ArrayList<String> getEvoMaterials() {
        return evoMaterials;
    }

    public void setEvoMaterials(ArrayList<String> evoMaterials) {
        this.evoMaterials = evoMaterials;
    }

}
