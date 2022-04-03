package com.baozi.demo.item.news;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baozi.demo.R;
import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.item.TreeItem;

/**
 * @author jlanglang  2017/7/5 17:15
 * @版本 2.0
 * @Change
 */

public class NewsImageItem extends TreeItem<String> {
    @Override
    public int getLayoutId() {
        return R.layout.item_news_image;
    }

    @Override
    public int getSpanSize(int maxSpan) {
        return maxSpan / 3;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, RecyclerView.LayoutParams layoutParams, int position) {
        super.getItemOffsets(outRect, layoutParams, position);
        outRect.set(1, 1, 1, 1);
    }
}
