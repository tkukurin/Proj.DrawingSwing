package co.kukurin.actions;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;

public class ComponentListenerFactory {

    private ComponentListenerFactory() {}

    public static ComponentAdapter createResizeListener(Consumer<ComponentEvent> componentEventConsumer) {
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                componentEventConsumer.accept(e);
            }
        };
    }

}
