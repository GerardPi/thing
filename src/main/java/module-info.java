module io.github.gerardpi.thing {
    requires javafx.controls;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    opens io.github.gerardpi.thing to javafx.graphics;
    exports io.github.gerardpi.thing;
}