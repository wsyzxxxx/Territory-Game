//import edu.duke651.wlt.models.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class MockTest {
//    public FireResult playOneTurn(Player p) {
//        Board b = p.getBoard();
//        Coordinate inputCoord;
//        do {
//            inputCoord = p.readCoordinate();
//        } while (!b.isValidCoordinate(inputCoord));
//        //Ship is interface with subclasses RectShip, TShip, ZShip
//        Ship s = b.getShipAt(inputCoord);
//        if (s == null) {
//            return new FireMissed();
//        }
//        return new FireHit(b);
//    }
//
//    @Test
//    public void testWithInValidInputFirst() {
//        //mock classes
//        Player player = Mockito.mock(Player.class);
//        Board board = Mockito.mock(Board.class);
//        Coordinate coordinate1 = Mockito.mock(Coordinate.class);
//        Coordinate coordinate2 = Mockito.mock(Coordinate.class);
//        Ship ship = Mockito.mock(Ship.class);
//
//        //mock return value for player
//        Mockito.when(player.getBoard()).thenReturn(board);
//        Mockito.when(player.readCoordinate()).thenReturn(coordinate1, coordinate2);
//
//        //mock return value for board
//        Mockito.when(board.isValidCoordinate(coordinate1)).thenReturn(false);
//        Mockito.when(board.isValidCoordinate(coordinate2)).thenReturn(true);
//        Mockito.when(board.getShipAt(coordinate2)).thenReturn(ship);
//
//        //verify the return value
//        Assertions.assertEquals(new FireHit(board), playOneTurn(player));
//
//        //verify calls for player
//        Mockito.verify(player, times(1)).getBoard();
//        Mockito.verify(player, times(2)).readCoordinate();
//        Mockito.verifyNoMoreInteractions(player);
//
//        //verify calls for board
//        Mockito.verify(board, times(1)).isValidCoordinate(coordinate1);
//        Mockito.verify(board, times(1)).isValidCoordinate(coordinate2);
//        Mockito.verify(board, times(1)).getShipAt(coordinate2);
//        Mockito.verifyNoMoreInteractions(board);
//    }
//
//    @Test
//    public void testWithValidInput() {
//        //mock classes
//        Player player = Mockito.mock(Player.class);
//        Board board = Mockito.mock(Board.class);
//        Coordinate coordinate = Mockito.mock(Coordinate.class);
//        Ship ship = Mockito.mock(Ship.class);
//
//        //mock return value for player
//        Mockito.when(player.getBoard()).thenReturn(board);
//        Mockito.when(player.readCoordinate()).thenReturn(coordinate);
//
//        //mock return value for board
//        Mockito.when(board.isValidCoordinate(coordinate)).thenReturn(true);
//        Mockito.when(board.getShipAt(coordinate)).thenReturn(ship);
//
//        //verify the return value
//        Assertions.assertEquals(new FireHit(board), playOneTurn(player));
//
//        //verify calls for player
//        Mockito.verify(player, times(1)).getBoard();
//        Mockito.verify(player, times(1)).readCoordinate();
//        Mockito.verifyNoMoreInteractions(player);
//
//        //verify calls for board
//        Mockito.verify(board, times(1)).isValidCoordinate(coordinate);
//        Mockito.verify(board, times(1)).getShipAt(coordinate);
//        Mockito.verifyNoMoreInteractions(board);
//    }
//
//    @Test
//    public void testWithMissedFire() {
//        //mock classes
//        Player player = Mockito.mock(Player.class);
//        Board board = Mockito.mock(Board.class);
//        Coordinate coordinate1 = Mockito.mock(Coordinate.class);
//        Coordinate coordinate2 = Mockito.mock(Coordinate.class);
//
//        //mock return value for player
//        Mockito.when(player.getBoard()).thenReturn(board);
//        Mockito.when(player.readCoordinate()).thenReturn(coordinate1, coordinate2);
//
//        //mock return value for board
//        Mockito.when(board.isValidCoordinate(coordinate1)).thenReturn(false);
//        Mockito.when(board.isValidCoordinate(coordinate2)).thenReturn(true);
//        Mockito.when(board.getShipAt(coordinate2)).thenReturn(null);
//
//        //verify the return value
//        Assertions.assertEquals(new FireMissed(), playOneTurn(player));
//
//        //verify calls for player
//        Mockito.verify(player, times(1)).getBoard();
//        Mockito.verify(player, times(2)).readCoordinate();
//        Mockito.verifyNoMoreInteractions(player);
//
//        //verify calls for board
//        Mockito.verify(board, times(1)).isValidCoordinate(coordinate1);
//        Mockito.verify(board, times(1)).isValidCoordinate(coordinate2);
//        Mockito.verify(board, times(1)).getShipAt(coordinate2);
//        Mockito.verifyNoMoreInteractions(board);
//    }
//}
