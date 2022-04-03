package com.baozi.demo.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baozi.demo.R;
import com.baozi.demo.item.sort.IndexBar;
import com.baozi.demo.item.sort.SortGroupItem;
import com.baozi.treerecyclerview.adpater.wrapper.HeaderWrapper;
import com.baozi.treerecyclerview.adpater.wrapper.SwipeWrapper;
import com.baozi.treerecyclerview.adpater.wrapper.TreeLoadWrapper;
import com.baozi.treerecyclerview.item.SimpleTreeItem;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.widget.TreeSortAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baozi on 2017/8/19.
 * 在SortActivity的代码逻辑上,加少部分代码实现
 */

public class SwipeSortAt extends AppCompatActivity {
    private static final String[] LETTERS = new String[]{"A", "B", "C", "D",
            "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private TreeSortAdapter mTreeSortAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private TreeLoadWrapper mWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        RecyclerView rv_content = (RecyclerView) findViewById(R.id.rv_content);
        TextView tv_index = (TextView) findViewById(R.id.tv_index);
        IndexBar qb_sort = (IndexBar) findViewById(R.id.qb_sort);
        qb_sort.setOnIndexChangedListener(new IndexBar.OnIndexChangedListener() {
            @Override
            public void onIndexChanged(String letter) {
                int sortIndex = mTreeSortAdapter.getSortIndex(letter);
                mLinearLayoutManager.scrollToPositionWithOffset(sortIndex, 0);
            }
        });
        qb_sort.setIndexs(LETTERS);
        qb_sort.setSelectedIndexTextView(tv_index);

        mLinearLayoutManager = new GridLayoutManager(this, 2);
        rv_content.setLayoutManager(mLinearLayoutManager);
        //创建索引adapter
        mTreeSortAdapter = new TreeSortAdapter();
        mTreeSortAdapter.getItemManager().setOpenAnim(true);
        HeaderWrapper headerWrapper = new HeaderWrapper<>(mTreeSortAdapter);
        addHeadView(headerWrapper);
        //包装成侧滑删除列表
        SwipeWrapper adapter = new SwipeWrapper(headerWrapper);
        mWrapper = new TreeLoadWrapper(adapter);
        mWrapper.setEmptyView(new SimpleTreeItem(R.layout.layout_empty));
        mWrapper.setLoadingView(R.layout.layout_loading);

        rv_content.setAdapter(mWrapper);

        initData();
    }

    private void addHeadView(HeaderWrapper headerWrapper) {
        for (int i = 0; i < 5; i++) {
            //添加头部View1
            TextView headView = new TextView(this);
            headView.setText("headView" + i);
            headView.setGravity(Gravity.CENTER);
            headView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 320));
            headerWrapper.addHeaderView(headView);
        }
    }

    private void initData() {
        final List<TreeItem> groupItems = new ArrayList<>();
        for (int i = 0; i < LETTERS.length; i++) {
            SortGroupItem sortGroupItem = new SortGroupItem();
            sortGroupItem.setSortKey(LETTERS[i]);
            sortGroupItem.setData(i);
            groupItems.add(sortGroupItem);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWrapper.setType(TreeLoadWrapper.Type.REFRESH_OVER);
                        mWrapper.getItemManager().notifyDataChanged();
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWrapper.setType(TreeLoadWrapper.Type.LOADING);
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWrapper.setType(TreeLoadWrapper.Type.REFRESH_OVER);
                        mWrapper.getItemManager().replaceAllItem(groupItems);
                    }
                });
            }
        }).start();


    }
}
