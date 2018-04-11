package vn.com.hieptt149.workoutmanager.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by Administrator on 04/11/2018.
 */

public abstract class MyCountDownTimer {
    private final long mMillisInFuture;

    private final long mCountdownInterval;

    private long mStopTimeInFuture;

    private long mPauseTime;

    private boolean mCancelled = false;

    private boolean mPaused = false;

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    public final void cancel() {
        mHandler.removeMessages(MSG);
        mCancelled = true;
    }

    public synchronized final MyCountDownTimer start() {
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        mCancelled = false;
        mPaused = false;
        return this;
    }

    public long pause() {
        mPauseTime = mStopTimeInFuture - SystemClock.elapsedRealtime();
        mPaused = true;
        return mPauseTime;
    }

    public long resume() {
        mStopTimeInFuture = mPauseTime + SystemClock.elapsedRealtime();
        mPaused = false;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return mPauseTime;
    }

    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish();


    private static final int MSG = 1;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (MyCountDownTimer.this) {
                if (!mPaused) {
                    final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                    if (millisLeft <= 0) {
                        onFinish();
                    } else if (millisLeft < mCountdownInterval) {
                        sendMessageDelayed(obtainMessage(MSG), millisLeft);
                    } else {
                        long lastTickStart = SystemClock.elapsedRealtime();
                        onTick(millisLeft);

                        long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();

                        while (delay < 0) delay += mCountdownInterval;

                        if (!mCancelled) {
                            sendMessageDelayed(obtainMessage(MSG), delay);
                        }
                    }
                }
            }
        }
    };
}
