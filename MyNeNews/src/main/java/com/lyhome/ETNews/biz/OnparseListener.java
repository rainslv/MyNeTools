package com.lyhome.ETNews.biz;

import com.lyhome.ETNews.bean.NewsItem;

import java.util.List;

/**
 * Created by lyhome on 2015/11/16.
 */
public interface OnparseListener {
    void onParseSuccess(List<NewsItem> list);

    void onParseFailed();
}
