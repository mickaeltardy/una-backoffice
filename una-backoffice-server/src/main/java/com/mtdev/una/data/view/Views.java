package com.mtdev.una.data.view;

public class Views {

	public static class Public implements BaseView {
	}

	public static class ExtendedPublic extends Public implements BaseView {
	}

	public static class Internal extends ExtendedPublic implements BaseView {
	}

}
