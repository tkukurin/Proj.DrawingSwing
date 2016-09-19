package co.kukurin.actions;

import java.awt.event.*;
import java.util.function.Consumer;

public class KeyListenerFactory {

    private KeyListenerFactory() {
    }

    public static KeyListener createOnReleaseEvent(Consumer<KeyEvent> keyEventConsumer) {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                keyEventConsumer.accept(e);
            }
        };
    }

    public static KeyListener createOnPressEvent(Consumer<KeyEvent> keyEventConsumer) {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyEventConsumer.accept(e);
            }
        };
    }

    public static KeyListener createOnTypedEvent(Consumer<KeyEvent> keyEventConsumer) {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                keyEventConsumer.accept(e);
            }
        };
    }

    public static class KeyListenerBuilder {
        private static final Consumer<KeyEvent> NO_OP = e -> {};
        private Consumer<KeyEvent> pressConsumer = NO_OP;
        private Consumer<KeyEvent> releaseConsumer = NO_OP;

        public KeyListenerBuilder onPress(Consumer<KeyEvent> pressConsumer) {
            this.pressConsumer = pressConsumer;
            return this;
        }

        public KeyListenerBuilder onRelease(Consumer<KeyEvent> onReleaseConsumer) {
            this.releaseConsumer = onReleaseConsumer;
            return this;
        }

        public KeyListener build() {
            return new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    KeyListenerBuilder.this.pressConsumer.accept(e);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    KeyListenerBuilder.this.releaseConsumer.accept(e);
                }
            };
        }
    }

    public static KeyListenerBuilder builder() {
        return new KeyListenerBuilder();
    }

}
