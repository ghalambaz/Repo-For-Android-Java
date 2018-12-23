package com.yana.YFrame.ValidationUtils;/*
 * Ali Ghalambaz<aghalambaz@gmail.com> on 2015/10/05 - 11:47 .
 * YFrame For Android Beta
 */

import android.content.Context;
import android.widget.TextView;
import com.yana.atiranhamrah.R;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidator {

    ArrayList<Rule> rules = new ArrayList<>();
    ArrayList<Error> errors = new ArrayList<>();
    Boolean showErrors = true;
    Boolean continueOnError = true;

    Context context;

    public FormValidator(Context context,Boolean showErrors, Boolean continueOnError) {
        this.showErrors = showErrors;
        this.continueOnError = continueOnError;
        this.context = context;
    }

    public FormValidator(Context context,Boolean continueOnError) {
        this.continueOnError = continueOnError;
        this.context = context;
    }

    public FormValidator(Context context) {
        this.context = context;
    }

    public class Rule {
        TextView textView;
        Type type;
        Long min, max;
        Boolean required;
        Boolean hasError = false;
        String msg;
        String extra;

        Rule(TextView textView, Type type, Long min, Long max, Boolean required, String extra) {
            this.textView = textView;
            this.type = type;
            this.min = min;
            this.max = max;
            this.required = required;
           // this.msg = msg;
            this.extra = extra;
        }

//        Rule(TextView textView, Type type, Integer min, Integer max, Boolean required, String msg) {
//            this.textView = textView;
//            this.type = type;
//            this.min = min;
//            this.max = max;
//            this.required = required;
//            this.msg = msg;
//        }

        Rule(TextView textView, Type type, Long min, Long max, Boolean required) {
            this.textView = textView;
            this.type = type;
            this.min = min;
            this.max = max;
            this.required = required;
        }
    }

    public enum Type {
        PATTERN, EMAIL,INT_DASHED, INT, DOUBLE, TITLE, ALPHABET, MOBILE
    }

    public void addRule(TextView textView, Type type, Long min, Long max, Boolean required,String extra) {
        rules.add(new Rule(textView, type, min, max, required, extra));
    }

    public void addRule(TextView textView, Type type, Long min, Long max, Boolean required) {
        rules.add(new Rule(textView, type, min, max, required));
    }

    public boolean isValidPattern(Rule rule) {
        CharSequence text = rule.textView.getText();
        Pattern pattern = Pattern.compile(rule.extra, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public boolean isEmail(CharSequence text) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    public boolean isMobile(Rule rule) {
        rule.extra = "^09[0-9]{9}";
        return isValidPattern(rule);
    }

    public boolean isTitle(CharSequence target) {
        return true;
    }

    public static boolean isInteger(CharSequence target) {
        try {
            Long d = Long.parseLong(target.toString());
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }

    }

    public boolean isDouble(CharSequence target) {
        try {
            double d = Double.parseDouble(target.toString());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean isAlphabet(CharSequence target) {
        char[] chars = target.toString().toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidIranianNationalCode(String input) {
        // check if input has 10 digits that all of them are not equal
//        if (!input.matches("^\\d{10}$"))
//            return false;
//
//        int check = Integer.parseInt(input.substring(9, 10));
//
//        int sum = Streams.intRange(0, 9)
//                .map((IntUnaryOperator) x ->
//                        Integer.parseInt(input.substring(x, x + 1)) * (10 - x))
//                .sum() % 11;
//
//        return sum < 2 && check == sum || sum >= 2 && check + sum == 11;
        return false;
    }

    public boolean validate()
    {
        if(isValid())
        {
            return true;
        }
        else if(showErrors)
        {
            for (Rule r:rules) {
                if(r.hasError)r.textView.setError(r.msg);
            }
            return false;
        }else return false;
    }


    private boolean isValid() {
        boolean sw = true;
        Iterator<Rule> iterator = rules.iterator();
        while (iterator.hasNext()) {
            Rule rule = iterator.next();
            if (rule.required && rule.textView.getText().toString().trim().equals("")) {
                sw = false;
                rule.hasError = true;
                rule.msg = context.getText(R.string.form_field_required_msg).toString();
                if (!continueOnError) return false;
            }
            else
            {
                switch (rule.type) {
                    case PATTERN:
                        if (isValidMaxMinText(rule)) {
                            if (!isValidPattern(rule)) {
                                setError(rule);
                                if (!continueOnError) return false;
                            }
                        }
                        else
                        {
                            if (!continueOnError) return false;
                            sw = false;
                        }
                        break;
                    case EMAIL:
                        if (isValidMaxMinText(rule)) {
                            if (!isEmail(rule.textView.getText())) {
                                setError(rule);
                                if (!continueOnError) return false;
                            }
                        }
                        else
                        {
                            if (!continueOnError) return false;
                            sw = false;
                        }
                        break;
                    case INT:
                        if (isValidMaxMinInteger(rule)) {
                            if (!isInteger(rule.textView.getText())) {
                                setError(rule);
                                if (!continueOnError) return false;
                            }
                        }
                        else
                        {
                            if (!continueOnError) return false;
                            sw = false;
                        }
                        break;
                    case INT_DASHED:

                            if (!isIntegerDashed(rule.textView.getText())) {
                                setError(rule);
                                if (!continueOnError) return false;
                            }

                        break;
                    case DOUBLE:
                        if (isValidMaxMinDouble(rule)) {
                            if (!isDouble(rule.textView.getText())) {
                                setError(rule);
                                if (!continueOnError) return false;
                            }
                        }
                        else
                        {
                            if (!continueOnError) return false;
                            sw = false;
                        }
                        break;
                    case TITLE:
                        if (isValidMaxMinText(rule)) {
                            if (!isTitle(rule.textView.getText())) {
                                setError(rule);
                                if (!continueOnError) return false;
                            }
                        }
                        else
                        {
                            if (!continueOnError) return false;
                            sw = false;
                        }
                        break;
                    case ALPHABET:
                        if (isValidMaxMinText(rule)) {
                            if (!isAlphabet(rule.textView.getText())) {
                                setError(rule);
                                if (!continueOnError) return false;
                            }
                        }
                        else
                        {
                            if (!continueOnError) return false;
                            sw = false;
                        }
                        break;
                    case MOBILE:
                        if (isValidMaxMinText(rule)) {
                            if (!isMobile(rule)) {
                                setError(rule);
                                if (!continueOnError) return false;
                            }
                        }
                        else
                        {
                            if (!continueOnError) return false;
                            sw = false;
                        }
                        break;
                }
            }

        }
        return sw;
    }

    public static boolean isIntegerDashed(CharSequence text) {
        for (String retval: text.toString().split("-")){
            if(!isInteger(retval))
            {
                return false;
            }
        }
        return true;
    }

    private void setError(Rule rule) {
        rule.hasError = true;
        rule.msg = context.getText(R.string.form_field_content_msg).toString();
    }

    private boolean isValidMaxMinText(Rule rule) {
        try {
            if (rule.textView.getText().length() < rule.min) {
                rule.hasError = true;
                rule.msg = String.format(context.getText(R.string.form_text_min_msg).toString(), rule.min);
                return false;
            }

            if (rule.textView.getText().length() > rule.max) {
                rule.hasError = true;
                rule.msg = String.format(context.getText(R.string.form_text_max_msg).toString(), rule.max);
                return false;
            }
            return true;
        } catch (Exception e) {
            rule.hasError = true;
            rule.msg = context.getText(R.string.form_field_content_msg).toString();
            return false;
        }

    }

    private boolean isValidMaxMinInteger(Rule rule) {
        try {
            if (Long.parseLong(rule.textView.getText().toString()) < rule.min) {
                rule.hasError = true;
                rule.msg = String.format(context.getText(R.string.form_number_min_msg).toString(), rule.min);
                return false;
            }

            if (Long.parseLong(rule.textView.getText().toString()) > rule.max) {
                rule.hasError = true;
                rule.msg = String.format(context.getText(R.string.form_number_max_msg).toString(), rule.max);
                return false;
            }
            return true;
        } catch (Exception e) {
            rule.hasError = true;
            rule.msg = context.getText(R.string.form_field_content_msg).toString();
            return false;
        }
    }

    private boolean isValidMaxMinDouble(Rule rule) {
        try {
            if (Double.parseDouble(rule.textView.getText().toString()) < rule.min) {
                rule.hasError = true;
                rule.msg = String.format(context.getText(R.string.form_number_min_msg).toString(), rule.min);
                return false;
            }

            if (Double.parseDouble(rule.textView.getText().toString()) > rule.max) {
                rule.hasError = true;
                rule.msg = String.format(context.getText(R.string.form_number_max_msg).toString(), rule.max);
                return false;
            }
            return true;
        } catch (Exception e) {
            rule.hasError = true;
            rule.msg = context.getText(R.string.form_field_content_msg).toString();
            return false;
        }
    }
}
