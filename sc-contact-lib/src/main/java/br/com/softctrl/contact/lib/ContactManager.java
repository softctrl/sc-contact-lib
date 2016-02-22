package br.com.softctrl.contact.lib;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.softctrl.contact.lib.list.CursorArrayList;
import br.com.softctrl.contact.lib.model.IContact;
import br.com.softctrl.contact.lib.model.IPhoneNumber;

import static br.com.softctrl.contact.lib.model.ContactFactory.newContact;
import static br.com.softctrl.contact.lib.model.ContactFactory.newPhoneNumber;
import static br.com.softctrl.contact.lib.util.Constants.Contact.CONTACTS_DISPLAY_NAME;
import static br.com.softctrl.contact.lib.util.Constants.Contact.CONTACTS_HAS_PHONE_NUMBER;
import static br.com.softctrl.contact.lib.util.Constants.Contact.CONTACTS_ID;
import static br.com.softctrl.contact.lib.util.Constants.Phone.PHONE_ID;
import static br.com.softctrl.contact.lib.util.Constants.Phone.PHONE_NUMBER;
import static br.com.softctrl.contact.lib.util.CursorUtils.getCursorFromUri;
import static br.com.softctrl.contact.lib.util.CursorUtils.getInt;
import static br.com.softctrl.contact.lib.util.CursorUtils.getLong;
import static br.com.softctrl.contact.lib.util.CursorUtils.getString;
import static br.com.softctrl.contact.lib.util.ListUtils.isNullOrEmpty;
import static br.com.softctrl.contact.lib.util.PermissionUtil.isPermissonGranted;
import static br.com.softctrl.contact.lib.util.StringUtils.cleanNumber;
import static br.com.softctrl.contact.lib.util.StringUtils.isNullOrEmpty;

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
     * @param context
     * @return
     */
    public static synchronized ContactManager setup(Context context) {
        if (!isPermissonGranted(context, Manifest.permission.READ_CONTACTS) ||
                !isPermissonGranted(context, Manifest.permission.WRITE_CONTACTS)) {
            CMRuntimeException.throwNew("You need to have the permissions \"android.permission.READ_CONTACTS\" and \"android.permission.WRITE_CONTACTS\" into your manifest.");
        }
        return ($THIS = new ContactManager(context));
    }

    /**
     * @return
     */
    public synchronized static final ContactManager getInstance() {
        if ($THIS == null)
            CMRuntimeException.throwNew("You need to call setup() first.");
        return $THIS;
    }

    /**
     * @param contact
     * @return
     */
    private static final synchronized boolean contactIsValid(final IContact contact) {
        return (contact != null && !isNullOrEmpty(contact.getName()) && !isNullOrEmpty(contact.getNumbers()));
    }

    /**
     * @param name
     * @param number
     * @return
     */
    public long persist(final String name, final String number) {
        return persist(newContact(name, number));
    }

    /**
     * @param contact
     * @param listener
     */
    public void persist(final IContact contact, final ISingleListener<Long> listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(persist(contact));
                } catch (Exception e) {
                    listener.onError(e);
                }
            }
        }).start();
    }

    /**
     * @param contact
     * @return
     */
    public synchronized long persist(final IContact contact) {
        long result = -1;
        if (contactIsValid(contact)) {
            // TODO ??????????????====================================
            //            if (find(contact.getNumber()) != null) {
            //                remove(contact.getNumber());
            //            }
            // =======================================================
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            int rawContactInsertIndex = ops.size();

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).build());

            for (final IPhoneNumber phoneNumber : contact.getNumbers()) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(PHONE_NUMBER, phoneNumber.getNumber()).build());
            }

            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .build());
            try {
                ContentProviderResult[] contentProviderResults = this.mContentResolver
                        .applyBatch(ContactsContract.AUTHORITY, ops);
                if (contentProviderResults == null || contentProviderResults.length != ops.size()) {
                    for (ContentProviderResult res : contentProviderResults) {
                        Log.e(TAG, "There is one error here. - " + res);
                    }
                } else {
                    result = Long.parseLong(contentProviderResults[0].uri.getLastPathSegment());
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                CMRuntimeException.throwNew(TAG, e);
            }
        }
        return result;
    }

    /**
     * @param number
     */
    public IContact find(final String number) {

        Cursor cur = getCursorFromUri(this.mContentResolver,
                Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number)));
        IContact result = null;
        try {
            if (cur.moveToFirst()) {
                String lookupKey = getString(cur, ContactsContract.Contacts.LOOKUP_KEY);
                Cursor contactCursor = getCursorFromUri(this.mContentResolver,
                        Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey));
                if (contactCursor.moveToFirst()) {
                    Long contactId = getLong(contactCursor, CONTACTS_ID);
                    String name = getString(contactCursor, CONTACTS_DISPLAY_NAME);
                    List<IPhoneNumber> numbers = findPhoneNumber(contactId);
                    result = newContact(contactId, name, numbers);
                }
                contactCursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            CMRuntimeException.throwNew(TAG, e);
        }
        cur.close();
        return result;

    }

    /**
     * @param contactId
     * @return
     */
    public IContact find(final Long contactId) {

        String[] contactFilter = new String[]{contactId + ""};
        Cursor contactCursor = getCursorFromUri(this.mContentResolver,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", contactFilter, null);
        IContact result = null;
        try {
            if (contactCursor.getCount() == 1 && contactCursor.moveToFirst()) {
                String name = getString(contactCursor, CONTACTS_DISPLAY_NAME);
                List<IPhoneNumber> numbers = new ArrayList<IPhoneNumber>();
                if (getInt(contactCursor, CONTACTS_HAS_PHONE_NUMBER) > 0) {
                    Cursor numberCursor = getCursorFromUri(this.mContentResolver,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", contactFilter, null);
                    while (numberCursor.moveToNext()) {
                        numbers.add(newPhoneNumber(getLong(numberCursor, PHONE_ID),
                                cleanNumber(getString(numberCursor, PHONE_NUMBER))));
                    }
                    numberCursor.close();
                }
                result = newContact(contactId, name, numbers);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            CMRuntimeException.throwNew(TAG, e);
        }
        contactCursor.close();
        return result;

    }

    /**
     * @param listener
     */
    public void findAll(final IMultiListener<IContact> listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(findAll());
                } catch (Exception e) {
                    listener.onError(e);
                }
            }
        }).start();
    }

    /**
     * @param contactId
     * @return
     */
    public CursorArrayList<IPhoneNumber> findPhoneNumber(long contactId) {
        Cursor numberCursor = getCursorFromUri(this.mContentResolver,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?", new String[]{contactId + ""}, null);
        CursorArrayList<IPhoneNumber> list = null;
        if (numberCursor.getCount() > 0) {
            list = new CursorArrayList<IPhoneNumber>(numberCursor) {
                @Override
                public List<IPhoneNumber> toList(int fromIndex, int toIndex) {
                    List<IPhoneNumber> list = new ArrayList<IPhoneNumber>();
                    for (int idx = getFromIndex(); idx < getToIndex(); idx++) {
                        list.add(get(idx));
                    }
                    return list;
                }

                @Override
                protected IPhoneNumber parseObject() {
                    return newPhoneNumber(getLong(getCursor(), PHONE_ID), getString(getCursor(), PHONE_NUMBER));
                }
            };
        }
        return list;
    }


    /**
     * @return
     */
    public CursorArrayList<IContact> findAll() {

        Cursor contactCursor = getCursorFromUri(this.mContentResolver, ContactsContract.Contacts.CONTENT_URI);
        CursorArrayList<IContact> result = null;
        try {
            if (contactCursor.getCount() > 0) {
                result = new CursorArrayList<IContact>(contactCursor) {
                    @Override
                    public List<IContact> toList(int fromIndex, int toIndex) {
                        List<IContact> list = new ArrayList<IContact>();
                        for (int idx = getFromIndex(); idx < getToIndex(); idx++) {
                            list.add(get(idx));
                        }
                        return list;
                    }

                    @Override
                    protected IContact parseObject() {

                        return new IContact() {
                            private static final long serialVersionUID = 3370304794264376963L;
                            private final List<IPhoneNumber> mNumbers = (getId() == null ?
                                    new ArrayList<IPhoneNumber>() : findPhoneNumber(getId()));
                            private int sizeNumbers = getNumbers().size();
                            @Override
                            public Long getId() {
                                return getLong(getCursor(), CONTACTS_ID);
                            }
                            @Override
                            public String getName() {
                                return getString(getCursor(), CONTACTS_DISPLAY_NAME);
                            }
                            @Override
                            public IPhoneNumber getMainNumber() {
                                return (sizeNumbers == 0 ? null : getNumbers().get(0));
                            }
                            @Override
                            public List<IPhoneNumber> getNumbers() {
                                return this.mNumbers;
                            }
                            @Override
                            public int hashCode() {
                                int hashCode = -1;
                                if (getNumbers() != null) {
                                    hashCode = getNumbers().hashCode() * 73;
                                }
                                return hashCode;
                            }
                            @Override
                            public String toString() {
                                String numbers = Arrays.toString(this.mNumbers.toArray());
                                return String.format("Contact()[id:%s, name:%s, number:%s]", (getId() + ""), getName(),
                                        numbers);
                            }
                            @Override
                            public boolean equals(Object obj) {
                                return ((obj != null) && (obj instanceof IContact) && (this.hashCode() == obj.hashCode()));
                            }
                        };
                    }
                };
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            CMRuntimeException.throwNew(TAG, e);
        }
        return result;

    }

    /**
     * @param number
     * @param listener
     */
    public void remove(final String number, final ISingleListener<Integer> listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(remove(number));
                } catch (Exception e) {
                    listener.onError(e);
                }
            }
        }).start();
    }

    /**
     * @param number
     * @return
     */
    public synchronized int remove(final String number) {
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
        return result;    }

    /**
     * @param id
     * @param listener
     * @return
     */
    public void remove(final Long id, final ISingleListener<Integer> listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.onSuccess(remove(id));
                } catch (Exception e) {
                    listener.onError(e);
                }
            }
        }).start();
    }

    /**
     * @param id
     * @return
     */
    public int remove(final Long id) {
        int result = -1;
        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, Long.toString(id));
            result = this.mContentResolver.delete(uri, null, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            CMRuntimeException.throwNew(TAG, e);
        }
        return result;
    }

}
