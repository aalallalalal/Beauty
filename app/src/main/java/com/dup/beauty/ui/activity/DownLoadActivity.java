package com.dup.beauty.ui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dup.beauty.R;
import com.dup.beauty.app.BaseActivity;
import com.dup.beauty.model.util.DownLoadUtil;
import com.dup.beauty.presenter.contract.IDownloadPresenter;
import com.dup.beauty.presenter.impl.DownloadPresenter;
import com.dup.beauty.ui.adapter.DownloadImagesAdapter;
import com.dup.beauty.ui.widget.ColorSwipeRefreshLayout;
import com.dup.beauty.util.FileUtil;
import com.dup.beauty.view.IDownloadView;
import com.dup.changeskin.SkinManager;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看下载图片 界面
 * <ul>
 * <li>{@link IDownloadPresenter}</li>
 * <li>{@link DownloadPresenter}</li>
 * <li>{@link IDownloadView}</li>
 * </ul>
 */
public class DownLoadActivity extends BaseActivity implements IDownloadView, DownloadImagesAdapter.OnItemClickListener {
    @BindView(R.id.download_recyclerview)
    public RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    public ColorSwipeRefreshLayout refreshLayout;

    private IDownloadPresenter mPresenter;

    private ArrayList<File> mData = new ArrayList<>();
    private DownloadImagesAdapter mDownloadImagesAdapter;

    @Override
    protected void bindPresenters() {
        super.bindPresenters();
        mPresenter = new DownloadPresenter(this, this);
    }

    @Override
    protected boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_down_load;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtil.setColor(this, SkinManager.getInstance().getResourceManager().getColor("status_bar_bg"),0);
        ButterKnife.bind(DownLoadActivity.this);
    }

    @Override
    protected void initData() {
        super.initData();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mDownloadImagesAdapter = new DownloadImagesAdapter(DownLoadActivity.this, mData);
        mDownloadImagesAdapter.setItemClickListener(this);
        recyclerView.setAdapter(mDownloadImagesAdapter);
        mPresenter.fetchDownloadImages();
    }

    @Override
    protected void initAction() {
        super.initAction();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.fetchDownloadImages();
            }
        });
    }

    @OnClick(R.id.toolbar_info)
    public void onInfoPress(View view) {
        new MaterialDialog.Builder(this).title(R.string.download_info)
                .content(FileUtil.getDownLoadFolder(this, DownLoadUtil.PIC_PATH).getAbsolutePath())
                .positiveText(R.string.done).show();
    }

    /**
     * 退回按钮点击事件
     */
    @OnClick(R.id.toolbar_back)
    public void onBackPress(View view) {
        finish();
    }

    /**
     * 获取到了下载列表数据
     *
     * @param list 下载的图片列表
     */
    @Override
    public void onFetchDownloadImages(ArrayList<File> list) {
        if (list != null) {
            mData.clear();
            mData.addAll(list);
            mDownloadImagesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position, String uri) {
        Intent intent = new Intent(this, DownloadPicturesActivity.class);
        intent.putExtra("DATA", mData);
        intent.putExtra("POSITION", position);
        startActivity(intent);
    }

    @Override
    public void onDataLoad(boolean isFinish) {
        refreshLayout.setRefreshing(!isFinish);
    }
}

