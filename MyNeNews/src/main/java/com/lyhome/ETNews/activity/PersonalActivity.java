package com.lyhome.ETNews.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lyhome.ETNews.R;
import com.lyhome.ETNews.bean.User;
import com.lyhome.ETNews.biz.UserProxy;
import com.lyhome.ETNews.view.ChooseDialog;
import com.lyhome.ETNews.view.ConfirmDialog;
import com.lyhome.ETNews.view.EditTextDialog;
import com.lyhome.ETNews.view.LoadingDialog;

import static com.lyhome.ETNews.R.id.tv_nickname;
import static com.lyhome.ETNews.R.id.tv_sex;

/**
 * Created by Iamasoldier6 on 2015/11/25.
 */

public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvUsername;

    private Button mBtnNickname;
    private Button mBtnSex;
    private Button mBtnSignature;
    private Button mBtnLogout;

    private TextView mTvNickname;
    private TextView mTvSex;
    
    private LoadingDialog mLoadingDialog;
    public User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initViews();

        mUser = UserProxy.getCurrentUser(this);
        if (mUser != null) {
            mTvUsername.setText(mUser.getUsername());
        }

        setListener();
    }

    private void setListener() {
        mBtnNickname.setOnClickListener(this);
        mBtnSex.setOnClickListener(this);
        mBtnSignature.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
    }

    private void initViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("个人信息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLoadingDialog = new LoadingDialog();

        mTvUsername = (TextView) findViewById(R.id.username);
        mTvNickname = (TextView) findViewById(tv_nickname);
        mTvSex = (TextView) findViewById(tv_sex);
        mBtnNickname = (Button) findViewById(R.id.btn_nickname);
        mBtnSex = (Button) findViewById(R.id.btn_sex);
        mBtnSignature = (Button) findViewById(R.id.btn_signature);
        mBtnLogout = (Button) findViewById(R.id.btn_logout);
    }

    @Override
    public void onClick(View v) {

        EditTextDialog editTextDialog = new EditTextDialog();

        int id = v.getId();
        if (id == R.id.btn_nickname) {
            editTextDialog.setParams(mTvNickname.getText().toString(), true, 20);
            editTextDialog.setMyOnClickListener(new EditTextDialog.MyOnClickListener() {
                @Override
                public void onClick(String str) {
                    if (str == null) return;
                    mLoadingDialog.show(getFragmentManager(), "loading");
                    UserProxy.upDataInfo(PersonalActivity.this, mUser, str, null, null, new UserProxy.UpdataInfo() {
                        @Override
                        public void onSuccess() {
                            mLoadingDialog.dismiss();
                            Toast.makeText(PersonalActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                            mTvNickname.setText(mUser.getNickName());
                        }

                        @Override
                        public void onFailure(String msg) {
                            mLoadingDialog.dismiss();
                            Toast.makeText(PersonalActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            editTextDialog.show(getFragmentManager(), "set_nickname_list_dialog");
        } else if (id == R.id.btn_sex) {
            final String[] str = {"男", "女"};
            ChooseDialog chooseDialog = new ChooseDialog();
            chooseDialog.setParams(null, str, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mLoadingDialog.show(getFragmentManager(), "loading");
                    UserProxy.upDataInfo(PersonalActivity.this, mUser, null, str[which], null, new UserProxy.UpdataInfo() {
                        @Override
                        public void onSuccess() {
                            mLoadingDialog.dismiss();
                            mTvSex.setText(mUser.getSex());
                            Toast.makeText(PersonalActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String msg) {
                            mLoadingDialog.dismiss();
                            Toast.makeText(PersonalActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            chooseDialog.show(getFragmentManager(), "choose");
        } else if (id == R.id.btn_signature) {
        } else if (id == R.id.btn_logout) {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setParams("确认退出登录吗？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UserProxy.logout(getApplicationContext());
                    PersonalActivity.this.finish();
                }
            }, null);
            confirmDialog.show(getFragmentManager(), "confirm");
        }
    }
}