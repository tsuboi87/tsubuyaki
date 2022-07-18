package cc.shinbi.tsubuyaki.model.entity;

public class Message extends EntityBase {
	private int userId;
	private String text;
	private String imageFileName;
	private boolean publicMessage;
	
	public int getUserId() {
		return userId;
	}
	
	public String getText() {
		return text;
	}
	
	public void srtImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	
	public boolean isPublicMessage() {
		return publicMessage;
	}

	public void setPublicMessage(boolean publicMessage) {
		this.publicMessage = publicMessage;

	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setText(String text) {
		this.text = text;
	}

}
