package co.kukurin.actions;

import java.awt.event.*;
import java.util.function.Consumer;

public class MouseListenerFactory {
    private MouseListenerFactory() {}

    public static MouseListener createMouseListener(Consumer<MouseEvent> clickConsumer,
                                                    Consumer<MouseEvent> releaseConsumer) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickConsumer.accept(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                releaseConsumer.accept(e);
            }
        };
    }

    public static MouseMotionListener createMouseDragListener(Consumer<MouseEvent> onDrag) {
        return new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                onDrag.accept(e);
            }
        };
    }

    public static class MouseListenerBuilder {
        private static final Consumer<MouseEvent> NO_OP = e -> {};

        private Consumer<MouseEvent> clickConsumer = NO_OP;
        private Consumer<MouseEvent> releaseConsumer = NO_OP;
        private Consumer<MouseEvent> moveConsumer = NO_OP;
        private Consumer<MouseEvent> pressConsumer = NO_OP;
        private Consumer<MouseEvent> dragConsumer = NO_OP;

        public MouseListenerBuilder onClick(Consumer<MouseEvent> onClickConsumer) {
            this.clickConsumer = onClickConsumer;
            return this;
        }

        public MouseListenerBuilder onRelease(Consumer<MouseEvent> onReleaseConsumer) {
            this.releaseConsumer = onReleaseConsumer;
            return this;
        }

        public MouseListenerBuilder onMove(Consumer<MouseEvent> onMoveConsumer) {
            this.moveConsumer = onMoveConsumer;
            return this;
        }

        public MouseListenerBuilder onPress(Consumer<MouseEvent> onPressConsumer) {
            this.pressConsumer = onPressConsumer;
            return this;
        }

        public MouseListenerBuilder onDrag(Consumer<MouseEvent> onDragConsumer) {
            this.dragConsumer = onDragConsumer;
            return this;
        }

        public MouseListener build() {
            return new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    MouseListenerBuilder.this.pressConsumer.accept(e);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    MouseListenerBuilder.this.clickConsumer.accept(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    MouseListenerBuilder.this.releaseConsumer.accept(e);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    MouseListenerBuilder.this.moveConsumer.accept(e);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    MouseListenerBuilder.this.dragConsumer.accept(e);
                }
            };
        }
    }

    public static MouseListenerBuilder builder() {
        return new MouseListenerBuilder();
    }
}
