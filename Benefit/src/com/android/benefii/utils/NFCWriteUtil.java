package com.android.benefii.utils;

import java.nio.charset.Charset;
import java.util.Locale;

import android.nfc.NdefRecord;

/**
 * Created by ES on 2015-05-16.
 */

public class NFCWriteUtil {

	// 텍스트 형식의 레코드를 생성
	public static NdefRecord createTextRecord(String text, Locale locale) {
		// 텍스트 데이터를 인코딩해서 byte 배열로 변환
		byte[] data = byteEncoding(text, locale);
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
				new byte[0], data);
	}

	// 텍스트 데이터를 인코딩해서 byte 배열로 변환
	public static byte[] byteEncoding(String text, Locale locale) {
		// 언어 지정 코드 생성
		byte[] langBytes = locale.getLanguage().getBytes(
				Charset.forName("US-ASCII"));
		// 인코딩 형식 생성
		Charset utfEncoding = Charset.forName("UTF-8");
		// 텍스트를 byte 배열로 변환
		byte[] textBytes = text.getBytes(utfEncoding);

		// 전송할 버퍼 생성
		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) langBytes.length;
		// 버퍼에 언어 코드 저장
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		// 버퍼에 데이터 저장
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
				textBytes.length);
		return data;
	}

	// URI 형식의 레코드를 생성
	public static NdefRecord createUriRecord(String url) {
		// URI 경로를 byte 배열로 변환할 때 US-ACSII 형식으로 지정
		byte[] uriField = url.getBytes(Charset.forName("US-ASCII"));
		// URL 경로를 의미하는 1 을 첫번째 byte 에 추가
		byte[] payload = new byte[uriField.length + 1];
		payload[0] = 0x01;
		System.arraycopy(uriField, 0, payload, 1, uriField.length);
		// NDEF 레코드를 생성해서 반환
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI,
				new byte[0], payload);
	}

	public static boolean supportedTechs(String[] techs) {
		boolean ultralight = false;
		boolean nfcA = false;
		boolean ndef = false;
		for (String tech : techs) {
			if (tech.equals("android.nfc.tech.MifareUltralight")) {
				ultralight = true;
			} else if (tech.equals("android.nfc.tech.NfcA")) {
				nfcA = true;
			} else if (tech.equals("android.nfc.tech.Ndef")
					|| tech.equals("android.nfc.tech.NdefFormatable")) {
				ndef = true;
			}
		}
		if (ultralight && nfcA && ndef) {
			return true;
		} else {
			return false;
		}
	}

	

}
