package com.yyxnb.module_wanandroid.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.common_base.base.BaseFragment;
import com.yyxnb.module_wanandroid.R;
import com.yyxnb.module_wanandroid.databinding.FragmentWanMainBinding;
import com.yyxnb.module_wanandroid.ui.home.WanHomeFragment;
import com.yyxnb.module_wanandroid.ui.project.WanProjectFragment;
import com.yyxnb.module_wanandroid.ui.publicnumber.WanPublicFragment;
import com.yyxnb.module_wanandroid.ui.tree.WanTreeFragment;
import com.yyxnb.view.tabbar.Tab;
import com.yyxnb.view.tabbar.TabBarView;

import java.util.ArrayList;
import java.util.List;

import static com.yyxnb.common_base.arouter.ARouterConstant.WAN_MAIN_FRAGMENT;

/**
 * 玩安卓 主页.
 */
@Route(path = WAN_MAIN_FRAGMENT)
@BindRes
public class WanMainFragment extends BaseFragment {


    private FragmentWanMainBinding binding;
    private ArrayList<Fragment> fragments;
    private List<Tab> tabs;

    private TabBarView mTabLayout;
    private int currentIndex;
    private boolean isAdded;

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_wan_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = getBinding();

        mTabLayout = binding.mTabLayout;

    }

    @Override
    public void initViewData() {

        if (fragments == null) {
            fragments = new ArrayList<>();
            fragments.add(new WanHomeFragment());
            fragments.add(new WanTreeFragment());
            fragments.add(new WanProjectFragment());
            fragments.add(new WanPublicFragment());

            tabs = new ArrayList<>();
            tabs.add(new Tab(getContext(), "首页", R.mipmap.ic_titlebar_progress));
            tabs.add(new Tab(getContext(), "分类", R.mipmap.ic_titlebar_progress));
            tabs.add(new Tab(getContext(), "项目", R.mipmap.ic_titlebar_progress));
            tabs.add(new Tab(getContext(), "公众号", R.mipmap.ic_titlebar_progress));
        }

        mTabLayout.setTab(tabs);

        mTabLayout.setOnSelectListener((v, position, text) -> changeView(position));

        changeView(0);
    }

    //设置Fragment页面
    private void changeView(int index) {

        if (currentIndex == index && isAdded) {
            //重复点击
            return;
        }
        isAdded = true;
        //开启事务
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        //隐藏当前Fragment
        ft.hide(fragments.get(currentIndex));
        //判断Fragment是否已经添加
        if (!fragments.get(index).isAdded()) {
            ft.add(R.id.fragment_content_view, fragments.get(index)).show(fragments.get(index));
        } else {
            //显示新的Fragment
            ft.show(fragments.get(index));
        }
        ft.commitAllowingStateLoss();
        currentIndex = index;
    }
}