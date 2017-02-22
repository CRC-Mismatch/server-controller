import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%date{dd MMM YY HH:mm:ss} [%thread] %-5level %logger - %msg%n"
    }
}

logger("com.cerner.fsi", Level.DEBUG)
root(Level.DEBUG, ["STDOUT"])
