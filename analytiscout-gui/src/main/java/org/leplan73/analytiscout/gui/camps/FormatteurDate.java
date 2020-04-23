package org.leplan73.analytiscout.gui.camps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class FormatteurDate extends AbstractFormatter {
	private String datePattern = "dd-MM-yyyy";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
     
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }
 
    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
         
        return "";
    }
}
