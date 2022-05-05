package statics;

public class Colors {
    // retourne le tableau {couleursCase, couleurTexte}
    public static String[] get(int nb){
        switch (nb){
            case 2:
                return new String[] {"#EEE4DA", "#776E65"};
            case 4:
                return new String[] {"#EEE2CE", "#776E65"};
            case 8:
                return new String[] {"#F3B27E", "#F9F8F5"};
            case 16:
                return new String[] {"#F6976B", "#F9F8F5"};
            case 32:
                return new String[] {"#F77E68", "#F9F8F5"};
            case 64:
                return new String[] {"#F76147", "#F9F8F5"};
            case 128:
                return new String[] {"#ECCD72", "#F9F8F5"};
            case 256:
                return new String[] {"#EBCA61", "#F9F8F5"};
            case 512:
                return new String[] {"#EEC752", "#F9F8F5"};
            case 1024:
                return new String[] {"#EEC340", "#F9F8F5"};
            case 2048:
                return new String[] {"#EEC22E", "#F9F8F5"};
            case 4096:
            case 8192:
            case 16384:
            case 32768:
            case 65536:
            case 131072:
                return new String[] {"#3D3A31", "#F9F8F5"};
            default:
                return new String[] {"#CCC1B2", "#CCC1B2"};
        }
    }
}