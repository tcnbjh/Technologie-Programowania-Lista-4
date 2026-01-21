package pl.moje.go.serwer;

import pl.moje.go.common.Kolor;
import java.util.ArrayList;


/**
 * Klasa przechowuje informacje o aktualnym stanie planszy (19x19).
 * Logikę sprawdzania ruchu, zdejmowania kamieni, oznaczania grup jako martwe
 * oraz liczenia terytorium deleguje do osobnych klas pomocniczych.
 */

public class Board {

    private final int size = 19;
    private final Kolor[][] fields;
    private final MoveValidator validator = new MoveValidator();
    private final StoneRemover remover = new StoneRemover();
    private final StonePainter painter = new StonePainter();
    private final DeadStonesRemover dRemover = new DeadStonesRemover();
    private final TeritoryCounter teritoryCounter = new TeritoryCounter();

    /**
     * Konstruktor tworzy pustą plansze o wymiarze 19x19
     */
    Board() {
        fields = new Kolor[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j] = Kolor.NONE;
            }
        }
    }

    /**
     * sprawdza czy podana pozycja jest na planszy
     * @param x
     * @param y
     * @return
     */
    private boolean isOnBoard(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    /**
     * Próbuje postawic kamień na podanej pozycji
     * @param x
     * @param y
     * @param color kolor gracza ktory wykonuje ruch
     * @return zwraca ilosc potencjalnie zbitych kamieni przeciwnika
     */
    public int placeStone(int x, int y, Kolor color) {
        if (!isOnBoard(x, y)){
            return -1;
        }

        if (fields[x][y] != Kolor.NONE){
            return -1;
        }

        if(!validator.isValidMove(fields, x, y, color)){
            return -1;
        }

        validator.clearBlocked(color);

        setField(x, y, color);

        ArrayList<Integer> list = remover.removeAround(x, y, fields, color);

        int dlg = list.size();

        System.out.println(list);

        for(int i = 0; i < dlg - 1; i = i + 2){
            validator.setBlock(list.get(i), list.get(i+1), color);
        }

        return list.get(dlg - 1);
    }

    /**
     * Zwraca obiekt klasy MoveValidator by moc w kontrolerze wyczyscic tablice wewnetrzna obiektu
     * @return zwraca obiekt klasy MoveValidator
     */
    public MoveValidator getValidator(){
        return validator;
    }

    /**
     * Metoda delegująca obiekt klasy StonePainter do zmiany koloru grupy na podanej pozycji
     * na ten sam ale martwy kolor
     * @param x
     * @param y
     */
    public void setDeadGroup(int x, int y){
        Kolor color = fields[x][y];
        switch (color) {
            case Kolor.BLACK:
                painter.setGroupColorTo(Kolor.DEAD_BLACK, x, y, fields);
                break;
            case Kolor.DEAD_BLACK:
                painter.setGroupColorTo(Kolor.BLACK, x, y, fields);
                break;
            case Kolor.DEAD_WHITE:
                painter.setGroupColorTo(Kolor.WHITE, x, y, fields);
                break;
            case Kolor.WHITE:
                painter.setGroupColorTo(Kolor.DEAD_WHITE, x, y, fields);
                break;
            default:
                break;
        }
    }



    private void setField(int x, int y, Kolor color){
        fields[x][y] = color;
    }


    public String toAscii() {
        StringBuilder sb = new StringBuilder();

        sb.append("   ");
        for (int i = 0; i < size; i++) {
            sb.append(i).append(" ");
        }
        sb.append("\n");

        for (int i = 0; i < size; i++) {
            if (i < 10){
                sb.append(" ");
            }
            sb.append(i).append(" ");

            for (int j = 0; j < size; j++) {
                Kolor k = fields[i][j];
                char c;

                switch (k){
                    case BLACK -> c = 'B';
                    case WHITE -> c = 'W';
                    case DEAD_BLACK -> c = 'b';
                    case DEAD_WHITE -> c = 'w';
                    default -> c = '□';
                }

                sb.append(c).append(" ");
                if (j >= 10){
                    sb.append(" ");
                }
            }

            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Metoda delegujaca obiekt klasy DeadStonesRemover do usuniecia martwych kamieni z planszy
     * @return zwraca tablice z liczbami zebranych martwych kamieni z planszy
     */
    public int[] removeDeadStones(){
        return dRemover.removeDeadStones(fields);
    }

    /**
     * Metoda delegujaca obiekt klasy StonePainter do przemienienia wszystkich martwych kamieni na żywe
     */
    public void setDeadToAlive(){
        painter.setToAlive(fields);
    }

    /**
     * Metoda delegujaca obiekt klasy TeritoryCounter do usuniecia martwych kamieni z planszy
     * @return zwraca tablice z liczbami pustych pol na planszy otoczonych tylko przez kamienie jednego koloru
     */
    public int[] countTeritory(){
        return teritoryCounter.count(fields);
    }
}
