package board.model.dto;

import java.sql.Date;
import java.util.List;

public class BoardExt extends Board {
	private int attachCount;
	private List<Attachment> attachments;
	private List<BoardComment> comments;

	public BoardExt() {
		super();
	}

	public BoardExt(int attachCount, List<Attachment> attachments) {
		super();
		this.attachCount = attachCount;
		this.attachments = attachments;
	}

	public BoardExt(int no, String title, String memberId, String content, int readCount, Date regDate, int attachCount,
			List<Attachment> attachments) {
		super(no, title, memberId, content, readCount, regDate);
		this.attachCount = attachCount;
		this.attachments = attachments;
	}

	public int getAttachCount() {
		return attachCount;
	}

	public void setAttachCount(int attachCount) {
		this.attachCount = attachCount;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
	public List<BoardComment> getBoardComments() {
		return comments;
	}

	public void setBoardComments(List<BoardComment> comments) {
		this.comments = comments;
		
	}

	@Override
	public String toString() {
		return "BoardExt [attachCount=" + attachCount + ", attachments=" + attachments + ", comments=" + comments
				+ ", toString()=" + super.toString() + "]";
	}

}
