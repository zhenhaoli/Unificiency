package lmu.de.unificiencyandroid.InputValidation;

import android.support.design.widget.TextInputLayout;

/**
 * Created by robertMueller on 02.01.17.
 */

public class ValidateGroupDescription extends InputValidation {
    public ValidateGroupDescription(TextInputLayout textView, String errorMsg) {
        super(textView, errorMsg);
    }

    @Override
    public boolean validate(String text) {
        return text.length() >= 12 && text.length() <= 40;
    }
}
