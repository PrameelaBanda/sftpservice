package com.sftp.service.impl;

import java.io.*;
import java.util.Vector;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import com.sftp.service.FileTransferService;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileTransferServiceImpl implements FileTransferService {

	private Logger logger = LoggerFactory.getLogger(FileTransferServiceImpl.class);
	
	@Value("${sftp.host}")
	private String host;
	
	@Value("${sftp.port}")
	private Integer port;
	
	@Value("${sftp.username}")
	private String username;
	
	@Value("${sftp.password}")
	private String password;
	
	@Value("${sftp.sessionTimeout}")
	private Integer sessionTimeout;
	
	@Value("${sftp.channelTimeout}")
	private Integer channelTimeout;

	@Value("${sftp.absolutePath}")
	private String absolutePath;
	
	@Override
	public boolean uploadFile(String localFilePath, String remoteFilePath) {
		ChannelSftp channelSftp = createChannelSftp();
		try {
//			channelSftp.
			channelSftp.put(localFilePath, remoteFilePath);
			return true;
		} catch(SftpException ex) {
			logger.error("Error upload file", ex);
		} finally {
			disconnectChannelSftp(channelSftp);
		}
		
		return false;
	}

	@Override
	public boolean uploadFile(MultipartFile file) {
		ChannelSftp channelSftp = createChannelSftp();
		InputStream inputStream = null;
		try {
			String fileName = file.getOriginalFilename();
			inputStream = file.getInputStream();
			channelSftp.put(inputStream, absolutePath+"/"+fileName);
			return true;
		} catch(SftpException | IOException ex) {
			logger.error("Error upload file", ex);
		} finally {
			disconnectChannelSftp(channelSftp);
		}

		return false;
	}

	@Override
	public boolean downloadFile(String localFilePath, String remoteFilePath) {
/*		ChannelSftp channelSftp = createChannelSftp();
		OutputStream outputStream;
		try {
			File file = new File(localFilePath);
			outputStream = new FileOutputStream(file);
			channelSftp.get(remoteFilePath, outputStream);
			file.createNewFile();
			return true;
		} catch(SftpException | IOException ex) {
			logger.error("Error download file", ex);
		} finally {
			disconnectChannelSftp(channelSftp);
		}*/
		
		return false;
	}

	@Override
	public File downloadFile(String fileName) throws IOException {
		ChannelSftp channelSftp = createChannelSftp();
		FileOutputStream outputStream;
		File file = null;
		try {
			file = new File(fileName);
			file.createNewFile();
			outputStream = new FileOutputStream(file);
			channelSftp.cd(absolutePath);
			Vector<ChannelSftp.LsEntry> lsEntries = channelSftp.ls(fileName);
			if (lsEntries.isEmpty()) {
				logger.error("No file exist in the specified com.sftp folder location.");
				return null;
			}
			IOUtils.copy(channelSftp.get(fileName), outputStream);
			outputStream.close();

		} catch(SftpException | FileNotFoundException ex) {
			logger.error("Error download file", ex);
		} finally {
			disconnectChannelSftp(channelSftp);
		}
		return file;
	}
	
	private ChannelSftp createChannelSftp() {
		try {
			JSch jSch = new JSch();
			Session session = jSch.getSession(username, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect(sessionTimeout);
			Channel channel = session.openChannel("sftp");
			channel.connect(channelTimeout);
			return (ChannelSftp) channel;
		} catch(JSchException ex) {
			logger.error("Create ChannelSftp error", ex);
		}
		
		return null;
	}
	
	private void disconnectChannelSftp(ChannelSftp channelSftp) {
		try {
			if( channelSftp == null) 
				return;
			
			if(channelSftp.isConnected()) 
				channelSftp.disconnect();
			
			if(channelSftp.getSession() != null) 
				channelSftp.getSession().disconnect();
			
		} catch(Exception ex) {
			logger.error("SFTP disconnect error", ex);
		}
	}
	
}
