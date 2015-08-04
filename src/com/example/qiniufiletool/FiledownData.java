package com.example.qiniufiletool;

import java.io.File;





public class FiledownData {

	//0 ���ļ�����ʼ�� 1���ع�_ETagһ�¡���ɫ���¡� 2���ع�_ETag��һ�¡���ɫ���¡� 3��ɡ���ɫ���¡�
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
