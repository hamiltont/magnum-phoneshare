package org.magnum.phoneshare.view;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

public class PlainTextView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> arg0,
			HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
		
		Writer out = arg2.getWriter();
		for(String key : arg0.keySet()){
			String value = arg0.get(key).toString();
			out.write(value);
		}

	}

}
