package com.theprogrammingturkey.customUI.util;

public class RenderUtil
{

	public static int colorIntFromRGBA(int r, int g, int b, int a)
	{
		return (a << 24) | (r << 16) | (g << 8) | (b);
	}

	public static int colorIntFromRGBA(float r, float g, float b, float a)
	{
		return RenderUtil.colorIntFromRGBA((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));
	}
}