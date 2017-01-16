package lmu.de.unificiencyandroid.InputValidation;

import android.support.design.widget.TextInputLayout;

public class ValidateGroupDescription extends InputValidation {
    public ValidateGroupDescription(TextInputLayout textView, String errorMsg) {
        super(textView, errorMsg);
    }

    @Override
    public boolean validate(String text) {
        return text.length() >= 12;
    }
}
