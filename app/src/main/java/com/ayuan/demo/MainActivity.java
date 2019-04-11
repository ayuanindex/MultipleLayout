package com.ayuan.demo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayuan.demo.bean.MessageBean;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private ListView lv_list;
    private EditText et_message;
    private TextView tv_send;
    private TranslateAnimation topAnim;
    private ArrayList<MessageBean> messageBeans;
    private MyAdapter myAdapter;
    private boolean flag = false;
    private static int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        lv_list = (ListView) findViewById(R.id.lv_list);
        et_message = (EditText) findViewById(R.id.et_message);
        tv_send = (TextView) findViewById(R.id.tv_send);
    }

    private void initData() {
        messageBeans = new ArrayList<>();
        myAdapter = new MyAdapter();
        lv_list.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        lv_list.setSelection(lv_list.getCount());
        topAnim = new TranslateAnimation(0, 0, 0, 0);
    }

    private void initListener() {
        lv_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                count++;
                if (count - visibleItemCount >= 9) {
                    flag = true;
                    count = 0;
                }
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = et_message.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    App.showToast("不可以发送空消息");
                    return;
                }
                et_message.setText("");
                lv_list.startAnimation(topAnim);
                MessageBean messageBean = new MessageBean();
                messageBean.setMessage(message);
                messageBean.setPosition("right");
                messageBeans.add(messageBean);
                myAdapter.notifyDataSetChanged();
                /*lv_list.setSelection(lv_list.getCount() - 1);*/
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(500);
                            MessageBean e = new MessageBean();
                            e.setPosition("");
                            e.setMessage("哈哈啊哈");
                            messageBeans.add(e);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lv_list.startAnimation(topAnim);
                                    myAdapter.notifyDataSetChanged();
                                    lv_list.setSelection(lv_list.getCount() - 1);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private TextView text;

        @Override
        public int getCount() {
            return messageBeans.size();
        }

        @Override
        public MessageBean getItem(int position) {
            return messageBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String weizhi = getItem(position).getPosition();
            View view;
            if (convertView == null) {
                if (weizhi.equals("right")) {
                    view = View.inflate(MainActivity.this, R.layout.item_left, null);
                    view.setTag("left");
                } else {
                    view = View.inflate(MainActivity.this, R.layout.item_right, null);
                    view.setTag("right");
                }
            } else {
                String tag = (String) convertView.getTag();
                if (weizhi.equals("right")) {
                    if (!tag.equals("left")) {
                        view = View.inflate(MainActivity.this, R.layout.item_left, null);
                        view.setTag("left");
                    } else {
                        view = convertView;
                    }
                } else {
                    if (!tag.equals("right")) {
                        view = View.inflate(MainActivity.this, R.layout.item_right, null);
                        view.setTag("right");
                    } else {
                        view = convertView;
                    }
                }
            }
            if (view.getTag().equals("left")) {
                initLeftView(view);
                text.setText(getItem(position).getMessage());
            } else if (view.getTag().equals("right")) {
                initRightView(view);
                text.setText(getItem(position).getMessage());
            }
            if (flag) {
                topAnim = new TranslateAnimation(0, 0, view.getHeight() + 10, 0);
                topAnim.setDuration(300);
            }
            return view;
        }

        private void initRightView(View view) {
            text = (TextView) view.findViewById(R.id.text);
        }

        private void initLeftView(View view) {
            text = (TextView) view.findViewById(R.id.text);
        }
    }

}
