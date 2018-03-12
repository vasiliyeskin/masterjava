package ru.javaops.masterjava.web.handler;

import com.sun.xml.ws.api.handler.MessageHandler;
import com.sun.xml.ws.api.handler.MessageHandlerContext;
import ru.javaops.masterjava.web.Statistics;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import java.util.Set;

public class StatisticsLoggingHandler implements MessageHandler<MessageHandlerContext> {
    private static long startTime;

    public static long getStartTime() {
        return startTime;
    }

    public StatisticsLoggingHandler(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(MessageHandlerContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public boolean handleFault(MessageHandlerContext mhc) {
        return true;
    }

    public static class ClientHandler extends StatisticsLoggingHandler {
        public ClientHandler() {
            super(System.currentTimeMillis());
        }
    }

    public static class ServerHandler extends StatisticsLoggingHandler {

        public ServerHandler() {
            super(System.currentTimeMillis());
            Statistics.count("", StatisticsLoggingHandler.getStartTime(), Statistics.RESULT.SUCCESS );
        }
    }
}
