package com.example.qiniufiletool;

import java.io.File;





public class FiledownData {

	//0 新文件【开始】 1下载过_ETag一致【橙色更新】 2下载过_ETag不一致【绿色更新】 3完成【橙色更新】
    int FILEDOWN = 0;
	File downfile;
	int startIndex;
	int endLength;
	String fileCode;
	String urlString;
	String filename;
	public FiledownData(File downfile,int startIndex,int endLength,String fileCode,String urlString,String filename)
	{
		this.downfile = downfile;
		this.startIndex = startIndex;
		this.endLength = endLength;
		this.fileCode = fileCode;
		this.urlString =urlString;
		this.filename = filename;
	}
	public void setState(int state)
	{
		FILEDOWN = state;
	}
	public int getState()
	{
		return FILEDOWN;
		
	}
	public void setUrlString(String newurlString)
	{
		urlString = newurlString;
	}
	public String getUrlString() 
	{
		return urlString;
		
	}
	public void setDownFile(File newfile)
	{
		downfile = newfile;
	}
	public void setStartIndex(int newIndex)
	{
		startIndex = newIndex;
	}
	public void setEndLength(int newendLength)
	{
		endLength = newendLength;
	}
	public void setFileCode(String newfileCode)
	{
		fileCode = newfileCode;
	}
	public File getDownFile()
	{
		return downfile;
	}
	public int getStartIndex()
	{
		return startIndex;
	}
	public int getEndLength()
	{
		return endLength;
	}
	public String getFileCode()
	{
		return fileCode;
	}
}
