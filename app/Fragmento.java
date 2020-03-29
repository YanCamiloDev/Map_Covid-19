package net.yan.maps;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Fragmento extends BottomSheetDialogFragment  {
    public static final String TAG = "ActionBottomDialog";
    private Context mListener;
    public TextView casos;
    public TextView casosH;
    public TextView mortes;
    public TextView mortesh;
    public TextView prec;
    public TextView pcrit;
    public TextView casosA;
    public TextView casos_p_mil;



    public static Fragmento newInstance() {
        return new Fragmento();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bottomsheetlayout, container, false);
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottomsheetlayout, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) contentView.getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView casos = view.findViewById(R.id.textView);
        TextView casosH = view.findViewById(R.id.textView2);
        TextView mortes = view.findViewById(R.id.textView3);
        TextView mortesh = view.findViewById(R.id.textView4);
        TextView prec = view.findViewById(R.id.textView5);
        TextView pcrit = view.findViewById(R.id.textView6);
        TextView casosA = view.findViewById(R.id.textView7);
        TextView casos_p_mil = view.findViewById(R.id.textView8);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener =  context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}