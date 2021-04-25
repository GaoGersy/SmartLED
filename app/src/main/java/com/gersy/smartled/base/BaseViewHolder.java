package com.gersy.smartled.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.gersy.smartled.listener.OnItemClickListener;


/**
 * @作者 Gersy
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnItemClickListener mListener;
    protected int group = 0;//用于多itemType 时显示item的分组

    public BaseViewHolder(ViewGroup parent, int layoutResourceId) {
        this(parent, layoutResourceId, null);
    }

    public BaseViewHolder(ViewGroup parent, int layoutResourceId, OnItemClickListener listener) {
        super(createView(parent, layoutResourceId));
        mListener = listener;
        initView(itemView);
        itemView.setOnClickListener(this);
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    private static View createView(ViewGroup parent, int layoutResourceId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, parent, false);
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onItemClick(view, getAdapterPosition());
        }
    }

    protected <T extends View> T findView(int id) {
        return (T) itemView.findViewById(id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    protected abstract void initView(View itemView);

    public abstract void setData(T t, int position);
}