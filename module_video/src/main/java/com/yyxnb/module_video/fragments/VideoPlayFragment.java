package com.yyxnb.module_video.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.L;
import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.arch.annotations.BindViewModel;
import com.yyxnb.arch.common.Bus;
import com.yyxnb.arch.common.MsgEvent;
import com.yyxnb.common.AppConfig;
import com.yyxnb.common.log.LogUtils;
import com.yyxnb.module_base.base.BaseFragment;
import com.yyxnb.module_video.R;
import com.yyxnb.module_video.adapter.TikTokAdapter;
import com.yyxnb.module_video.bean.TikTokBean;
import com.yyxnb.module_video.config.DataConfig;
import com.yyxnb.module_video.databinding.FragmentVideoPlayBinding;
import com.yyxnb.module_video.utils.cache.PreloadManager;
import com.yyxnb.module_video.utils.cache.ProxyVideoCacheManager;
import com.yyxnb.module_video.viewmodel.VideoViewModel;
import com.yyxnb.module_video.widget.TikTokController;
import com.yyxnb.module_video.widget.TikTokRenderViewFactory;
import com.yyxnb.module_video.widget.VerticalViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.yyxnb.module_base.config.Constants.KEY_VIDEO_BOTTOM_VP;
import static com.yyxnb.module_base.config.Constants.KEY_VIDEO_BOTTOM_VP_SWITCH;

/**
 * 短视频播放的fragment 可以上下滑动
 */
@BindRes
public class VideoPlayFragment extends BaseFragment {

    @BindViewModel
    VideoViewModel mViewModel;

    private FragmentVideoPlayBinding binding;
    private VerticalViewPager mViewPager;

    private VideoView mVideoView;
    private TikTokController mController;
    private PreloadManager mPreloadManager;
    private TikTokAdapter mAdapter;
    private List<TikTokBean> mVideoList = new ArrayList<>();
    private int mCurPos;
    private boolean isCur;

    public static VideoPlayFragment newInstance(int pos, List<TikTokBean> data) {

        Bundle args = new Bundle();
        args.putInt("CurPos", pos);
        args.putSerializable("data", (Serializable) data);
        VideoPlayFragment fragment = new VideoPlayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_video_play;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = getBinding();
        mViewPager = binding.mViewPager;

    }

    @Override
    public void initViewData() {
//        mCurPos = getArguments().getInt("CurPos",0);
//        mVideoList = (List<TikTokBean>) getArguments().getSerializable("data");
        initViewPager();
        initVideoView();
        mPreloadManager = PreloadManager.getInstance(getContext());

        addData(null);

        mViewPager.post(() -> startPlay(mCurPos));

        // 点赞、评论等交互
        mAdapter.setOnSelectListener((v, position, text) -> {
            switch (position) {
                case 0:
                    break;
                case 5:
                    Bus.post(new MsgEvent(KEY_VIDEO_BOTTOM_VP_SWITCH, 1));
                    break;
            }
            AppConfig.getInstance().toast(text);
        });
    }

    @Override
    public void initObservable() {
//        mViewModel.reqVideoList();

        mViewModel.result.observe(this, data -> {

        });
    }

    private void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setLooping(true);

        //以下只能二选一，看你的需求
        mVideoView.setRenderViewFactory(TikTokRenderViewFactory.create());
//        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);

        mController = new TikTokController(getActivity());
        mVideoView.setVideoController(mController);

    }

    private void initViewPager() {
//        mViewPager.setOffscreenPageLimit(4);
        mAdapter = new TikTokAdapter(mVideoList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            private int mCurItem;

            /**
             * VerticalViewPager是否反向滑动
             */
            private boolean mIsReverseScroll;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position == mCurItem) {
                    return;
                }
                mIsReverseScroll = position < mCurItem;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == mCurPos) {
                    return;
                }
                startPlay(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == VerticalViewPager.SCROLL_STATE_DRAGGING) {
                    mCurItem = mViewPager.getCurrentItem();
                }

                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    mPreloadManager.resumePreload(mCurPos, mIsReverseScroll);
                } else {
                    mPreloadManager.pausePreload(mCurPos, mIsReverseScroll);
                }
            }
        });
    }

    private void startPlay(int position) {
        int count = mViewPager.getChildCount();
        for (int i = 0; i < count; i++) {
            View itemView = mViewPager.getChildAt(i);
            TikTokAdapter.ViewHolder viewHolder = (TikTokAdapter.ViewHolder) itemView.getTag();
            if (viewHolder.mPosition == position) {
                mVideoView.release();
                removeViewFormParent(mVideoView);

                TikTokBean tiktokBean = mVideoList.get(position);
                String playUrl = mPreloadManager.getPlayUrl(tiktokBean.videoUrl);
                L.i("startPlay: " + "position: " + position + "  url: " + playUrl);
                mVideoView.setUrl(playUrl);
                mController.addControlComponent(viewHolder.mTikTokView, true);
                viewHolder.mPlayerContainer.addView(mVideoView, 0);
                mVideoView.start();
                mCurPos = position;
                break;
            }
        }
    }

    public void addData(View view) {
//        if (mCurPos == 0 && mVideoList == null){
        mVideoList.addAll(DataConfig.getTikTokBeans());
//        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 将View从父控件中移除
     */
    public void removeViewFormParent(View v) {
        if (v == null) {
            return;
        }
        ViewParent parent = v.getParent();
        if (parent instanceof FrameLayout) {
            ((FrameLayout) parent).removeView(v);
        }
    }

    @Override
    public void onVisible() {
        isCur = true;
        LogUtils.e("Play onVisible");
        Bus.post(new MsgEvent(KEY_VIDEO_BOTTOM_VP, false), 100);
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }

    @Override
    public void onInVisible() {
        isCur = false;
        LogUtils.e("Play onInVisible");
        Bus.post(new MsgEvent(KEY_VIDEO_BOTTOM_VP, true));
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCur = false;
        if (mVideoView != null) {
            mVideoView.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCur = false;
        if (mPreloadManager != null) {
            mPreloadManager.removeAllPreloadTask();
        }
        //清除缓存，实际使用可以不需要清除，这里为了方便测试
        ProxyVideoCacheManager.clearAllCache(getContext());
    }
}