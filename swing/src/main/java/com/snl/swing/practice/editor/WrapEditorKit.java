package com.snl.swing.practice.editor;

import javax.swing.text.*;

public class WrapEditorKit extends StyledEditorKit {
    private ViewFactory defaultFactory = new WrapColumnFactory();

    @Override
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    static class WrapColumnFactory implements ViewFactory {
        @Override
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new WrapLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    // 使用 ParagraphView 保持段落行为
                    return new ParagraphView(elem);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }
            // fallback
            return new LabelView(elem);
        }
    }

    /**
     * 重写 LabelView 的 getMinimumSpan 以允许它在 X 方向上被压缩，从而触发换行。
     * 并且重写 breakView 支持在单词中间换断（当单词太长时也能换行）。
     */
    static class WrapLabelView extends LabelView {
        public WrapLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            if (axis == View.X_AXIS) {
                return 0; // 允许X方向被压缩
            }
            return super.getMinimumSpan(axis);
        }

        @Override
        public int getBreakWeight(int axis, float pos, float len) {
            if (axis == View.X_AXIS) {
                return View.ForcedBreakWeight;
            }
            return super.getBreakWeight(axis, pos, len);
        }

        @Override
        public View breakView(int axis, int p0, float pos, float len) {
            if (axis == View.X_AXIS) {
                // 使用父类的换断逻辑：会在合适位置断开
                return super.breakView(axis, p0, pos, len);
            }
            return super.breakView(axis, p0, pos, len);
        }
    }
}
