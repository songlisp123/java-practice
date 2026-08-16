package com.snl.swing.game2026;

import com.snl.swing.game2026.util.Dispose;

public interface LifecycleListener extends Dispose {

    /**
     * 当{@link Application}准备暂停的时候调用此方法
     * {@code if (!wasPaused && paused) { // just been minimized
     * 				wasPaused = true;
     * 				synchronized (lifecycleListeners) {
     * 					LifecycleListener[] listeners = lifecycleListeners.begin();
     * 					for (int i = 0, n = lifecycleListeners.size; i < n; ++i)
     * 						listeners[i].pause();
     * 					lifecycleListeners.end();
     * 				                }
     * 				listener.pause();            * 			}
     * 				}
     * @implNote 上面为源代码实现
     * @since ⌚2026年6月28日16:13:49
     */
    void pause();

    /**
     * 当{@code Application}准备回复的时候调用这个方法
     * {@code if (wasPaused && !paused) { // just been restore from being minimized
     * 				wasPaused = false;
     * 				synchronized (lifecycleListeners) {
     * 					LifecycleListener[] listeners = lifecycleListeners.begin();
     * 					for (int i = 0, n = lifecycleListeners.size; i < n; ++i)
     * 						listeners[i].resume();
     * 					lifecycleListeners.end();
     * 				                }
     * 				listener.resume();            * 			}
     * 				}
     */
    void resume();
}
