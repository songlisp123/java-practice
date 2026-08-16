package com.snl.swing.animator;

import com.snl.swing.animator.interpolator.Interpolator;
import com.snl.swing.animator.interpolator.LinearInterpolator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Animator {

    //动画持续时长
    private long duration;
    //动画开始时间
    private long startTime;
    //动画当前指针
    private long currentStartTime;
    //动画当前循环
    private int currentCycle;
    //动画开始延迟时间
    private int startDelay;
    //动画速度
    private int resolution;

    //暂停时间
    private long pauseBeginTime;
    //重复计数
    private double repeatCount;

    /*
    动画速度相关字段
     */
    private double acceleration;
    private double deceleration;
    private double startFraction;

    /*
    插值器：这是一个很重要的类
     */
    private Interpolator interpolator;

    /*
    运行模式
     */
    public final static int INFINITE = -1;

    /*
    事件类
     */
    private ArrayList<TimingTarget> targets;

    /*
    定时器类
     */
    private TimingSource swingTimer;

    /*
    布尔变量
     */
    //这是什么变量？？？
    private boolean intRepeatCount;
    private boolean timeToStop;
    private boolean hasBegun;
    private boolean running;

    private RepeatBehavior repeatBehavior;
    private EndBehavior endBehavior;
    private Direction direction;
    private TimingSourceTarget timingSourceTarget;



    public Animator(long duration) {
        this(duration, null);
    }

    public Animator(long duration, TimingTarget target) {
        if (duration < 0)
            throw new IllegalArgumentException("duration参数至少大于0");
        this.targets = new ArrayList<>();
        //当前循环次数
        this.currentCycle  = 0;
        //这个字段是什么意思？？？（当初始化设置为true的时候，当然
        // 播放从头开始设置）
        //是否归一化比率？？？,我们默认开启
        this.intRepeatCount = true;
        //暂停动作
        this.timeToStop = false;
        this.hasBegun = false; //是否开始，设置为false
        this.pauseBeginTime = 0L; //暂停开始时间
        this.running = false; //还未开始
        this.repeatCount = 1.0; //我们设置为1.0，是为了让动画走一遍

        //定时器定时值，相当与闹钟
        this.resolution = 20; //分辨率
        this.acceleration = 0.0; //加速区间
        this.deceleration = 0.0; //减速区间
        this.startFraction = 0.0; //开始百分比

        this.duration = duration;
        this.targets.add(target);

        //默认反向
        this.repeatBehavior = RepeatBehavior.REVERSE;
        //默认停在最后
        this.endBehavior = EndBehavior.HOLD;
        //默认从前到后
        this.direction = Direction.FORWARD;
        Toolkit tk =Toolkit.getDefaultToolkit();
        this.swingTimer = new SwingTimingSource();

        //插值器
        this.interpolator = LinearInterpolator.getInstance();
    }

    public Animator(long duration, double repeatCount, RepeatBehavior repeatBehavior,TimingTarget target) {
        this(duration,target);
        this.validateRepeatCount(repeatCount);
        this.repeatCount = repeatCount;
        this.repeatBehavior = repeatBehavior != null ? repeatBehavior : RepeatBehavior.REVERSE;
        this.intRepeatCount = Math.rint(repeatCount) == repeatCount;
    }

    private void throwExceptionIfRunning() {
        if (isRunning())
            throw new IllegalStateException("不能再动画运动时更改效果");
    }

    private void validateRepeatCount(double repeatCount) {
        if (repeatCount < (double)1.0F && repeatCount != INFINITE) {
            throw new IllegalArgumentException("repeatCount (" + repeatCount + ") cannot be <= 0");
        }
    }

    /*
    开始操作：有以下的作用
    设置hasBegun为false，
     */
    public void start() {
        throwExceptionIfRunning();
        this.hasBegun = false;
        this.running = true;
        this.startTime = (long) (System.nanoTime() / 1.0E6 + getStartDelay());
        if (duration != -1 && (this.direction == Direction.FORWARD && this.startFraction > 0.0 ||
                this.direction == Direction.BACKWARD && this.startFraction < 1.0))
        {
            //如果，我说如果，动画总时长是-1，并且开始部分处于动画的区间内？？？，而不是从0或者1开始
            double offsetF = this.direction == Direction.FORWARD ? startFraction : 1 - startFraction;
            //由于动画不是从头开始播放的，所以，默认来说，动画的时间要少一些
            long startDelta = (long) (this.duration * offsetF);
            this.startTime -= startDelta;
        }
        this.currentStartTime = this.startTime;
        this.swingTimer.start();
    }

    public void stop() {
        this.swingTimer.stop();
        //TODO 结束动画
        //通知所有的timing动画已完结
        this.end();
        this.timeToStop = false;
        this.running = false;
        this.pauseBeginTime = 0L;
    }

    //取消播放
    public void cancel() {
        this.swingTimer.stop();
        this.timeToStop = false;
        this.running = false;
        this.pauseBeginTime = 0L;
    }

    public void pause() {
        if (isRunning()) {
            this.pauseBeginTime = System.nanoTime();
            this.running = false;
            this.swingTimer.stop();
        }
    }

    public void  resume() {
        if (this.pauseBeginTime > 0L) {
            long pauseDelta = (long) ((System.nanoTime() - this.pauseBeginTime) / 1.0E6);
            //我们需要更新动画起始时间，增加detail
            this.startTime += pauseDelta;
            this.currentStartTime += pauseDelta;
            this.swingTimer.start();
            this.pauseBeginTime = 0L;
            this.running = true;
        }
    }

    public void begin() {
        synchronized (this.targets) {
            for (TimingTarget t : targets)
                t.begin();
        }
    }

    public void end() {
        synchronized (this.targets) {
            for (TimingTarget t : targets)
                t.end();
        }
    }

    public void repeat() {
        synchronized (this.targets) {
            for (TimingTarget t : targets)
                t.repeat();
        }
    }

    public long getTotalElapsedTime(long currentTime) {
        return currentTime - this.startTime;
    }

    public long getTotalElapsedTime() {
        long t = (long) (System.nanoTime() / 1E6);
        return getTotalElapsedTime(t);
    }

    public long getCycleElapsedTime(long currentTime) {
        return currentTime - this.currentStartTime;
    }

    public long getCycleElapsedTime() {
        long t = (long) (System.nanoTime() / 1E6);
        return getCycleElapsedTime(t);
    }

    public int getStartDelay() {
        return startDelay;
    }

    public boolean isRunning() {
        return running;
    }

    private void timingEvent(double fraction) {
        synchronized (this.targets) {
            for (TimingTarget t : targets)
                t.timingEvent(fraction);
        }
        if (this.timeToStop)
            stop();
    }


    /**
     * 核心算法，计算动画播放百分比
     * @return
     */
    public double getTimingFraction() {
        //TODO 待实现
        long currentTime = (long) (System.nanoTime() / 1.0E6);
        //获取重复播放到目前为止的时间
        long cycleElapsedTime = this.getCycleElapsedTime(currentTime);
//        System.out.println("cycleElapsedTime = " + cycleElapsedTime);
        //获取全部时间间隔
        long totalElapsedTime = this.getTotalElapsedTime(currentTime);
//        System.out.println("totalElapsedTime = " + totalElapsedTime);
        //获取当前循环，当前是第几遍？？？
        double currentCycle =(double) totalElapsedTime /this.duration;
        if (!hasBegun) {
            //在第一次播放动画的时候调用此事件
            //开始播放动画，通知所有的定时目标开始
            //通知所有的timingtarget开始
            begin();
            hasBegun = true;
            //动画开始播放标志
        }

        double fraction = 0;
        if (duration != -1 && this.repeatCount != -1.0 && currentCycle >= this.repeatCount) {
            //如果 动画不是无限循环并且 当前循环次数大于我们设置的重复次数，即动画播放完毕
            //这是什么意思？当然如果重复次数不等于-1，表示无限！！且我们当前的动画播放比前面大,动画播放完毕
            //动画播放完毕的时候，会根据结束行为
            switch (this.endBehavior) {
                case HOLD :
                    //如果停在最后一刻
                    if (this.intRepeatCount) {
                        //如果动画播放完毕，对于不同的播放方式，存在不同的起始处
                        if (this.direction == Direction.BACKWARD)
                            //从后到前 停在前
                            fraction = 0.0;
                        else if (this.direction == Direction.FORWARD) {
                            //从前到后 停在后
                            fraction = 1.0;
                        }
                    }else {
                        //如果动画播放完毕，且不归一化fraction，那么我们通过经过的循环时间/duration计算出当前分段
                        fraction = Math.min(1.0, (double) cycleElapsedTime / this.duration);
                    }
                    break;
                case RESET:
                default:
                    //默认情况下，回到起始点
                    fraction = 0.0;
            }
            //动画播放完毕，暂停动画
            this.timeToStop = true;
        } else if (this.duration != -1 && cycleElapsedTime > this.duration) {
            //如果循环时间超过动画总时长，也就是，动画已经播放了一遍,我们需要调用这个分支来处理
            //下一步的动作，比如，对于往返动画，需要触发方向的问题，对于loop动画，只是简单的从最初开始，
            //一般是0
            //获取当前的实际循环比率
            long actualCycleTime = cycleElapsedTime % this.duration;
            fraction = (double) actualCycleTime / this.duration;
            //更新当前开始时间,确保当前开始时间总是处于【0,1】区间
            this.currentStartTime = currentTime - actualCycleTime;
            //如果动画往返运动
            if (this.repeatBehavior == RepeatBehavior.REVERSE) {
                //判断是否是奇数播放，比如第一次，第三次播放
                boolean oddCycles = (int)(cycleElapsedTime / this.duration) % 2 > 0;
                if (oddCycles) {
                    //如果是奇数播放，我们更改播放方向，来反向播放动画
                    this.direction = this.direction == Direction.FORWARD ?
                            Direction.BACKWARD : Direction.FORWARD;
                }

                if (this.direction == Direction.BACKWARD) {
                    fraction = 1.0F - fraction;
                }
            }
            //通知所有的定时事件重复
            this.repeat();
        }else {
            //否则，运行动画,这个是动画运行主要逻辑
            fraction = 0.0;
            if (this.duration != -1) {
                //计算经过时间
                fraction = (double) cycleElapsedTime / this.duration;
                if (this.direction == Direction.BACKWARD) {
                    //如果动画向后播放
                    fraction = 1.0 - fraction;
                }
                //保障fraction处于[0,1]区间
                fraction = Math.min(fraction,1.0);
                fraction = Math.max(fraction,0.0);
            }
        }
        //通过调用帮助方法
        return timingEventPreprocessor(fraction);
    }
    
    private double timingEventPreprocessor(double fraction) {
        //如果存在加速减速区间
        if (acceleration != 0.0 || this.deceleration != 0.0) {
            //如果动画区间存在加速或者减速
            double runRate = 1.0 / (1.0 - this.acceleration / 2.0 - this.deceleration / 2.0);
            //如果处于加速区间
            if (fraction < this.acceleration) {
                double averageRunRate = runRate * (fraction / this.acceleration) / 2.0F;
                fraction *= averageRunRate;
            } else if (fraction > 1.0F - this.deceleration) {
                //如果处于减速区间
                double tdec = fraction - (1.0F - this.deceleration);
                double pdec = tdec / this.deceleration;
                fraction = runRate * (1.0F - this.acceleration / 2.0F - this.deceleration + tdec * (2.0F - pdec) / 2.0F);
            } else {
                //直线插值
                fraction = runRate * (fraction - this.acceleration / 2.0F);
            }

            if (fraction < 0.0F) {
                fraction = 0.0F;
            } else if (fraction > 1.0F) {
                fraction = 1.0F;
            }
        }
        //我们使用插值来基于fraction计算动画流失比
        System.out.println("原始时间："+fraction);
        return this.interpolator.interpolate(fraction);
    }

    public void addTargets(TimingTarget target) {
        if (target != null) {
            synchronized(this.targets) {
                if (!this.targets.contains(target)) {
                    this.targets.add(target);
                }
            }
        }
    }

    public void removeTarget(TimingTarget target) {
        synchronized(this.targets) {
            this.targets.remove(target);
        }
    }

    //getter
    public long getDuration() {
        return duration;
    }

    public TimingSource getSwingTimer() {
        return swingTimer;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getCurrentStartTime() {
        return currentStartTime;
    }

    public int getCurrentCycle() {
        return currentCycle;
    }

    public int getResolution() {
        return resolution;
    }

    public long getPauseBeginTime() {
        return pauseBeginTime;
    }

    public double getRepeatCount() {
        return repeatCount;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public double getStartFraction() {
        return startFraction;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public ArrayList<TimingTarget> getTargets() {
        return targets;
    }

    public boolean isIntRepeatCount() {
        return intRepeatCount;
    }

    public boolean isTimeToStop() {
        return timeToStop;
    }

    public boolean isHasBegun() {
        return hasBegun;
    }

    public RepeatBehavior getRepeatBehavior() {
        return repeatBehavior;
    }

    public EndBehavior getEndBehavior() {
        return endBehavior;
    }

    public Direction getDirection() {
        return direction;
    }

    //setter


    public void setIntRepeatCount(boolean intRepeatCount) {
        this.intRepeatCount = intRepeatCount;
    }

    public void setDirection(Direction direction) {
        throwExceptionIfRunning();
        this.direction = direction;
    }

    public void setInterpolator(Interpolator interpolator) {
        throwExceptionIfRunning();
        this.interpolator = interpolator;
    }

    public void setAcceleration(double acceleration) {
        if (!(acceleration < 0.0F) && !(acceleration > 1.0F)) {
            if (acceleration > 1 - this.deceleration)
                throw new IllegalStateException("参数异常");
            else
                this.acceleration = acceleration;
            return;
        }
        throw new IllegalStateException("xxx");
    }

    public void setDeceleration(double deceleration) {
        if (!(deceleration < 0.0F) && !(deceleration > 1.0F)) {
            if (deceleration > 1 - this.acceleration)
                throw new IllegalStateException("参数异常");
            else
                this.deceleration = deceleration;
            return;
        }
        throw new IllegalStateException("xxx");
    }

    public void setResolution(int resolution) {
        if (resolution < 0) {
            throw new IllegalArgumentException("resolution must be >= 0");
        } else {
            this.throwExceptionIfRunning();
            this.resolution = resolution;
            this.swingTimer.setResolution(resolution);
        }
    }

    public void setDuration(long duration) {
        throwExceptionIfRunning();
        this.duration = duration;
    }

    public void setRepeatCount(double repeatCount) {
        validateRepeatCount(repeatCount);
        throwExceptionIfRunning();
        this.repeatCount = repeatCount;
    }

    public void setStartDelay(int startDelay) {
        if (startDelay < 0) {
            throw new IllegalArgumentException("startDelay (" + startDelay + ") cannot be < 0");
        } else {
            this.throwExceptionIfRunning();
            this.startDelay = startDelay;
            this.swingTimer.setStartDelay(startDelay);
        }
    }

    public void setRepeatBehavior(RepeatBehavior repeatBehavior) {
        this.throwExceptionIfRunning();
        this.repeatBehavior = repeatBehavior != null ? repeatBehavior : Animator.RepeatBehavior.REVERSE;
    }

    public void setEndBehavior(EndBehavior endBehavior) {
        this.throwExceptionIfRunning();
        this.endBehavior = endBehavior;
    }

    public void setStartFraction(double startFraction) {
        if (!(startFraction < 0.0F) && !(startFraction > 1.0F)) {
            this.throwExceptionIfRunning();
            this.startFraction = startFraction;
        } else {
            throw new IllegalArgumentException("initialFraction must be between 0 and 1");
        }
    }

    public void setSwingTimer(TimingSource swingTimer) {
        throwExceptionIfRunning();
        if (swingTimer == null)
            return;
        this.swingTimer = swingTimer;
        if (this.timingSourceTarget == null)
            timingSourceTarget = new TimingSourceTarget();
        this.swingTimer.addEventListener(timingSourceTarget);

        this.swingTimer.setStartDelay(this.startDelay);
        this.swingTimer.setResolution(this.resolution);
    }

    class SwingTimingSource extends TimingSource {

        Timer timer;

        public SwingTimingSource() {
            timer = new Timer(Animator.this.resolution,Animator.this.new TimerTarget());
            //设置初始为0
            this.timer.setInitialDelay(0);
        }

        @Override
        public void start() {
            timer.start();
        }

        @Override
        public void stop() {
            timer.stop();
        }

        @Override
        public void setResolution(int resolution) {
            timer.setDelay(resolution);
        }

        @Override
        public void setStartDelay(int startDelay) {
            timer.setInitialDelay(startDelay);
        }
    }

    public static enum EndBehavior {
        HOLD,
        RESET;

        EndBehavior() {
        }
    }

    public static enum Direction {
        FORWARD,
        BACKWARD;

        Direction() {
        }
    }

    public static enum RepeatBehavior {
        LOOP,
        REVERSE;

        RepeatBehavior() {
        }
    }

    //内置定时任务，调用框架
    class TimerTarget implements ActionListener {
        public TimerTarget() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Animator.this.timingEvent(Animator.this.getTimingFraction());
        }
    }

    class TimingSourceTarget implements TimingEventListener {
        public TimingSourceTarget() {
        }

        @Override
        public void timingSourceEvent(TimingSource timingSource) {
            if (Animator.this.swingTimer == timingSource && Animator.this.running) {
                //如果定时器正在运行
                Animator.this.timingEvent(Animator.this.getTimingFraction());
            }
        }
    }

}
