package br.com.softctrl.contact.lib;

import static br.com.softctrl.contact.lib.model.ContactFactory.newContact;
import static br.com.softctrl.contact.lib.util.CursorUtils.getCursorFromUri;
import static br.com.softctrl.contact.lib.util.CursorUtils.getInt;
import static br.com.softctrl.contact.lib.util.CursorUtils.getString;
import static br.com.softctrl.contact.lib.util.StringUtils.cleanNumber;
import static br.com.softctrl.contact.lib.util.StringUtils.isNullOrEmpty;

import static br.com.softctrl.contact.lib.util.Constants.Contact.CONTACTS_DISPLAY_NAME;
import static br.com.softctrl.contact.lib.util.Constants.Contact.CONTACTS_ID;
import static br.com.softctrl.contact.lib.util.Constants.Contact.CONTACTS_HAS_PHONE_NUMBER;
import static br.com.softctrl.contact.lib.util.Constants.Phone.PHONE_NUMBER;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import br.com.softctrl.contact.lib.model.IContact;

/*
The MIT License (MIT)

Copyright (c) 2015 Carlos Timoshenko Rodrigues Lopes
http://www.0x09.com.br

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

/**
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
public class ContactManager implements Serializable {

    private static final String TAG = ContactManager.class.getSimpleName();
    private static final long serialVersionUID = -4123543101336793057L;

    private static ContactManager $THIS = null;
    private final ContentResolver mContentResolver;

    private ContactManager(Context context) {
        this.mContentResolver = context.getContentResolver();
    }

    /**
     * 
     * @param context
     * @return
     */
    public static synchronized ContactManager setup(Context context) {
        return ($THIS = new ContactManager(context));
    }

    /**
     * 
     * @return
     */
    public synchronized static final ContactManager getInstance() {
        if ($THIS == null)
            throw new RuntimeException("You need to call setup() first.");
        return $THIS;
    }

    /**
     * @param contact
     * @return
     */
    private static final synchronized boolean contactIsValid(final IContact contact) {
        return (contact != null && !isNullOrEmpty(contact.getName()) && !isNullOrEmpty(contact.getNumber()));
    }

    /**
     *
     * @param name
     * @param number
     */
    public void persist(final String name, final String number) {
        persist(newContact(name, number));
    }

    /**
     * @param contact
     */
    public void persist(IContact contact) {
        if (contactIsValid(contact)) {
            if (find(contact.getNumber()) != null) {
                remove(contact.getNumber());
            }
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            int rawContactInsertIndex = ops.size();

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).build());
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(PHONE_NUMBER, contact.getNumber()).build());
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .build());
            try {
                ContentProviderResult[] contentProviderResults = this.mContentResolver
                        .applyBatch(ContactsContract.AUTHORITY, ops);
                if (contentProviderResults != null && contentProviderResults.length > 0) {
                    for (ContentProviderResult res : contentProviderResults) {
                        if (res.uri == null) {
                            Log.e(TAG, "There is one error here. - " + res);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param number
     */
    public IContact find(final String number) {

        Cursor cur = getCursorFromUri(this.mContentResolver, ContactsContract.Contacts.CONTENT_URI);
        IContact result = null;
        try {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    final String id = getString(cur, CONTACTS_ID);
                    final String name = getString(cur, CONTACTS_DISPLAY_NAME);
                    if (getInt(cur, CONTACTS_HAS_PHONE_NUMBER) > 0) {
                        Cursor numbers = getCursorFromUri(this.mContentResolver,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                        while (numbers.moveToNext()) {
                            final String foundedNumber = cleanNumber(getString(numbers, PHONE_NUMBER));
                            if (foundedNumber.contains(number)) {
                                numbers.close();
                                result = newContact(Long.valueOf(id), name, foundedNumber);
                                break;
                            }

                        }
                        numbers.close();
                    }

                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }
        return result;

    }

    /**
     * @param number
     * @return
     */
    public int remove(final String number) {
        int result = -1;
        Cursor cur = getCursorFromUri(this.mContentResolver,
                Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)));
        try {
            if (cur.moveToFirst()) {
                do {
                    String lookupKey = getString(cur, ContactsContract.Contacts.LOOKUP_KEY);
                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                    result = this.mContentResolver.delete(uri, null, null);
                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
