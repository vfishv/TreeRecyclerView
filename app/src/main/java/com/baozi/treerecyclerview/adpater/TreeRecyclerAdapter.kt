package com.baozi.treerecyclerview.adpater

import android.graphics.Canvas
import android.graphics.Rect
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View

import com.baozi.treerecyclerview.base.BaseRecyclerAdapter
import com.baozi.treerecyclerview.base.ViewHolder
import com.baozi.treerecyclerview.factory.ItemHelperFactory
import com.baozi.treerecyclerview.item.TreeItem
import com.baozi.treerecyclerview.item.TreeItemGroup
import com.baozi.treerecyclerview.manager.ItemManageImpl
import com.baozi.treerecyclerview.manager.ItemManager

import java.util.ArrayList

/**
 * Created by baozi on 2017/4/20.
 * 树级结构recycleradapter.
 * item之间有子父级关系,
 */

open class TreeRecyclerAdapter(var type: TreeRecyclerType = TreeRecyclerType.SHOW_DEFAULT) :
        BaseRecyclerAdapter<TreeItem<*>>() {

    override val itemManager: ItemManager<TreeItem<*>> by lazy {
        TreeItemManageImpl(this)
    }

    /**
     * 分割器
     */
    private val treeItemDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                    state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val i = itemCount
            if (itemCount == 0) {
                return
            }
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams
            val viewLayoutPosition = layoutParams.viewLayoutPosition

            val checkPosition = checkPosition(viewLayoutPosition)
            if (checkPosition < 0 || checkPosition >= i) {
                return
            }
            val data = getData(checkPosition)
            data?.getItemOffsets(outRect, view, parent, state, checkPosition)
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
            getData().forEach {
                if (it is RecyclerView.ItemDecoration) {
                    it.onDraw(c, parent, state)
                }
            }
        }

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDrawOver(c, parent, state)
            getData().forEach {
                if (it is RecyclerView.ItemDecoration) {
                    it.onDrawOver(c, parent, state)
                }
            }
        }
    }

    override fun onBindViewHolderClick(viewHolder: ViewHolder, view: View) {
        if (!view.hasOnClickListeners()) {
            view.setOnClickListener(View.OnClickListener {
                var layoutPosition = viewHolder.layoutPosition
                //获得处理后的position
                layoutPosition = checkPosition(layoutPosition)
                //拿到BaseItem
                val item = getData(layoutPosition) ?: return@OnClickListener
                val itemParentItem = item.parentItem
                //判断上一级是否需要拦截这次事件，只处理当前item的上级，不关心上上级如何处理.
                if (itemParentItem != null && itemParentItem.onInterceptClick(item)) {
                    return@OnClickListener
                }
                mOnItemClickListener?.invoke(viewHolder, layoutPosition) ?: item.onClick(viewHolder)
            })
        }
        view.setOnLongClickListener {
            //获得holder的position
            val layoutPosition = viewHolder.layoutPosition
            //检查position是否可以点击
            //检查并得到真实的position
            val itemPosition = checkPosition(layoutPosition)
            mOnItemLongClickListener?.invoke(viewHolder, itemPosition) ?: false
        }
    }

    override fun setData(data: List<TreeItem<*>>?) {
        if (null == data) {
            return
        }
        getData().clear()
        assembleItems(data)
    }

    fun setData(treeItemGroup: TreeItemGroup<*>?) {
        if (null == treeItemGroup) {
            return
        }
        val arrayList = ArrayList<TreeItem<*>>()
        arrayList.add(treeItemGroup)
        setData(arrayList)
    }

    /**
     * 对初始的一级items进行遍历,将每个item的childs拿出来,进行組合。
     *
     * @param items
     */
    private fun assembleItems(items: List<TreeItem<*>>) {
        if (type == TreeRecyclerType.SHOW_DEFAULT) {
            super.setData(items)
        } else {
            getData().addAll(ItemHelperFactory.getChildItemsWithType(items, type))
        }
    }

    override fun getLayoutId(position: Int): Int {
        val data = getData(position)
        return data?.getLayoutId() ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = getData(position) ?: return
        if (t is TreeItemGroup<*>) {
            t.isCanExpand = type !== TreeRecyclerType.SHOW_ALL
        }
        checkItemManage(t)
        t.onBindViewHolder(holder)
    }

    private fun checkItemManage(item: TreeItem<*>) {
        if (item.itemManager == null) {
            item.itemManager = itemManager
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.removeItemDecoration(treeItemDecoration)
        recyclerView.addItemDecoration(treeItemDecoration)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            val treeSpanSizeLookup = TreeSpanSizeLookup(this, spanCount)
            layoutManager.spanSizeLookup = treeSpanSizeLookup
        }
    }


    override fun getItemSpanSize(position: Int, maxSpan: Int): Int {
        val data = getData(position) ?: return maxSpan
        return data.getSpanSize(maxSpan)
    }

    private inner class TreeItemManageImpl(adapter: BaseRecyclerAdapter<TreeItem<*>>)
        : ItemManageImpl<TreeItem<*>>(adapter) {

        override fun addItem(t: TreeItem<*>) {
            if (t is TreeItemGroup<*>) {
                val childItemsWithType = ItemHelperFactory.getChildItemsWithType(t, type)
                childItemsWithType.add(0, t)
                super.addItems(childItemsWithType)
            } else {
                super.addItem(t)
            }
        }

        override fun addItems(items: List<TreeItem<*>>) {
            val childItemsWithType = ItemHelperFactory.getChildItemsWithType(items, type)
            super.addItems(childItemsWithType)
        }

        override fun addItems(position: Int, items: List<TreeItem<*>>) {
            val childItemsWithType = ItemHelperFactory.getChildItemsWithType(items, type)
            super.addItems(position, childItemsWithType)
        }

        override fun removeItem(item: TreeItem<*>) {
            if (item is TreeItemGroup<*>) {
                val childItemsWithType = ItemHelperFactory.getChildItemsWithType(item, type)
                childItemsWithType.add(0, item)
                super.removeItems(childItemsWithType)
            } else {
                super.removeItem(item)
            }
        }

        override fun removeItems(items: List<TreeItem<*>>) {
            val childItemsWithType = ItemHelperFactory.getChildItemsWithType(items, type)
            super.removeItems(childItemsWithType)
        }

    }

    class TreeSpanSizeLookup(private val adapter: BaseRecyclerAdapter<*>, private val spanCount: Int)
        : GridLayoutManager.SpanSizeLookup() {

        override fun getSpanSize(position: Int): Int {
            val i = adapter.itemCount
            if (i == 0) {
                return spanCount
            }
            val itemToDataPosition = adapter.itemManager.itemToDataPosition(position)
            if (itemToDataPosition < 0 || itemToDataPosition >= i) {
                return spanCount
            }
            val itemSpanSize = adapter.getItemSpanSize(itemToDataPosition, spanCount)//新版本传入总数
            return if (itemSpanSize == 0) {
                spanCount
            } else itemSpanSize
        }
    }
}
