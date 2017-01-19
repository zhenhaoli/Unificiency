package lmu.de.unificiencyandroid.InputValidation;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

public abstract class InputValidation implements TextWatcher {
    private final TextInputLayout textView;
    private String errorMsg;

    public InputValidation(TextInputLayout textView, String errorMsg) {
        this.textView = textView;
        this.errorMsg = errorMsg;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // doesn't matter
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // doesn't matter
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = textView.getEditText().getText().toString();
        //possibly show error message
        if(validate(text) || text.length() == 0){
           this.textView.setError(null);
        } else{
            this.textView.setError(this.errorMsg);
        }

    }

    public abstract boolean validate(String text);
}
