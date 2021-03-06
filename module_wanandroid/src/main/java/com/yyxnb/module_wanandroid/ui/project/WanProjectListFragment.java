package com.yyxnb.module_wanandroid.ui.project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.MultiItemTypeAdapter;
import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.arch.annotations.BindViewModel;
import com.yyxnb.common_base.base.BaseFragment;
import com.yyxnb.common_base.databinding.IncludeRlRvLayoutBinding;
import com.yyxnb.module_wanandroid.R;
import com.yyxnb.module_wanandroid.adapter.WanProjectAdapter;
import com.yyxnb.module_wanandroid.config.DataConfig;
import com.yyxnb.module_wanandroid.viewmodel.WanProjectViewModel;

/**
 * 项目 list数据.
 */
@BindRes(subPage = true)
public class WanProjectListFragment extends BaseFragment {

    private IncludeRlRvLayoutBinding binding;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    @BindViewModel
    WanProjectViewModel mViewModel;

    private WanProjectAdapter mAdapter;
    private int mPage;
    private int mId;
    private boolean isNew;

    public static WanProjectListFragment newInstance(boolean isNew, int id) {

        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putBoolean("isNew", isNew);
        WanProjectListFragment fragment = new WanProjectListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.include_rl_rv_layout;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = getBinding();
        mRefreshLayout = binding.mRefreshLayout;
        mRecyclerView = binding.mRecyclerView;

        if (getArguments() != null) {
            mId = getArguments().getInt("id", mId);
            isNew = getArguments().getBoolean("isNew", false);
        }

    }

    @Override
    public void initViewData() {
        mAdapter = new WanProjectAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder, int position) {
                super.onItemClick(view, holder, position);

            }
        });

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                if (isNew) {
                    mViewModel.getProjecNewData(mPage);
                } else {
                    mViewModel.getProjecDataByType(mPage + 1, mId);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 0;
                if (isNew) {
                    mViewModel.getProjecNewData(mPage);
                } else {
                    mViewModel.getProjecDataByType(mPage + 1, mId);
                }
            }
        });
    }

    @Override
    public void initObservable() {
        if (isNew) {
            mViewModel.getProjecNewData(mPage);
        } else {
            mViewModel.getProjecDataByType(mPage + 1, mId);
        }

        mViewModel.projecNewData.observe(this, data -> {
            mRefreshLayout.finishRefresh().finishLoadMore();
            if (data != null) {
                if (mPage == 0) {
                    mAdapter.setDataItems(data);
                } else {
                    mAdapter.addDataItem(data);
                }
                if (data.size() < DataConfig.DATA_SIZE) {
                    mRefreshLayout.finishRefreshWithNoMoreData();
                }
            }
        });

        mViewModel.projecDataByType.observe(this, data -> {
            mRefreshLayout.finishRefresh().finishLoadMore();
            if (data != null) {
                if (mPage == 1) {
                    mAdapter.setDataItems(data.datas);
                } else {
                    mAdapter.addDataItem(data.datas);
                }
                if (data.datas.size() < DataConfig.DATA_SIZE) {
                    mRefreshLayout.finishRefreshWithNoMoreData();
                }
            }
        });
    }
}