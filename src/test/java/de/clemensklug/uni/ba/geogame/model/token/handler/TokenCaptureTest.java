package de.clemensklug.uni.ba.geogame.model.token.handler;

import de.clemensklug.uni.ba.geogame.model.Player;
import de.clemensklug.uni.ba.geogame.model.token.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author clemens
 */
public class TokenCaptureTest {
    private TokenCapture _capture;
    private Token _mockedToken;
    private Player _mockedPlayer;

    @Before
    public void setUp() throws Exception {
        _capture = new TokenCapture();
        _mockedToken = mock(Token.class);
        when(_mockedToken.isValid()).thenReturn(true);
        _mockedPlayer = mock(Player.class);
        when(_mockedPlayer.popDisposableItem()).thenReturn(_mockedToken);
    }

    @Test
    public void testDrop() throws Exception {
        assertTrue(_capture.drop(_mockedPlayer));
        verify(_mockedPlayer).popDisposableItem();
        assertTrue(_capture._map.containsKey(_mockedPlayer));
        _capture.drop(_mockedPlayer);
        verifyNoMoreInteractions(_mockedPlayer);
        assertFalse(_capture.drop(_mockedPlayer));
    }
}