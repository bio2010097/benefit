package com.android.benefit.nfcwrite;

import java.io.IOException;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.widget.Toast;


public class WriteResponse {
	private int status;
	private String message;

    private static boolean writeProtect = false;
    
    
	WriteResponse(int Status, String Message) {
		this.status = Status;
		this.message = Message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public static WriteResponse writeTag(NdefMessage message, Tag tag) {
		int size = message.toByteArray().length;
		String mess = "";
		try {
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();
				if (!ndef.isWritable()) {
					return new WriteResponse(0, "Tag is read-only");
				}
				if (ndef.getMaxSize() < size) {
					mess = "Tag capacity is " + ndef.getMaxSize()
							+ " bytes, message is " + size + " bytes.";
					return new WriteResponse(0, mess);
				}
				ndef.writeNdefMessage(message);
				if (writeProtect)
					ndef.makeReadOnly();
				mess = "Wrote message to pre-formatted tag.";
				return new WriteResponse(1, mess);
			} else {
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);
						mess = "Formatted tag and wrote message";
						return new WriteResponse(1, mess);
					} catch (IOException e) {
						mess = "Failed to format tag.";
						return new WriteResponse(0, mess);
					}
				} else {
					mess = "Tag doesn't support NDEF.";
					return new WriteResponse(0, mess);
				}
			}
		} catch (Exception e) {
			mess = "Failed to write tag";
			return new WriteResponse(0, mess);
		}
	}
	
	  public static boolean writableTag(Context context, Tag tag) {
			try {
				Ndef ndef = Ndef.get(tag);
				if (ndef != null) {
					ndef.connect();
					if (!ndef.isWritable()) {
						Toast.makeText(context, "Tag is read-only.",
								Toast.LENGTH_SHORT).show();
						ndef.close();
						return false;
					}
					ndef.close();
					return true;
				}
			} catch (Exception e) {
				Toast.makeText(context, "Failed to read tag",
						Toast.LENGTH_SHORT).show();
			}
			return false;
		}

	
}