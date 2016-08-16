package com.myscript.atk.math.sample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myscript.atk.math.sample.R;

import java.util.Arrays;
import java.util.Stack;

import bsh.EvalError;
import bsh.Interpreter;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hd_chen on 2016/8/15.
 */
public class CalculateFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.result_view)
    TextView result;
    @Bind(R.id.result_last)
    TextView result_last;
    @Bind(R.id.number0)
    Button number0;
    @Bind(R.id.number1)
    Button number1;
    @Bind(R.id.number2)
    Button number2;
    @Bind(R.id.number3)
    Button number3;
    @Bind(R.id.number4)
    Button number4;
    @Bind(R.id.number5)
    Button number5;
    @Bind(R.id.number6)
    Button number6;
    @Bind(R.id.number7)
    Button number7;
    @Bind(R.id.number8)
    Button number8;
    @Bind(R.id.number9)
    Button number9;
    @Bind(R.id.clear)
    Button clear;
    @Bind(R.id.add)
    Button add;
    @Bind(R.id.reduce)
    Button reduce;
    @Bind(R.id.multiply)
    Button multiply;
    @Bind(R.id.divide)
    Button divide;
    @Bind(R.id.opposite)
    Button opposite;
    @Bind(R.id.back)
    Button back;
    @Bind(R.id.point)
    Button point;
    @Bind(R.id.result)
    Button calculate;
    @Bind(R.id.delete)
    Button delete;

    boolean isClear = false; //用于是否显示器需要被清理
    Stack<String> expressions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_calculate, container, false);
        ButterKnife.bind(this, view);
        expressions = new Stack<>();
        number0.setOnClickListener(this);
        number1.setOnClickListener(this);
        number2.setOnClickListener(this);
        number3.setOnClickListener(this);
        number4.setOnClickListener(this);
        number5.setOnClickListener(this);
        number6.setOnClickListener(this);
        number7.setOnClickListener(this);
        number8.setOnClickListener(this);
        number9.setOnClickListener(this);
        point.setOnClickListener(this);
        add.setOnClickListener(this);
        divide.setOnClickListener(this);
        reduce.setOnClickListener(this);
        multiply.setOnClickListener(this);
        result.setOnClickListener(this);
        clear.setOnClickListener(this);
        calculate.setOnClickListener(this);
        opposite.setOnClickListener(this);
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        String result_old = result.getText().toString();
        switch (view.getId()) {
            case R.id.result:
                if (!isEmpty(result_old)) {
                    String result_new = getRs(result_old);
                    result.setText(result_new);
                    if (result_new != null) {
                        expressions.add(result_old);
                    }
                }
                break;
            case R.id.clear:
                result.setText(null);
                result_last.setText(result_old);
                break;
            case R.id.back:
                if (!expressions.isEmpty()) {
                    result.setText(expressions.pop());
                }
                break;
            case R.id.delete:
                result.setText(result_old.substring(0, result_old.length() - 1));
                break;
            case R.id.opposite:
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
                stringBuilder.append(((Button) view).getText().toString());
                result.setText(stringBuilder);
        }
    }
}
