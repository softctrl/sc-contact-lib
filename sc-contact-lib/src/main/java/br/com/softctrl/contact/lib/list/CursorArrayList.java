package br.com.softctrl.contact.lib.list;

import android.database.Cursor;
import android.util.Log;
import android.util.StringBuilderPrinter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import br.com.softctrl.contact.lib.CMRuntimeException;
import br.com.softctrl.contact.lib.model.IContact;

import static br.com.softctrl.contact.lib.model.ContactFactory.newContact;
import static br.com.softctrl.contact.lib.util.Constants.Contact.CONTACTS_DISPLAY_NAME;
import static br.com.softctrl.contact.lib.util.Constants.Contact.CONTACTS_ID;
import static br.com.softctrl.contact.lib.util.Constants.Phone.PHONE_NUMBER;
import static br.com.softctrl.contact.lib.util.CursorUtils.getLong;
import static br.com.softctrl.contact.lib.util.CursorUtils.getString;

;

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
public abstract class CursorArrayList<E> implements List<E>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5008231043926594115L;
    private static final String TAG = CursorArrayList.class.getSimpleName();

    private Cursor mCursor = null;
    private int mSize = -1;
    private int mFromIndex = -1;
    private int mToIndex = -1;

    /**
     * @param cursor
     */
    public CursorArrayList(final Cursor cursor) {
        this(cursor, 0, cursor.getCount() - 1);
    }

    /**
     * @param cursor
     * @param fromIndex
     * @param toIndex
     */
    public CursorArrayList(final Cursor cursor, int fromIndex, int toIndex) {
        if (cursor == null || cursor.getCount() == 0)
            throw new CMRuntimeException("You need to inform a valid cursor.");
        this.mFromIndex = fromIndex;
        this.mCursor = cursor;
        this.mSize = this.mCursor.getCount();
        this.mToIndex = toIndex + 1;
    }

    /**
     *
     * @return
     */
    protected int getFromIndex() {
        return mFromIndex;
    }

    /**
     *
     * @return
     */
    protected int getToIndex() {
        return mToIndex;
    }

    /**
     *
     * @return
     */
    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int size() {
        return this.mSize;
    }

    @Override
    public boolean isEmpty() {
        return !(this.size() > 0);
    }

    @Override
    public boolean contains(Object contact) {
        boolean result = false;
//        if (contact instanceof IContact && this.mCursor.moveToFirst()) {
//            while (this.mCursor.moveToNext()) {
//
//            }
//        }
        return result;
    }

    /**
     * This is a auxiliar method to create a list using the instance cursor.
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public abstract List<E> toList(int fromIndex, int toIndex);

    @Override
    public Iterator<E> iterator() {
        Iterator<E> result = toList(this.mFromIndex, this.mToIndex).iterator();
        return result;
    }

    @Override
    public Object[] toArray() {
        Object[] result = toList(this.mFromIndex, this.mToIndex).toArray();
        return result;
    }

    @Override
    public <E> E[] toArray(E[] array) {
        E[] result = toList(this.mFromIndex, this.mToIndex).toArray(array);
        return result;
    }

    @Override
    public boolean add(E entity) {
        CMRuntimeException.throwNew("Sorry, but you can't do this.[boolean add]");
        return false;
    }

    @Override
    public boolean remove(Object entity) {
        CMRuntimeException.throwNew("Sorry, but you can't do this.[boolean remove]");
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> entities) {
        CMRuntimeException.throwNew("Sorry, Not implemented yet.[boolean containsAll]");
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> entities) {
        CMRuntimeException.throwNew("Sorry, but you can't do this.[boolean addAll]");
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> entities) {
        CMRuntimeException.throwNew("Sorry, but you can't do this.[boolean addAll]");
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> entities) {
        CMRuntimeException.throwNew("Sorry, but you can't do this.[boolean removeAll]");
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> entities) {
        CMRuntimeException.throwNew("Sorry, but you can't do this.[boolean retainAll]");
        return false;
    }

    @Override
    public void clear() {
        CMRuntimeException.throwNew("Sorry, but you can't do this.[void clear]");
    }

    /**
     * This method need to be implemented, to perform a correctly parse from cursor to object.
     * @return
     */
    protected abstract E parseObject();// {

    @Override
    public E get(int index) {
        E entity = null;
        if (this.mCursor.moveToPosition(index)) {
            entity = parseObject();
        }
        return entity;
    }

    @Override
    public E set(int index, E entity) {
        CMRuntimeException.throwNew("Sorry, but you can't do this.[IContact set]");
        return null;
    }

    @Override
    public void add(int index, E entity) {
        CMRuntimeException.throwNew("Sorry, but you can't do this.[void add]");
    }

    @Override
    public E remove(int index) {
        CMRuntimeException.throwNew("Sorry, but you can't do this.[IContact remove]");
        return null;
    }

    @Override
    public int indexOf(Object entity) {
        // TODO
        return 0;
    }

    @Override
    public int lastIndexOf(Object entity) {
        // TODO
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        // TODO
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        // TODO
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // TODO
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.mCursor != null && !this.mCursor.isClosed()) {
            this.mCursor.close();
            Log.d(TAG, "CursorArrayList.cursor.close()");
        }
        super.finalize();
    }

}
