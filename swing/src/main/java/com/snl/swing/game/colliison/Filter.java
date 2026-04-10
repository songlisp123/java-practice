package com.snl.swing.game.colliison;

public interface Filter {

    Filter DEFAULT_FILTER =new Filter() {
        @Override
        public boolean isAllowed(Filter filter) {
            return true;
        }

        @Override
        public String toString() {
            return "默认过滤器:[]";
        }
    };

    boolean isAllowed(Filter filter);
}
