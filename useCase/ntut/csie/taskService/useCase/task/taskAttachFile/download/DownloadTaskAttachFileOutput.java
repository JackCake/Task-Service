package ntut.csie.taskService.useCase.task.taskAttachFile.download;

import ntut.csie.taskService.useCase.Output;

public interface DownloadTaskAttachFileOutput extends Output {
	public boolean isDownloadSuccess();
	
	public void setDownloadSuccess(boolean downloadSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
	
	public byte[] getAttachFileContents();
	
	public void setAttachFileContents(byte[] attachFileContents);
}
