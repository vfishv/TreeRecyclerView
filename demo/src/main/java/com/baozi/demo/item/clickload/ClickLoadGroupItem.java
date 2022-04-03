package com.baozi.demo.item.clickload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baozi.demo.R;
import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.item.TreeItemGroup;

import java.util.Arrays;
import java.util.List;

public class ClickLoadGroupItem extends TreeItemGroup<String[]> {
    @Override
    public int getLayoutId() {
        return R.layout.item_sort_group;
    }

    @Nullable
    @Override
    protected List<TreeItem> initChild(String[] data) {
        return ItemHelperFactory.createItems(Arrays.asList(data), ClickLoadChildItem.class, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {

    }
}
