package com.acsm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.acsm.MultiImageSelector.R;
import com.acsm.bean.Pics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoma on 2017/4/24.
 */

public class ShowBigImageActivity extends Activity {

    private RelativeLayout mBack;
    private TextView mPage;
    private Button mComplete;
    private ImageView mImgChoose;
    private ViewPager mViewPager;
    private ArrayList<String> picsList;
    private ArrayList<String> mFinalPics;
    private ArrayList<Pics> mDatas;
    private int desireImg;
    private int mPosition=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_big_img);
        Intent intent = getIntent();
        picsList= intent.getStringArrayListExtra("imgs");
        desireImg = intent.getIntExtra("desireImg", 9);
        initView();
        mFinalPics=new ArrayList<>();
        mDatas=new ArrayList<>();
        for (int i=0;i<picsList.size();i++){
            mDatas.add(i,new Pics(picsList.get(i),true));
        }
    }

    private void initView() {
        mBack = (RelativeLayout) findViewById(R.id.iv_back);
        mPage = (TextView) findViewById(R.id.tv_page);
        mComplete = (Button) findViewById(R.id.btn_complete);
        mImgChoose = (ImageView) findViewById(R.id.img_choose);
        mViewPager = (ViewPager) findViewById(R.id.vp_viewpager);
        mComplete.setText("完成("+picsList.size()+"/"+desireImg+")");
        initViewPager();
        mPage.setText(1+"/"+picsList.size());
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(10); //因为跳转是需要返回值，所致该剧用来关闭预览页面
                finish();
            }
        });
        mComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<mDatas.size();i++){
                   if(mDatas.get(i).isHasChoose()){
                       mFinalPics.add(mDatas.get(i).getPic());
                   } 
                }
                if(mFinalPics != null && mFinalPics.size() >0){
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra("preview", mFinalPics);
                    setResult(35, data);
                    finish();
                }else {
                    Toast.makeText(ShowBigImageActivity.this,"没有选中的图片",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mImgChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pics pics = mDatas.get(mPosition);
                if (pics.isHasChoose()){
                    mImgChoose.setImageResource(R.mipmap.mis_btn_unselected);
                    mDatas.get(mPosition).setHasChoose(false);
                }else {
                    mImgChoose.setImageResource(R.mipmap.mis_btn_selecting);
                    mDatas.get(mPosition).setHasChoose(true);
                }
            }
        });
        
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }

            @Override
            public void onPageSelected(int position) {
                mPosition=position;
                mPage.setText((position+1)+"/"+picsList.size());
                if(mDatas.get(position).isHasChoose()){
                    mImgChoose.setImageResource(R.mipmap.mis_btn_selecting);
                }else {
                    mImgChoose.setImageResource(R.mipmap.mis_btn_unselected);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    List<View> views = new ArrayList<>();
    private void initViewPager() {
        for (int i=0;i<picsList.size();i++){
            View view =View.inflate(ShowBigImageActivity.this, R.layout.item_big_pics, null);
            ImageView mImg = (ImageView) view.findViewById(R.id.iv_pics);
            Glide.with(ShowBigImageActivity.this).load(picsList.get(i))
                    .into(mImg);
            views.add(view);
        }
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return picsList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager)container).addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager)container).removeView(views.get(position));
            }
        });
    }


    

    
    

}
