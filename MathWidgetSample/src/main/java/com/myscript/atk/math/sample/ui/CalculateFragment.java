package com.myscript.atk.math.sample.ui;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.myscript.atk.math.sample.R;
import com.myscript.atk.math.sample.adapter.MySimpleAdapter;
import com.myscript.atk.math.sample.widget.MyGridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import bsh.EvalError;
import bsh.Interpreter;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hd_chen on 2016/8/15.
 */
public class CalculateFragment extends Fragment {

    @Bind(R.id.result_view)
    TextView result;
    @Bind(R.id.result_last)
    TextView result_last;
    @Bind(R.id.gridView)
    MyGridView myGridView;

    boolean isClear = false; //用于是否显示器需要被清理
    Stack<String> expressions;
    private String[] itemName = {
            "AC", "del", "+/-", "%",
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "*",
            "0", ".", "=", "/"};
    private List<Map<String, Object>> data_list;
    private SoundPool sp;
    private int music;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_calculate, container, false);
        ButterKnife.bind(this, view);
        expressions = new Stack<>();
        fillGrid();
        initSound();
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sp.play(music, 0.5f, 0.5f, 0, 0, 1);
                String result_old = result.getText().toString();
                switch (itemName[i]) {
                    case "=":
                        if (!isEmpty(result_old)) {
                            String result_new = getRs(result_old);
                            result.setText(result_new);
                            if (result_new != null) {
                                expressions.add(result_old);
                            }
                        }
                        break;
                    case "AC":
                        result.setText(null);
                        result_last.setText(result_old);
                        break;
//                    case "back":
//                        if (!expressions.isEmpty()) {
//                            result.setText(expressions.pop());
//                        }
//                        break;
                    case "del":
                        if (!"".equals(result_old))
                            result.setText(result_old.substring(0, result_old.length() - 1));
                        break;
                    case "+/-":
                        try {
                            double d_result = Double.parseDouble(result_old);
                            String new_result;
                            if (result_old.startsWith("-")) {
                                new_result = result_old.substring(1, result_old.length());
                            } else {
                                new_result = "-" + result_old;
                            }
                            result.setText(new_result);
                        } catch (Exception e) {

                        } finally {
                            break;
                        }
                    default:
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(result_old);
                        stringBuilder.append(itemName[i]);
                        result.setText(stringBuilder);
                }
            }
        });
        return view;
    }

    /***
     * @param exp 算数表达式
     * @return 根据表达式返回结果
     */
    private String getRs(String exp) {
        Interpreter bsh = new Interpreter();
        Number result = null;
        try {
            exp = filterExp(exp);
            result = (Number) bsh.eval(exp);
        } catch (EvalError e) {
            e.printStackTrace();
            isClear = true;
            Toast.makeText(getActivity(), "算式错误", Toast.LENGTH_SHORT).show();
            return null;
        }
        exp = result.doubleValue() + "";
        if (exp.endsWith(".0"))
            exp = exp.substring(0, exp.indexOf(".0"));
        return exp;
    }

    /**
     * 因为计算过程中,全程需要有小数参与,所以需要过滤一下
     *
     * @param exp 算数表达式
     * @return
     */
    private String filterExp(String exp) {
        String num[] = exp.split("");
        String temp = null;
        int begin = 0, end = 0;
        for (int i = 1; i < num.length; i++) {
            temp = num[i];
            if (temp.matches("[+-/()*]")) {
                if (temp.equals(".")) continue;
                end = i - 1;
                temp = exp.substring(begin, end);
                if (temp.trim().length() > 0 && temp.indexOf(".") < 0)
                    num[i - 1] = num[i - 1] + ".0";
                begin = end + 1;
            }
        }
        return Arrays.toString(num).replaceAll("[\\[\\], ]", "");
    }

    /***
     * @param str
     * @return 字符串非空验证
     */
    private boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public void fillGrid() {
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getGridData();
        //新建适配器
        String[] from = {"text"};
        int[] to = {R.id.tv_item};
        MySimpleAdapter sim_adapter = new MySimpleAdapter(getActivity(), data_list, R.layout.gird_item, from, to);
        //配置适配器
        myGridView.setAdapter(sim_adapter);
    }

    public List<Map<String, Object>> getGridData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < itemName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", itemName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    public void initSound(){
        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(getActivity(), R.raw.keysound, 1);
    }
}
