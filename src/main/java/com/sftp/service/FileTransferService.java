package com.sftp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileTransferService {
	
	boolean uploadFile(String localFilePath, String remoteFilePath);

	boolean uploadFile(MultipartFile file);
	
	boolean downloadFile(String localFilePath, String remoteFilePath);

	File downloadFile(String fileName) throws IOException;

}
