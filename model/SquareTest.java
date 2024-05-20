package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class SquareTest {

    //board with mines
    public Square[][] board1 = {
            {
                    new Square(true, new Location(0, 0)),
                    new Square(false, new Location(0, 1)),
                    new Square(true, new Location(0, 2))
            },
            {
                    new Square(false, new Location(1, 0)),
                    new Square(false, new Location(1, 1)),
                    new Square(false, new Location(1, 2))

            },
            {
                    new Square(false, new Location(2, 0)),
                    new Square(true, new Location(2, 1)),
                    new Square(false, new Location(2, 2))

            }
    };


    //board without mines
    public Square[][] boardBlank = {
            {
                    new Square(false, new Location(0, 0)),
                    new Square(false, new Location(0, 1)),
                    new Square(false, new Location(0, 2))
            },
            {
                    new Square(false, new Location(1, 0)),
                    new Square(false, new Location(1, 1)),
                    new Square(false, new Location(1, 2))

            },
            {
                    new Square(false, new Location(2, 0)),
                    new Square(false, new Location(2, 1)),
                    new Square(false, new Location(2, 2))

            }
    };

    @Test
    public void testInstance() {
        Square s = new Square(false, new Location(0, 0));
        assertEquals(new Location(0, 0), s.getLocation());
    }

    @Test
    public void testGetNum() {
        int expected = 3;
        Square s = board1[1][1];
        int actual = s.getNum(board1);
        assertEquals(expected, actual);

    }

    @Test
    public void testGetNumEdge() {
        int expected = 2;
        Square s = board1[0][1];
        int actual = s.getNum(board1);
        assertEquals(expected, actual);

    }

    @Test
    public void testGetNumCorner() {
        int expected = 1;
        Square s = board1[2][2];
        int actual = s.getNum(board1);
        assertEquals(expected, actual);

    }

    @Test
    public void testUncoverBlank() {
        boolean expected = false;
        Square s = board1[1][1];
        boolean actual = s.uncover(board1);
        assertEquals(expected, actual);

    }

    @Test
    public void testUncoverMine() {
        boolean expected = true;
        Square s = board1[0][0];
        boolean actual = s.uncover(board1);
        assertEquals(expected, actual);

    }

    @Test
    public void testUncoverAll() {
        Square s = boardBlank[1][1];
        s.uncover(boardBlank);
        for (Square[] array : boardBlank) {
            for (Square p : array) {
                assertEquals(false, p.isCovered());
            }
        }

    }

}
