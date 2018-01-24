package uk.co.satander.mortgages;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoggerServiceTest {

    private Appender mockAppender;

    @Captor
    private ArgumentCaptor<ch.qos.logback.classic.spi.LoggingEvent> captorLoggingEvent;

    private Logger logger;

    @Before
    public void setUp() throws Exception {
        logger = (Logger)
                LoggerFactory.getLogger("moon.alert");
        mockAppender = Mockito.mock(Appender.class);


    }

    @Test
    public void testAlertLogger() throws Exception {
        Mockito.when(mockAppender.getName()).thenReturn("alertAppender");
        logger.addAppender(mockAppender);
        LoggerService.raiseAlert("MOERR001", "OLA0000000001");

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getAllValues().get(0);
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("MOERR001 OLA0000000001 failed at First Risk."));
        Assert.assertThat(loggingEvent.getLevel().levelStr, is("ERROR"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAlertLoggerWhenInvalidErrorCode() throws Exception {
        Mockito.when(mockAppender.getName()).thenReturn("alertAppender");
        logger.addAppender(mockAppender);
        LoggerService.raiseAlert("KJHKLKJHJ$%", "OLA0000000001");
    }

    @Test
    public void testMoonMonitor() throws Exception {
        logger = (Logger)
                LoggerFactory.getLogger("moon.monitor");
        Mockito.when(mockAppender.getName()).thenReturn("monitorAppender");
        logger.addAppender(mockAppender);
        LoggerService.monitor("MO002", "OLA0000000002");

        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        LoggingEvent loggingEvent = captorLoggingEvent.getAllValues().get(0);
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("MO002 OLA0000000002 PAF failed."));
        Assert.assertThat(loggingEvent.getLevel().levelStr, is("INFO"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMoonMonitor_WhenInvalidErrorCode() throws Exception {
        logger = (Logger)
                LoggerFactory.getLogger("moon.monitor");
        Mockito.when(mockAppender.getName()).thenReturn("monitorAppender");
        logger.addAppender(mockAppender);
        LoggerService.monitor("KJHKH", "OLA0000000003");

    }

}
