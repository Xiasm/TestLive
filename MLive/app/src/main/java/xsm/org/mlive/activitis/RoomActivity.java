package xsm.org.mlive.activitis;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.recorder.api.LiveSession;
import com.baidu.recorder.api.LiveSessionHW;
import com.baidu.recorder.api.LiveSessionSW;
import com.baidu.recorder.api.SessionStateListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import xsm.org.mlive.R;
import xsm.org.mlive.messages.RoomStateMessage;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isSessionReady = false;
    private boolean isSessionStarted = false;
    private boolean isConnecting = false;

    private SessionStateListener mStateListener;
    private Handler mUIEventHandler;
    private static final String TAG = "RoomActivity";
    @BindView(R.id.camera_surface) SurfaceView mSurfaceView;
    @BindView(R.id.room_edit_title) EditText mEditTitle;
    @BindView(R.id.room_start) TextView mStart;
    @BindView(R.id.room_stop) Button mStop;
    @BindView(R.id.btn_camera_switch) Button mCameraSwitch;
    @BindView(R.id.btn_flash_switch) Button mFlashSwitch;

    @BindView(R.id.room_share_layout)
    LinearLayout mShareLayout;
    @BindView(R.id.room_share_qq)
    ImageView shareQQ;
    @BindView(R.id.room_share_qzone)
    ImageView shareQZone;
    @BindView(R.id.room_share_weixin)
    ImageView shareWeiXin;
    @BindView(R.id.room_share_weixinzone)
    ImageView shareWeiXinZone;
    @BindView(R.id.room_share_weibo)
    ImageView shareWeiBo;
    private LiveSession mLiveSession;
    private int mCurrentCamera = -1;
    private boolean isFlashOn = false;

    private String shareStr;

    private int mVideoWidth = 1280; //设置推流视频宽度, 需传入长的一边
    private int mVideoHeight = 720; // 设置推流视频高度，需传入短的一边
    private int mFrameRate = 15;    // 设置视频帧率
    private int mBitrate = 2048000; // 设置视频码率，单位为bit per seconds
    private String mStreamingUrl = "rtmp://push.bcelive.com/live/2rds0k1c3dipjjxy2m"; // TODO:: Replace it with your streaming url.
    private RoomStateMessage mRoomStateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_room);
        ButterKnife.bind(this);
        mCurrentCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
        initUIEventHandler();
        initUIOnclick();
        initStateListener();
        //初始化LiveSession
        initLiveSession(mSurfaceView.getHolder());
        mRoomStateMessage = new RoomStateMessage();
    }

    private void initUIOnclick() {
        mStart.setText("正在初始化中..");
        mStart.setOnClickListener(this);
        mStop.setOnClickListener(this);
        mCameraSwitch.setOnClickListener(this);
        mFlashSwitch.setOnClickListener(this);

        shareQQ.setOnClickListener(this);
        shareQZone.setOnClickListener(this);
        shareWeiXin.setOnClickListener(this);
        shareWeiXinZone.setOnClickListener(this);
        shareWeiBo.setOnClickListener(this);
    }

    private void initUIEventHandler() {
        mUIEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:     //LiveSession准备好了
                        isSessionReady = true;
                        mStart.setText("开始直播");
                        break;
                    case 6:     //LiveSessionStart调用 开始连接
                        isConnecting = true;
                        mStart.setText("正在连接中");
                        mRoomStateMessage.setRoomTitle(mEditTitle.getText().toString());
                        mEditTitle.setVisibility(View.GONE);
                        mShareLayout.setVisibility(View.GONE);
                        logdState();
                        break;
                    case 1:     //当前摄像头不支持所选分辨率
                        String hint = String.format("注意：当前摄像头不支持您所选择的分辨率\n实际分辨率为%dx%d",
                                mVideoWidth, mVideoHeight);
                        Toast.makeText(RoomActivity.this, hint, Toast.LENGTH_LONG).show();
                        fitPreviewToParentByResolution(mSurfaceView.getHolder(), mVideoWidth, mVideoHeight);
                        break;
                    case 2:     //LiveSessionStart完成 读流开始
                        Log.d(TAG, "读流开始！");
                        isSessionStarted = true;
                        isConnecting = false;
                        mStart.setVisibility(View.GONE);
                        mStop.setVisibility(View.VISIBLE);
                        mCameraSwitch.setVisibility(View.VISIBLE);
                        mFlashSwitch.setVisibility(View.VISIBLE);
                        break;
                    case 3:     //读流停止 关闭直播
                        Log.d(TAG, "关闭流成功！");
                        isSessionStarted = false;
                        isConnecting = false;
                        finish();
                        break;
                    case 4:     //显示Toast
                        String text = (String) msg.obj;
                        Toast.makeText(RoomActivity.this, text, Toast.LENGTH_SHORT).show();
                        break;
                    case 5:     //重新推流
                        Log.d(TAG, "正在尝试重新连接推流服务器");
                        if (isSessionReady) {
                            mLiveSession.startRtmpSession(mStreamingUrl);
                        }
                        mUIEventHandler.sendEmptyMessage(6);
                        break;
                    case 7:     //重新开启流
                        if (!isConnecting) {
                            Log.d(TAG, "重新开启流中！");
                            isConnecting = true;
                            if (isSessionReady) {
                                mLiveSession.stopRtmpSession();
                            }
                            mUIEventHandler.sendEmptyMessage(6);
                        }
                        break;
                }
            }
        };
    }

    private void logdState() {
        Log.d(TAG, mRoomStateMessage.toString());
    }

    private void initStateListener() {
        mStateListener = new SessionStateListener() {
            //推流状态变化事件回调
            @Override
            public void onSessionPrepared(int i) {
                if (i == SessionStateListener.RESULT_CODE_OF_OPERATION_SUCCEEDED) {
                    if (mUIEventHandler != null) {
                        mUIEventHandler.sendEmptyMessage(0);
                    }
                    int realWidth = mLiveSession.getAdaptedVideoWidth();
                    int realHeight = mLiveSession.getAdaptedVideoHeight();
                    if (realHeight != mVideoHeight || realWidth != mVideoWidth) {
                        mVideoHeight = realHeight;
                        mVideoWidth = realWidth;
                        mUIEventHandler.sendEmptyMessage(1);
                    }
                }
            }

            @Override
            public void onSessionStarted(int i) {
                if (i == SessionStateListener.RESULT_CODE_OF_OPERATION_SUCCEEDED) {
                    if (mUIEventHandler != null) {
                        mUIEventHandler.sendEmptyMessage(2);
                        //美颜效果
                        mLiveSession.enableDefaultBeautyEffect(true);
                    }
                } else {
                    Log.d(TAG, "开启推流失败！");
                }
            }

            @Override
            public void onSessionStopped(int i) {
                if (i == SessionStateListener.RESULT_CODE_OF_OPERATION_SUCCEEDED) {
                    if (mUIEventHandler != null) {
                        mUIEventHandler.sendEmptyMessage(3);
                    }
                    mLiveSession.enableDefaultBeautyEffect(false);
                } else {
                    Log.d(TAG, "关闭直播失败！");
                }
            }

            @Override
            public void onSessionError(int i) {
                switch (i) {
                    case SessionStateListener.ERROR_CODE_OF_OPEN_MIC_FAILED:
                        Log.d(TAG, "MIC设备无法打开!");
                        onOpenDeviceFailed();
                        break;
                    case SessionStateListener.ERROR_CODE_OF_OPEN_CAMERA_FAILED:
                        Log.e(TAG, "相机设备无法打开!");
                        onOpenDeviceFailed();
                        break;
                    case SessionStateListener.ERROR_CODE_OF_PREPARE_SESSION_FAILED:
                        Log.e(TAG, "Error occurred while preparing recorder!");
                        onPrepareFailed();
                        break;
                    case SessionStateListener.ERROR_CODE_OF_CONNECT_TO_SERVER_FAILED:
                        Log.e(TAG, "startRtmpSession 接口调用失败，原因通常为连接不上推流服务器!");
                        if (mUIEventHandler != null) {
                            Message msg = mUIEventHandler.obtainMessage(4);
                            msg.obj = "连接推流服务器失败，正在重试！";
                            mUIEventHandler.sendMessage(msg);
                            mUIEventHandler.sendEmptyMessageDelayed(5, 2000);
                        }
                        break;
                    case SessionStateListener.ERROR_CODE_OF_DISCONNECT_FROM_SERVER_FAILED:
                        Log.e(TAG, "stopRtmpSession 接口调用失败，原因通常是网络异常!");
                        isConnecting = false;
                        // Although we can not stop session successfully, we still need to take it as stopped
                        if (mUIEventHandler != null) {
                            mUIEventHandler.sendEmptyMessage(3);
                        }
                        break;
                    default:
                        onStreamingError(i);
                        break;
                }
            }
        };
    }

    private void initLiveSession(SurfaceHolder sh) {
        Log.d(TAG, "Calling intiLiveSession: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            mLiveSession = new LiveSessionHW(this, mVideoWidth, mVideoHeight, mFrameRate, mBitrate, mCurrentCamera);
        else
            mLiveSession = new LiveSessionSW(this, mVideoWidth, mVideoHeight, mFrameRate, mBitrate, mCurrentCamera);
        mLiveSession.setStateListener(mStateListener);
        //设置预览View
        mLiveSession.bindPreviewDisplay(mSurfaceView.getHolder());
        mLiveSession.prepareSessionAsync();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.room_start:
                start();
                break;
            case R.id.room_stop:
                stop();
                break;
            case R.id.btn_camera_switch:
                cameraSwitch();
                break;
            case R.id.btn_flash_switch:
                flashSwitch();
                break;
            case R.id.room_share_qq:
                if (mRoomStateMessage.isQQShare()) {
                    mRoomStateMessage.setQQShare(!mRoomStateMessage.isQQShare());
                    shareStr = "qq分享已关闭";
                    sendToToast();
                }else {
                    mRoomStateMessage.setQQShare(!mRoomStateMessage.isQQShare());
                    shareStr = "qq分享已打开";
                    sendToToast();
                }
                break;
            case R.id.room_share_qzone:
                if (mRoomStateMessage.isQZoneShare()) {
                    mRoomStateMessage.setQZoneShare(!mRoomStateMessage.isQZoneShare());
                    shareStr = "qq空间分享已关闭";
                    sendToToast();
                }else {
                    mRoomStateMessage.setQZoneShare(!mRoomStateMessage.isQZoneShare());
                    shareStr = "qq空间分享已打开";
                    sendToToast();
                }
                break;
            case R.id.room_share_weixin:
                if (mRoomStateMessage.isWeiXinShare()) {
                    mRoomStateMessage.setWeiXinShare(!mRoomStateMessage.isWeiXinShare());
                    shareStr = "微信分享已关闭";
                    sendToToast();
                }else {
                    mRoomStateMessage.setWeiXinShare(!mRoomStateMessage.isWeiXinShare());
                    shareStr = "微信分享已打开";
                    sendToToast();
                }
                break;
            case R.id.room_share_weixinzone:
                if (mRoomStateMessage.isWeiXinQzone()) {
                    mRoomStateMessage.setWeiXinQzone(!mRoomStateMessage.isWeiXinQzone());
                    shareStr = "朋友圈分享已关闭";
                    sendToToast();
                }else {
                    mRoomStateMessage.setWeiXinQzone(!mRoomStateMessage.isWeiXinQzone());
                    shareStr = "朋友圈分享已打开";
                    sendToToast();
                }
                break;
            case R.id.room_share_weibo:
                if (mRoomStateMessage.isSinaWeiBoShare()) {
                    mRoomStateMessage.setSinaWeiBoShare(!mRoomStateMessage.isSinaWeiBoShare());
                    shareStr = "微博分享已关闭";
                    sendToToast();
                }else {
                    mRoomStateMessage.setSinaWeiBoShare(!mRoomStateMessage.isSinaWeiBoShare());
                    shareStr = "微博分享已打开";
                    sendToToast();
                }
                break;
        }
    }

    private void sendToToast() {
        Message message = mUIEventHandler.obtainMessage(4);
        message.obj = shareStr;
        mUIEventHandler.sendMessage(message);
    }

    private void stop() {
        if (isSessionStarted && !isConnecting) {
            if (mLiveSession.stopRtmpSession()) {
                mUIEventHandler.sendEmptyMessage(8);
            }
        }
    }

    private void flashSwitch() {
        mFlashSwitch.setEnabled(false);
        if (mCurrentCamera == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mLiveSession.toggleFlash(!isFlashOn);
            isFlashOn = !isFlashOn;
            if (isFlashOn) {
                mFlashSwitch.setBackgroundResource(R.drawable.flash_on);
            } else {
                mFlashSwitch.setBackgroundResource(R.drawable.flash_off);
            }
        } else {
            Toast.makeText(this, "抱歉！前置摄像头不支持切换闪光灯！", Toast.LENGTH_SHORT).show();
        }
        mFlashSwitch.setEnabled(true);
    }

    private void cameraSwitch() {
        mCameraSwitch.setEnabled(false);
        if (mLiveSession.canSwitchCamera()) {
            if (mCurrentCamera == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCurrentCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
                mLiveSession.switchCamera(mCurrentCamera);
            } else {
                mCurrentCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
                mLiveSession.switchCamera(mCurrentCamera);
            }
            isFlashOn = false;
            mFlashSwitch.setBackgroundResource(R.drawable.flash_off);
        } else {
            Toast.makeText(this, "抱歉！该分辨率下不支持切换摄像头！", Toast.LENGTH_SHORT).show();
        }
        mCameraSwitch.setEnabled(true);
    }

    private void start() {
        if (!isSessionReady)
            return;
        if (!isSessionStarted && !TextUtils.isEmpty(mStreamingUrl)) {
            //session没有开始并且url不为null
            if (mLiveSession.startRtmpSession(mStreamingUrl)) {
                mUIEventHandler.sendEmptyMessage(6);
            }
        }
    }

    int mWeakConnectionHintCount = 0;
    private void onStreamingError(int i) {
        Message msg = mUIEventHandler.obtainMessage(4);
        switch (i) {
            case SessionStateListener.ERROR_CODE_OF_PACKET_REFUSED_BY_SERVER:
            case SessionStateListener.ERROR_CODE_OF_SERVER_INTERNAL_ERROR:
                msg.obj = "因服务器异常，当前直播已经中断！正在尝试重新推流...";
                if (mUIEventHandler != null) {
                    mUIEventHandler.sendMessage(msg);
                    mUIEventHandler.sendEmptyMessage(7);
                }
                break;
            case SessionStateListener.ERROR_CODE_OF_WEAK_CONNECTION:
                Log.i(TAG, "Weak connection...");
                msg.obj = "当前网络不稳定，请检查网络信号！";
                mWeakConnectionHintCount++;
                if (mUIEventHandler != null) {
                    mUIEventHandler.sendMessage(msg);
                    if (mWeakConnectionHintCount >= 5) {
                        mWeakConnectionHintCount = 0;
                        mUIEventHandler.sendEmptyMessage(7);
                    }
                }
                break;
            case SessionStateListener.ERROR_CODE_OF_CONNECTION_TIMEOUT:
                Log.i(TAG, "Timeout when streaming...");
                msg.obj = "连接超时，请检查当前网络是否畅通！我们正在努力重连...";
                if (mUIEventHandler != null) {
                    mUIEventHandler.sendMessage(msg);
                    mUIEventHandler.sendEmptyMessage(7);
                }
                break;
            default:
                Log.i(TAG, "Unknown error when streaming...");
                msg.obj = "未知错误，当前直播已经中断！正在重试！";
                mUIEventHandler.sendMessage(msg);
                if (mUIEventHandler != null) {
                    mUIEventHandler.sendMessage(msg);
                    mUIEventHandler.sendEmptyMessageDelayed(7, 1000);
                }
                break;
        }
    }

    private void onPrepareFailed() {
        //onSessionPrepared 接口调用失败，原因只能是 MIC 或相机打开失败
        isSessionReady = false;
    }

    private void onOpenDeviceFailed() {
        if (mUIEventHandler != null) {
            Message message = mUIEventHandler.obtainMessage(4);
            message.obj = "摄像头或MIC打开失败！请确认您已开启相关硬件使用权限！";
            mUIEventHandler.sendMessage(message);
        }
    }

    private void fitPreviewToParentByResolution(SurfaceHolder holder, int width, int height) {
        // Adjust the size of SurfaceView dynamically
        int screenHeight = getWindow().getDecorView().getRootView().getHeight();
        int screenWidth = getWindow().getDecorView().getRootView().getWidth();

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) { // If portrait, we should swap width and height
            width = width ^ height;
            height = width ^ height;
            width = width ^ height;
        }

        int adjustedVideoHeight = screenHeight;
        int adjustedVideoWidth = screenWidth;
        if (width * screenHeight > height * screenWidth) { // means width/height > screenWidth/screenHeight
            // Fit width
            adjustedVideoHeight = height * screenWidth / width;
            adjustedVideoWidth = screenWidth;
        } else {
            // Fit height
            adjustedVideoHeight = screenHeight;
            adjustedVideoWidth = width * screenHeight / height;
        }
        holder.setFixedSize(adjustedVideoWidth, adjustedVideoHeight);
    }

}
