package demo.doctor.com.doctor.wenzhen;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import demo.doctor.com.doctor.R;

public class ChatAdapter extends BaseQuickAdapter<Chat, BaseViewHolder> {
    public ChatAdapter(@Nullable List<Chat> data) {
        super(R.layout.content_main,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Chat item) {
        helper.setVisible(R.id.left_layout,true);
        helper.setVisible(R.id.right_Layout,true);
        switch (item.type){
            case 1:
                helper.setGone(R.id.right_Layout,false);
                helper.setText(R.id.left_msg,item.content);
                break;

            case 2:
                helper.setGone(R.id.left_layout,false);
                helper.setText(R.id.right_msg,item.content);
                break;
        }
    }
}
