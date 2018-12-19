package megvii.testfacepass.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.flexbox.FlexboxLayout;

public class ViewUpadte {

    public static void updataView(String viewGroupParent, View view, int width, int hight, int left, int top, int right, int booton ){
        if (viewGroupParent.equals("RelativeLayout")){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            if (left!=0)
                params.leftMargin = left;
            if (top!=0)
                params.topMargin = top;
            if (right!=0)
                params.rightMargin = right;
            if (booton!=0)
                params.bottomMargin = booton;
            if (width!=0)
                params.width = width;
            if (hight!=0)
                params.height = hight;
            view.setLayoutParams(params);
            view.invalidate();
        }
        if (viewGroupParent.equals("LinearLayout")){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            if (left!=0)
                params.leftMargin = left;
            if (top!=0)
                params.topMargin = top;
            if (right!=0)
                params.rightMargin = right;
            if (booton!=0)
                params.bottomMargin = booton;
            if (width!=0)
                params.width = width;
            if (hight!=0)
                params.height = hight;
            view.setLayoutParams(params);
            view.invalidate();
        }
        if (viewGroupParent.equals("FlexboxLayout")){
            FlexboxLayout.LayoutParams params = (FlexboxLayout.LayoutParams) view.getLayoutParams();
            if (left!=0)
                params.leftMargin = left;
            if (top!=0)
                params.topMargin = top;
            if (right!=0)
                params.rightMargin = right;
            if (booton!=0)
                params.bottomMargin = booton;
            if (width!=0)
                params.width = width;
            if (hight!=0)
                params.height = hight;
            view.setLayoutParams(params);
            view.invalidate();
        }

    }


    public static void updataViewGroup(String viewGroupParent, ViewGroup viewGroup, int width, int hight, int left, int top, int right, int booton ){
        if (viewGroupParent.equals("RelativeLayout")){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewGroup.getLayoutParams();
            if (left!=0)
                params.leftMargin = left;
            if (top!=0)
                params.topMargin = top;
            if (right!=0)
                params.rightMargin = right;
            if (booton!=0)
                params.bottomMargin = booton;
            if (width!=0)
                params.width = width;
            if (hight!=0)
                params.height = hight;
            viewGroup.setLayoutParams(params);
            viewGroup.invalidate();
        }
        if (viewGroupParent.equals("LinearLayout")){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewGroup.getLayoutParams();
            if (left!=0)
                params.leftMargin = left;
            if (top!=0)
                params.topMargin = top;
            if (right!=0)
                params.rightMargin = right;
            if (booton!=0)
                params.bottomMargin = booton;
            if (width!=0)
                params.width = width;
            if (hight!=0)
                params.height = hight;
            viewGroup.setLayoutParams(params);
            viewGroup.invalidate();
        }
        if (viewGroupParent.equals("FlexboxLayout")){
            FlexboxLayout.LayoutParams params = (FlexboxLayout.LayoutParams) viewGroup.getLayoutParams();
            if (left!=0)
                params.leftMargin = left;
            if (top!=0)
                params.topMargin = top;
            if (right!=0)
                params.rightMargin = right;
            if (booton!=0)
                params.bottomMargin = booton;
            if (width!=0)
                params.width = width;
            if (hight!=0)
                params.height = hight;
            viewGroup.setLayoutParams(params);
            viewGroup.invalidate();
        }
    }
}
