package ntut.csie.taskService.unitTest.useCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ntut.csie.taskService.controller.task.taskAttachFile.DownloadTaskAttachFileRestfulAPI;
import ntut.csie.taskService.controller.task.taskAttachFile.GetTaskAttachFilesByTaskIdRestfulAPI;
import ntut.csie.taskService.controller.task.taskAttachFile.RemoveTaskAttachFileRestfulAPI;
import ntut.csie.taskService.controller.task.taskAttachFile.UploadTaskAttachFileRestfulAPI;
import ntut.csie.taskService.model.task.Task;
import ntut.csie.taskService.model.task.TaskAttachFile;
import ntut.csie.taskService.unitTest.factory.TestFactory;
import ntut.csie.taskService.unitTest.repository.FakeEventStore;
import ntut.csie.taskService.unitTest.repository.FakeTaskRepository;
import ntut.csie.taskService.useCase.DomainEventListener;
import ntut.csie.taskService.useCase.task.taskAttachFile.TaskAttachFileModel;
import ntut.csie.taskService.useCase.task.taskAttachFile.download.DownloadTaskAttachFileInput;
import ntut.csie.taskService.useCase.task.taskAttachFile.download.DownloadTaskAttachFileOutput;
import ntut.csie.taskService.useCase.task.taskAttachFile.download.DownloadTaskAttachFileUseCase;
import ntut.csie.taskService.useCase.task.taskAttachFile.download.DownloadTaskAttachFileUseCaseImpl;
import ntut.csie.taskService.useCase.task.taskAttachFile.get.GetTaskAttachFilesByTaskIdInput;
import ntut.csie.taskService.useCase.task.taskAttachFile.get.GetTaskAttachFilesByTaskIdOutput;
import ntut.csie.taskService.useCase.task.taskAttachFile.get.GetTaskAttachFilesByTaskIdUseCase;
import ntut.csie.taskService.useCase.task.taskAttachFile.get.GetTaskAttachFilesByTaskIdUseCaseImpl;
import ntut.csie.taskService.useCase.task.taskAttachFile.remove.RemoveTaskAttachFileInput;
import ntut.csie.taskService.useCase.task.taskAttachFile.remove.RemoveTaskAttachFileOutput;
import ntut.csie.taskService.useCase.task.taskAttachFile.remove.RemoveTaskAttachFileUseCase;
import ntut.csie.taskService.useCase.task.taskAttachFile.remove.RemoveTaskAttachFileUseCaseImpl;
import ntut.csie.taskService.useCase.task.taskAttachFile.upload.UploadTaskAttachFileInput;
import ntut.csie.taskService.useCase.task.taskAttachFile.upload.UploadTaskAttachFileOutput;
import ntut.csie.taskService.useCase.task.taskAttachFile.upload.UploadTaskAttachFileUseCase;
import ntut.csie.taskService.useCase.task.taskAttachFile.upload.UploadTaskAttachFileUseCaseImpl;

public class TaskAttachFileUseCaseTest {
	private FakeTaskRepository fakeTaskRepository;
	private FakeEventStore fakeEventStore;
	
	private TestFactory testFactory;
	
	private Task task;
	private String taskId;
	private String backlogItemId;
	
	@Before
	public void setUp() {
		fakeTaskRepository = new FakeTaskRepository();
		fakeEventStore = new FakeEventStore();
		DomainEventListener.getInstance().init(fakeTaskRepository, fakeEventStore);
		
		testFactory = new TestFactory(fakeTaskRepository);
		
		backlogItemId = "1";
		String description = "Implement the entity of the task.";
		int estimate = 3;
		String notes = "This is the notes for the task.";
		task = testFactory.newTask(description, estimate, notes, backlogItemId);
		taskId = task.getTaskId();
	}
	
	@After
	public void tearDown() {
		fakeTaskRepository.clearAll();
		fakeEventStore.clearAll();
		
		File folder = new File("taskAttachFiles" + File.separator + taskId);
		File[] attachFileList = folder.listFiles();
		if(attachFileList != null) {
			for(File attachFile : attachFileList) {
				attachFile.delete();
			}
		}
		folder.delete();
	}
	
