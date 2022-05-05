package modele;

public class Case {
    private int value;
    private boolean merge=false;

    public Case(int _valeur) {
        this.value = _valeur;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int valeur){
        this.value = valeur;
    }

    public void doubleValue(){
        this.value = this.value*2;
        this.merge = true;
    }

    public boolean hasMerge(){
        return this.merge;
    }

    public void changeMergeState(boolean m){
        this.merge = m;

    }
}
