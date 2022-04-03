package com.baozi.demo.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.baozi.demo.R;
import com.baozi.demo.item.sort.IndexBar;
import com.baozi.demo.item.sort.SortGroupItem;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.widget.TreeSortAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baozi on 2017/8/19.
 * 侧滑+索引
 */

public class SortAt extends AppCompatActivity {
    private static final String[] LETTERS = new String[]{"A", "B", "C", "D",
            "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private TreeSortAdapter mTreeSortAdapter = new TreeSortAdapter();
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        final RecyclerView rv_content = findViewById(R.id.rv_content);
        TextView tv_index = findViewById(R.id.tv_index);
        IndexBar qb_sort = findViewById(R.id.qb_sort);
        qb_sort.setOnIndexChangedListener(letter -> {
            int sortIndex = mTreeSortAdapter.getSortIndex(letter);
            mLinearLayoutManager.scrollToPositionWithOffset(sortIndex, 0);
        });
        qb_sort.setIndexs(LETTERS);
        qb_sort.setSelectedIndexTextView(tv_index);

        mLinearLayoutManager = new LinearLayoutManager(this);
        rv_content.setLayoutManager(mLinearLayoutManager);
        rv_content.setAdapter(mTreeSortAdapter);
        initData();
    }


    private void initData() {
        List<TreeItem> groupItems = new ArrayList<>();
        for (String str : LETTERS) {
            SortGroupItem sortGroupItem = new SortGroupItem();
            sortGroupItem.setSortKey(str);
            sortGroupItem.setData(null);
            groupItems.add(sortGroupItem);
        }
        mTreeSortAdapter.getItemManager().replaceAllItem(groupItems);
    }

}
