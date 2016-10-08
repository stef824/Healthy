package com.satan.healthy.utils;

import android.provider.BaseColumns;

public final class UserContract {
	public abstract class ContactsTableAppearance implements BaseColumns {
		public static final String TABLE_NAME = "contacts";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_PHONE = "phone";
		public static final String COLUMN_NAME_COMPANY = "company";
		public static final String COLUMN_NAME_EMAIL = "email";
	}
}
