package modele;

public class Case {
    private int value;
    private boolean merge=false;

    private final Game _game;

    public Case(int _value, Game game) {
        this.value = _value;
        this._game = game;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int _value){
        this.value = _value;
    }

    public void doubleValue(){
        this.value = this.value*2;
        this.merge = true;
    }

    public boolean hasMerge(){
        return this.merge;
    }

    public void changeMergeState(boolean _merge){
        this.merge = _merge;

    }

    public void shift() {
        Case neighbour = _game.getNeighbour(this);
        if(neighbour == null){
            _game.moveCase(this);
        }
    }
}
