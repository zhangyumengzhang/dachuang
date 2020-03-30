package demo.doctor.com.doctor.shouye;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;

import demo.doctor.com.doctor.R;
import demo.doctor.com.doctor.login.ForgetPwdActivity;
import demo.doctor.com.doctor.shezhi.banben;
import demo.doctor.com.doctor.shezhi.bingli;
import demo.doctor.com.doctor.shezhi.informationActivity;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class setFragment extends Fragment implements View.OnClickListener {

    private ArrayList<ImageView> imageViewList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewHome = inflater.inflate(R.layout.setting, container, false);
        initView(viewHome);
        return viewHome;
    }
    @SuppressLint("WrongViewCast")
    private void initView(View viewHome) {
        ImageView hBack=viewHome.findViewById(R.id.h_back);
        ImageView hHead=viewHome.findViewById(R.id.h_head);

        TextView name=viewHome.findViewById(R.id.name);
        TextView password=viewHome.findViewById(R.id.password);
        TextView bingli=viewHome.findViewById(R.id.bingli);
        TextView banben=viewHome.findViewById(R.id.banben);
        TextView username=viewHome.findViewById(R.id.user_name);

        Glide.with(this).load(R.drawable.touxiang)
                .bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity()))
                .into(hBack);
        //设置圆形图像
        Glide.with(this).load(R.drawable.touxiang)
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(hHead);
        username.setText(functionActivity.getname());
        name.setOnClickListener(this);
        password.setOnClickListener(this);
        bingli.setOnClickListener(this);
        banben.setOnClickListener(this);
    }



    class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            if (view == object) {
                return true;
            } else
                return false;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }
    }

    private Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.name://用户信息
                intent = new Intent(getContext(), informationActivity.class);
                intent.putExtra("name",functionActivity.getname());
                startActivity(intent);
                break;
            case R.id.password://密码
                Intent intent = new Intent(getContext(), ForgetPwdActivity.class);
                intent.putExtra("name",functionActivity.getname());
                startActivity(intent);
                break;
            case R.id.bingli://病例
                intent = new Intent(getContext(), bingli.class);
                intent.putExtra("name",functionActivity.getname());
                startActivity(intent);
                break;
            case R.id.banben://版本
                intent = new Intent(getContext(), banben.class);
                startActivity(intent);
                break;
        }
    }
}