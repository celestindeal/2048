package modele;

import java.io.Serializable;

public class Case  implements Serializable {
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

    public void reinitMergeState(){
        this.merge = false;

    }

    public void shift() {
        Case neighbour = _game.getNeighbour(this);
        if(neighbour == null){
            while(neighbour == null){
                _game.moveCase(this);
                if (!_game.isLeft(this)){
                    neighbour = _game.getNeighbour(this);
                }else{
                    break;
                }
            }

        }
        if(neighbour != null && neighbour.getValue() == this.getValue() && !neighbour.hasMerge()){
            _game.mergeCases(neighbour, this);
        }
    }

    public void merge(Case c){
        this.value += c.value;
        this.merge = true;
    }
}
