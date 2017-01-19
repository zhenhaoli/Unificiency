package lmu.de.unificiencyandroid.InputValidation;

import android.support.design.widget.TextInputLayout;

public class ValidateGroupName extends InputValidation {
    public ValidateGroupName(TextInputLayout textView, String errorMsg) {
        super(textView, errorMsg);
    }
    @Override
    public boolean validate(String text) {
        return text.length() >= 3;
    }
}
