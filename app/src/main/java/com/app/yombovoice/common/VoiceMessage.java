package com.app.yombovoice.common;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("VoiceMessage")
public class VoiceMessage extends ParseObject {
	public VoiceMessage(){
	}
	public ParseUser getSender(){	return getParseUser("sender");	}
	public void setSender(ParseUser sender){
		put("sender",sender);
	}
	public ParseUser getReceiver(){	return getParseUser("receiver");	}
	public void setReceiver(ParseUser receiver){
		put("receiver",receiver);
	}
	public ParseFile getVoice(){ return getParseFile("voice");}
	public void setVoice(ParseFile voice){ put("voice",voice);}
	public ParseFile getImage(){ return getParseFile("image");}
	public void setImage(ParseFile image){ put("image",image);}
}
