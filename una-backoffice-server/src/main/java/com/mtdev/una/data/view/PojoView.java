package com.mtdev.una.data.view;

import lombok.Data;

@Data
public class PojoView implements DataView {

	private Object data;
	private Class<? extends BaseView> view;

	public PojoView(Object result, Class<? extends BaseView> viewClass) {
		data = result;
		view = viewClass;
	}

	public boolean hasView() {
		return true;
	}

	public Class<? extends BaseView> getView() {
		return view;
	}

	public Object getData() {
		return data;
	}
}
