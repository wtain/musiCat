package ru.rz.musiCat.helpers;

public class StringHelpers {
	
	public static String sizes[] = { "B", "KB", "MB", "GB", "TB" };
	
	public static String fileSizeToString(long fileSize) {
		int i = 0, n = sizes.length;
		double size = fileSize;
		while (size >= 1024 && i < n - 1) {
			++i;
			size /= 1024;
		}
		return new StringBuffer()
				.append(String.format("%.2f", size))
				.append(" ")
				.append(sizes[i])
				.toString();
	}
}