	@Test
	public void Should_Success_When_UploadTaskAttachFile() {
		String name = "Test.txt";
		File attachFile = new File(name);
		byte[] attachFileContents = null;
		try {
			attachFileContents = Files.readAllBytes(attachFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int originalNumberOfUploadedTaskAttachFiles = task.getTaskAttachFiles().size();
		
		UploadTaskAttachFileOutput output = uploadTaskAttachFile(attachFileContents, name, taskId);
		
		int newNumberOfUploadedTaskAttachFiles = task.getTaskAttachFiles().size();
		
		assertEquals(1, newNumberOfUploadedTaskAttachFiles - originalNumberOfUploadedTaskAttachFiles);
		
		boolean isUploadSuccess = output.isUploadSuccess();
		assertTrue(isUploadSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_UploadAttachFileToNotExistTask() {
		String name = "Test.txt";
		File attachFile = new File(name);
		byte[] attachFileContents = null;
		try {
			attachFileContents = Files.readAllBytes(attachFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		UploadTaskAttachFileOutput output = uploadTaskAttachFile(attachFileContents, name, null);
		
		boolean isUploadSuccess = output.isUploadSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the task is not exist!";
		assertFalse(isUploadSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_UploadAttachFileToTaskWithNullAttachFileContents() {
		byte[] attachFileContents = null;
		String name = null;
		
		UploadTaskAttachFileOutput output = uploadTaskAttachFile(attachFileContents, name, taskId);
		
		boolean isUploadSuccess = output.isUploadSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Please upload the file!";
		assertFalse(isUploadSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_UploadAttachFileToTaskWithEmptyAttachFileContents() {
		byte[] attachFileContents = new byte[0];
		String name = "";
		
		UploadTaskAttachFileOutput output = uploadTaskAttachFile(attachFileContents, name, taskId);
		
		boolean isUploadSuccess = output.isUploadSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Please upload the file!";
		assertFalse(isUploadSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_UploadAttachFileToTaskWithTooLargeAttachFileContents() {
		byte[] attachFileContents = new byte[2097153];
		String name = "Test.txt";
		
		UploadTaskAttachFileOutput output = uploadTaskAttachFile(attachFileContents, name, taskId);
		
		boolean isUploadSuccess = output.isUploadSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "The size of the file is too large! Please upload the smaller file!";
		assertFalse(isUploadSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_UploadAttachFileToTaskWithNullName() {
		byte[] attachFileContents = new byte[2097152];
		String name = null;
		
		UploadTaskAttachFileOutput output = uploadTaskAttachFile(attachFileContents, name, taskId);
		
		boolean isUploadSuccess = output.isUploadSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "The name of the task attach file should be required!\n";
		assertFalse(isUploadSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_UploadAttachFileToTaskWithEmptyName() {
		byte[] attachFileContents = new byte[2097152];
		String name = "";
		
		UploadTaskAttachFileOutput output = uploadTaskAttachFile(attachFileContents, name, taskId);
		
		boolean isUploadSuccess = output.isUploadSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "The name of the task attach file should be required!\n";
		assertFalse(isUploadSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnTaskAttachFileList_When_GetAttachFilesOfTask() {
		String[] names = {"Test.txt", "測試用檔案-工作會議附件.docx", "(Test) Product Backlog Excel File 2020-1-8.xlsx"};
		
		int numberOfTaskAttachFiles = names.length;
		
		for(int i = 0; i < numberOfTaskAttachFiles; i++) {
			File attachFile = new File(names[i]);
			byte[] attachFileContents = null;
			try {
				attachFileContents = Files.readAllBytes(attachFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			uploadTaskAttachFile(attachFileContents, names[i], taskId);
		}
		
		GetTaskAttachFilesByTaskIdOutput output = getTaskAttachFilesByTaskId(taskId);
		List<TaskAttachFileModel> taskAttachFileList = output.getTaskAttachFileList();
		
		for(int i = 0; i < numberOfTaskAttachFiles; i++) {
			assertEquals(names[i], taskAttachFileList.get(i).getName());
		}
		assertEquals(numberOfTaskAttachFiles, taskAttachFileList.size());
	}
	
	@Test
	public void Should_Success_When_DownloadTaskAttachFile() {
		String name = "Test.txt";
		File attachFile = new File(name);
		byte[] attachFileContents = null;
		try {
			attachFileContents = Files.readAllBytes(attachFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		uploadTaskAttachFile(attachFileContents, name, taskId);

		List<TaskAttachFile> taskAttachFileList = new ArrayList<>(task.getTaskAttachFiles());
		String taskAttachFileId = taskAttachFileList.get(taskAttachFileList.size() - 1).getTaskAttachFileId();
		
		DownloadTaskAttachFileOutput output = downloadTaskAttachFile(taskAttachFileId, taskId);
		
		boolean isDownloadSuccess = output.isDownloadSuccess();
		assertTrue(isDownloadSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_DownloadAttachFileFromNotExistTask() {
		String name = "Test.txt";
		File attachFile = new File(name);
		byte[] attachFileContents = null;
		try {
			attachFileContents = Files.readAllBytes(attachFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		uploadTaskAttachFile(attachFileContents, name, taskId);

		List<TaskAttachFile> taskAttachFileList = new ArrayList<>(task.getTaskAttachFiles());
		String taskAttachFileId = taskAttachFileList.get(taskAttachFileList.size() - 1).getTaskAttachFileId();
		
		DownloadTaskAttachFileOutput output = downloadTaskAttachFile(taskAttachFileId, null);
		
		boolean isDownloadSuccess = output.isDownloadSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the task is not exist!";
		assertFalse(isDownloadSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_DownloadNotExistTaskAttachFile() {
		DownloadTaskAttachFileOutput output = downloadTaskAttachFile(null, taskId);
		
		boolean isDownloadSuccess = output.isDownloadSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the attach file of the task is not exist!";
		assertFalse(isDownloadSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_AttachFileNotBelongToAnyTask_When_RemoveTaskAttachFile() {
		String name = "Test.txt";
		File attachFile = new File(name);
		byte[] attachFileContents = null;
		try {
			attachFileContents = Files.readAllBytes(attachFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		uploadTaskAttachFile(attachFileContents, name, taskId);
		int numberOfTaskAttachFileBeforeRemoved = task.getTaskAttachFiles().size();
		
		List<TaskAttachFile> taskAttachFileList = new ArrayList<>(task.getTaskAttachFiles());
		String taskAttachFileId = taskAttachFileList.get(taskAttachFileList.size() - 1).getTaskAttachFileId();
		
		RemoveTaskAttachFileOutput output = removeTaskAttachFile(taskAttachFileId, taskId);
		int numberOfTaskAttachFileAfterRemoved = task.getTaskAttachFiles().size();
		
		assertEquals(1, numberOfTaskAttachFileBeforeRemoved - numberOfTaskAttachFileAfterRemoved);
		
		boolean isRemoveSuccess = output.isRemoveSuccess();
		assertTrue(isRemoveSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_RemoveAttachFileFromNotExistTask() {
		String name = "Test.txt";
		File attachFile = new File(name);
		byte[] attachFileContents = null;
		try {
			attachFileContents = Files.readAllBytes(attachFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		uploadTaskAttachFile(attachFileContents, name, taskId);
		List<TaskAttachFile> taskAttachFileList = new ArrayList<>(task.getTaskAttachFiles());
		String taskAttachFileId = taskAttachFileList.get(taskAttachFileList.size() - 1).getTaskAttachFileId();
		
		RemoveTaskAttachFileOutput output = removeTaskAttachFile(taskAttachFileId, null);
		
		boolean isRemoveSuccess = output.isRemoveSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the task is not exist!";
		assertFalse(isRemoveSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_RemoveNotExistTaskAttachFile() {
		String taskAttachFileId = null;
		
		RemoveTaskAttachFileOutput output = removeTaskAttachFile(taskAttachFileId, taskId);
		
		boolean isRemoveSuccess = output.isRemoveSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the attach file of the task is not exist!";
		assertFalse(isRemoveSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	private UploadTaskAttachFileOutput uploadTaskAttachFile(byte[] attachFileContents, String name, String taskId) {
		UploadTaskAttachFileUseCase uploadTaskAttachFileUseCase = new UploadTaskAttachFileUseCaseImpl(fakeTaskRepository);
		UploadTaskAttachFileInput input = (UploadTaskAttachFileInput) uploadTaskAttachFileUseCase;
		input.setAttachFileContents(attachFileContents);
		input.setName(name);
		input.setTaskId(taskId);
		UploadTaskAttachFileOutput output = new UploadTaskAttachFileRestfulAPI();
		uploadTaskAttachFileUseCase.execute(input, output);
		return output;
	}
	
	private GetTaskAttachFilesByTaskIdOutput getTaskAttachFilesByTaskId(String taskId) {
		GetTaskAttachFilesByTaskIdUseCase getTaskAttachFilesByTaskIdUseCase = new GetTaskAttachFilesByTaskIdUseCaseImpl(fakeTaskRepository);
		GetTaskAttachFilesByTaskIdInput input = (GetTaskAttachFilesByTaskIdInput) getTaskAttachFilesByTaskIdUseCase;
		input.setTaskId(taskId);
		GetTaskAttachFilesByTaskIdOutput output = new GetTaskAttachFilesByTaskIdRestfulAPI();
		getTaskAttachFilesByTaskIdUseCase.execute(input, output);
		return output;
	}
	
	private DownloadTaskAttachFileOutput downloadTaskAttachFile(String taskAttachFileId, String taskId) {
		DownloadTaskAttachFileUseCase downloadTaskAttachFileUseCase = new DownloadTaskAttachFileUseCaseImpl(fakeTaskRepository);
		DownloadTaskAttachFileInput input = (DownloadTaskAttachFileInput) downloadTaskAttachFileUseCase;
		input.setTaskAttachFileId(taskAttachFileId);
		input.setTaskId(taskId);
		DownloadTaskAttachFileOutput output = new DownloadTaskAttachFileRestfulAPI();
		downloadTaskAttachFileUseCase.execute(input, output);
		return output;
	}
	
	private RemoveTaskAttachFileOutput removeTaskAttachFile(String taskAttachFileId, String taskId) {
		RemoveTaskAttachFileUseCase removeTaskAttachFileUseCase = new RemoveTaskAttachFileUseCaseImpl(fakeTaskRepository);
		RemoveTaskAttachFileInput input = (RemoveTaskAttachFileInput) removeTaskAttachFileUseCase;
		input.setTaskAttachFileId(taskAttachFileId);
		input.setTaskId(taskId);
		RemoveTaskAttachFileOutput output = new RemoveTaskAttachFileRestfulAPI();
		removeTaskAttachFileUseCase.execute(input, output);
		return output;
	}
}
