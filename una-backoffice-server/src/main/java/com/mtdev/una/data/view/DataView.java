package com.mtdev.una.data.view;

public interface DataView {
	boolean hasView();

	Class<? extends BaseView> getView();

	Object getData();
}
