package br.com.softctrl.contact.lib.list;

import static br.com.softctrl.contact.lib.model.ContactFactory.newContact;
import static br.com.softctrl.contact.lib.util.Constants.Contact.CONTACTS_DISPLAY_NAME;
import static br.com.softctrl.contact.lib.util.Constants.Contact.CONTACTS_ID;
import static br.com.softctrl.contact.lib.util.Constants.Phone.PHONE_NUMBER;
import static br.com.softctrl.contact.lib.util.CursorUtils.getLong;
import static br.com.softctrl.contact.lib.util.CursorUtils.getString;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.database.Cursor;
import br.com.softctrl.contact.lib.model.IContact;;

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
 * Need much more improvements before using.
 * 
 * @author carlostimoshenkorodrigueslopes@gmail.com
 */
@Deprecated
public final class ArrayListCursor implements List<IContact>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5008231043926594115L;

    private Cursor mCursor = null;

    // private List<IContact> mNewContacts = new ArrayList<IContact>();
    // private List<Long> mIds = new ArrayList<Long>();

    public ArrayListCursor(final Cursor cursor) {
        if (cursor == null)
            throw new RuntimeException("You need to inform a valid cursor.");
        this.mCursor = cursor;
    }

    // public void save() {
    // if (mNewContacts != null && mNewContacts.size() > 0) {
    //
    // }
    // if (mIds != null && mIds.size() > 0) {
    //
    // }
    // }

    @Override
    public int size() {
        return this.mCursor.getCount();
    }

    @Override
    public boolean isEmpty() {
        return !(this.size() > 0);
    }

    @Override
    public boolean contains(Object contact) {
        boolean result = false;
        if (contact instanceof IContact && this.mCursor.moveToFirst()) {
            while (this.mCursor.moveToNext()) {
                
            }
        }
        return result;
    }

    @Override
    public Iterator<IContact> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return null;
    }

    @Override
    public boolean add(IContact contact) {
        return false;
    }

    @Override
    public boolean remove(Object contact) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> contacts) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends IContact> contacts) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends IContact> contacts) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> contacts) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> contacts) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public IContact get(int index) {
        IContact contact = null;
        if (this.mCursor.moveToPosition(index)) {
            contact = newContact(getLong(this.mCursor, CONTACTS_ID), getString(this.mCursor, CONTACTS_DISPLAY_NAME),
                    getString(this.mCursor, PHONE_NUMBER));
        }
        return contact;
    }

    @Override
    public IContact set(int index, IContact contact) {
        return null;
    }

    @Override
    public void add(int index, IContact contact) {
    }

    @Override
    public IContact remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object contact) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object contact) {
        return 0;
    }

    @Override
    public ListIterator<IContact> listIterator() {
        return null;
    }

    @Override
    public ListIterator<IContact> listIterator(int index) {
        return null;
    }

    @Override
    public List<IContact> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.mCursor != null && !this.mCursor.isClosed())
            this.mCursor.close();
        super.finalize();
    }

}
