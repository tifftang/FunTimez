package core;

public class ChatroomInvite {
	private String from;
	private String chatroomID;
	private String chatroomName;

	public void setSender(String from){
		this.from = from;
	}
	
	public void setChatroomID(String chatroomID){
		this.chatroomID = chatroomID;
	}
	
	public void setChatroomName(String chatroomName){
		this.chatroomName = chatroomName;
	}
	
	public String getSender(){
		return from;
	}
	
	public String getChatroomID(){
		return chatroomID;
	}
	
	public String getChatroomName(){
		return chatroomName;
	}
}
