package com.baozi.demo.item.tbhome;

import androidx.annotation.NonNull;

import com.baozi.demo.R;
import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.item.TreeItem;

/**
 * 模板B
 */
public class HomeItemA extends TreeItem {
    @Override
    public int getLayoutId() {
        return R.layout.item_tb_home_a;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {

    }

    @Override
    public int getSpanSize(int maxSpan) {
        return maxSpan / 2;
    }
}
