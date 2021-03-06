package com.lyhome.ETNews.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lyhome.ETNews.MyApplication;
import com.lyhome.ETNews.activity.DetailActivity;
import com.lyhome.ETNews.adapter.MyRecyclerAdapter;
import com.lyhome.ETNews.bean.Constant;
import com.lyhome.ETNews.bean.NewsItem;
import com.lyhome.ETNews.biz.NewsBiz;
import com.lyhome.ETNews.biz.OnparseListener;
import com.lyhome.ETNews.util.ACache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyhome on 2015/11/22.
 */
public class JoyFragment extends Fragment {

    private static final String TAG = "Fragment_Joy";
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mAdapter;
    private Button reloadBtn;
    private Handler mHandler = new Handler();
    private ACache mACache;
    final List<NewsItem> itemList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mACache = ACache.get(getActivity());
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setListener();
        loadCache();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setViewVisible(true, true, false);
                loadData();
            }
        }, 1000);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(com.lyhome.ETNews.R.layout.fragment_joy, container, false);
    }

    private void loadCache() {
        if (covertToList()) {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void initView(View view) {
        // 下拉刷新
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(com.lyhome.ETNews.R.id.srl_content);
        mRefreshLayout.setColorSchemeResources(com.lyhome.ETNews.R.color.colorPrimary, com.lyhome.ETNews.R.color.secondColor, com.lyhome.ETNews.R.color.purple, com.lyhome.ETNews.R.color.blue);
        // 主要内容
        mRecyclerView = (RecyclerView) view.findViewById(com.lyhome.ETNews.R.id.recycler_content);
        mAdapter = new MyRecyclerAdapter(getActivity(), itemList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Constant.NEWS_ITEM, itemList.get(position));
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        reloadBtn = (Button) view.findViewById(com.lyhome.ETNews.R.id.refresh_btn);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    public void loadData() {
        StringRequest stringrequest = new StringRequest(Constant.JOY_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                NewsBiz.getFeed(response, new OnparseListener() {

                    @Override
                    public void onParseSuccess(List<NewsItem> list) {
                        if (list != null) {
                            itemList.clear();
                            itemList.addAll(list);
                            if (mAdapter != null) {
                                mAdapter.updateData(itemList);
                            }
                            setViewVisible(false, true, false);
                            mACache.put(TAG, convertToJson(itemList));
                        } else {

                        }

                    }

                    @Override
                    public void onParseFailed() {
                        setViewVisible(false, false, true);
                    }
                });
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (itemList == null) {
                    setViewVisible(false, false, true);
                } else {
                    setViewVisible(false, true, false);
                }
            }
        });
        stringrequest.setTag(TAG);
        MyApplication.getRequestQueue().add(stringrequest);

    }

    public void setViewVisible(boolean mSrl, boolean mRecyclerView, boolean reloadBtn) {
        if (mSrl) {
            if (this.mRefreshLayout != null) {
                this.mRefreshLayout.setRefreshing(true);
            }
        } else {
            if (this.mRefreshLayout != null) {
                this.mRefreshLayout.setRefreshing(false);
            }
        }
        if (mRecyclerView) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.setVisibility(View.GONE);
            }
        }
        if (reloadBtn) {
            if (this.reloadBtn != null) {
                this.reloadBtn.setVisibility(View.VISIBLE);
            }
        } else {
            if (this.reloadBtn != null) {
                this.reloadBtn.setVisibility(View.GONE);
            }
        }

    }

    public boolean covertToList() {
        JSONObject jsonObj = mACache.getAsJSONObject(TAG);
        if (jsonObj != null) {
            try {
                JSONArray feedlistAr = jsonObj.getJSONArray("feed_list");
                itemList.clear();
                for (int i = 0; i < feedlistAr.length(); i++) {
                    JSONObject feedlist = feedlistAr.getJSONObject(i);
                    NewsItem item = NewsItem.parse(feedlist);
                    itemList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private JSONObject convertToJson(List<NewsItem> item_list) {
        JSONObject outJsonObj = new JSONObject();
        JSONArray itemJsonAr = new JSONArray();
        for (NewsItem feedlistItem : item_list) {
            JSONObject jsonObj = feedlistItem.toJSONObj();
            itemJsonAr.put(jsonObj);
        }
        try {
            outJsonObj.put("feed_list", itemJsonAr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return outJsonObj;
    }
}
