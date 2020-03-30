package demo.doctor.com.doctor.shouye;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import demo.doctor.com.doctor.R;
import demo.doctor.com.doctor.shezhi.bingli;
import demo.doctor.com.doctor.wenzhen.MainActivity;

/**
 * 首页的界面
 */
public class daohangFrament extends Fragment implements View.OnClickListener {
    private static final String TAG = "HomeFragment";
    private ImageView iv_back;//隐藏返回按钮
    private TextView title_tv;//标题
    private ViewPager pager_function;
    private LinearLayout ll;
    private int lastPosition = 0;
    private ArrayList<ImageView> imageViewList;
    private int[] imageIds = {
            R.drawable.shouye1,
            R.drawable.shouye2,
            R.drawable.shouye3,
            R.drawable.shouye4,
    };

    private RelativeLayout rl_skin_daquan,skin_disease;//首页
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewHome = inflater.inflate(R.layout.fragment_content_home, container, false);
        initView(viewHome);
        initData();
        return viewHome;
    }

    private void initView(View viewHome) {
        iv_back = (ImageView) viewHome.findViewById(R.id.iv_back);
        title_tv = (TextView) viewHome.findViewById(R.id.title_tv);
        pager_function = (ViewPager) viewHome.findViewById(R.id.pager_function);
        ll = (LinearLayout) viewHome.findViewById(R.id.ll);

        rl_skin_daquan = (RelativeLayout) viewHome.findViewById(R.id.rl_skin_daquan);
        skin_disease = (RelativeLayout) viewHome.findViewById(R.id.skin_disease);



        rl_skin_daquan.setOnClickListener(this);
        skin_disease.setOnClickListener(this);


    }

    private void initData() {
        iv_back.setVisibility(View.GONE);
        title_tv.setText(getResources().getString(R.string.app_name));

        showDataList();
        setDataAdater();
    }

    private void showDataList() {
        //定义集合存放所有条目要显示的内容
        imageViewList = new ArrayList<ImageView>();
        int length = imageIds.length;

        for (int i = 0; i < length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(imageIds[i]);
            imageViewList.add(imageView);
            ImageView iv = new ImageView(getContext());
            iv.setBackgroundResource(R.drawable.selector_circle_bg);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 15;
            iv.setLayoutParams(lp);
            ll.addView(iv);
            if (i != 0) {
                iv.setEnabled(false);
            }
        }
    }

    private void setDataAdater() {
        pager_function.setAdapter(new MyAdapter());
        pager_function.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //界面切换时改变圆点的颜色
                ll.getChildAt(position).setEnabled(true);
                ll.getChildAt(lastPosition).setEnabled(false);
                lastPosition = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView iv = imageViewList.get(position);
            iv.setOnClickListener(new View.OnClickListener() {

                private Intent intent;

                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
//                            intent = new Intent(getContext(), DaquanActivity.class);
                            intent = new Intent(getContext(), MainActivity.class);
                            break;
                        case 1:
//                            intent = new Intent(getContext(), YuzhenActivity.class);
                            intent = new Intent(getContext(), MainActivity.class);
                            break;
                        case 2:
//                            intent = new Intent(getContext(), HuayandanActivity.class);
                            intent = new Intent(getContext(), MainActivity.class);
                            break;
                        case 3:
//                                intent = new Intent(getContext(), HealthActivity.class);
                            intent = new Intent(getContext(), MainActivity.class);
                            break;
                    }

                    startActivity(intent);
                }
            });
            container.addView(iv);
            return iv;
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
            case R.id.rl_skin_daquan://疾病大全
                intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.skin_disease://病例信息
                intent = new Intent(getContext(), bingli.class);
                intent.putExtra("name",functionActivity.getname());
                startActivity(intent);
                break;
        }
    }

    //统计页面
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MainScreen");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainScreen");
    }

}
