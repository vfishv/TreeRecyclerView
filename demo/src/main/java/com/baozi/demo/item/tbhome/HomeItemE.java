package com.baozi.demo.item.tbhome;

import androidx.annotation.NonNull;

import com.baozi.demo.R;
import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.item.TreeItem;

/**
 * 模板E
 */
public class HomeItemE extends TreeItem {
    @Override
    public int getLayoutId() {
        return R.layout.item_tb_home_e;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {

    }

    @Override
    public int getSpanSize(int maxSpan) {
        return maxSpan/3;
    }
}
