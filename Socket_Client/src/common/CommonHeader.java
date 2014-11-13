package common;

public class CommonHeader {
	
	private short version;
	private short flag;           
    private short messageType;  
    private short streamId;

	public CommonHeader( short aVersion, short aFlag, short aMessageType, short aStreamId ) {
		version = aVersion;
		flag = aFlag;
		messageType = aMessageType;
		streamId = aStreamId;
	}
	
	public short getVersion() {
		return version;
	}

	public short getFlag() {
		return flag;
	}

	public short getMessageType() {
		return messageType;
	}

	public short getStreamId() {
		return streamId;
	}
}