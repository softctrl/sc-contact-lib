/**
 * 
 */
package br.com.softctrl.contact.lib.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

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
public final class CursorUtils {

    private static final String TAG = CursorUtils.class.getSimpleName();

    /**
     * Private constructor.
     */
    private CursorUtils() {}

    /**
     * Get a single cursor from a given Uri.
     * 
     * @param contentResolver
     * @param uri
     * @return
     */
    public static final synchronized Cursor getCursorFromUri(final ContentResolver contentResolver, final Uri uri) {
        return getCursorFromUri(contentResolver, uri, null, null, null, null);
    }

    /**
     * Get a single cursor from a given Uri.
     * 
     * @param contentResolver
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    public static final synchronized Cursor getCursorFromUri(final ContentResolver contentResolver, final Uri uri,
            final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        Cursor cursor;
        try {
            cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new RuntimeException(e);
        }
        return cursor;
    }

    /**
     * Get a string value from given cursor with informed column name.
     * 
     * @param cursor
     * @param column
     * @return
     */
    public static final synchronized String getString(final Cursor cursor, final String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    /**
     * Get a string value from given cursor with informed column name.
     * 
     * @param cursor
     * @param column
     * @return
     */
    public static final synchronized byte[] getBlob(final Cursor cursor, final String column) {
        return cursor.getBlob(cursor.getColumnIndex(column));
    }

    /**
     * Get a string value from given cursor with informed column name.
     * 
     * @param cursor
     * @param column
     * @return
     */
    public static final synchronized double getDouble(final Cursor cursor, final String column) {
        return cursor.getDouble(cursor.getColumnIndex(column));
    }

    /**
     * Get a float value from given cursor with informed column name.
     * 
     * @param cursor
     * @param column
     * @return
     */
    public static final synchronized float getFloat(final Cursor cursor, final String column) {
        return cursor.getFloat(cursor.getColumnIndex(column));
    }

    /**
     * Get a integer value from given cursor with informed column name.
     * 
     * @param cursor
     * @param column
     * @return
     */
    public static final synchronized int getInt(final Cursor cursor, final String column) {
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    /**
     * Get a long value from given cursor with informed column name.
     * 
     * @param cursor
     * @param column
     * @return
     */
    public static final synchronized long getLong(final Cursor cursor, final String column) {
        return cursor.getLong(cursor.getColumnIndex(column));
    }

    /**
     * Get a short value from given cursor with informed column name.
     * 
     * @param cursor
     * @param column
     * @return
     */
    public static final synchronized short getShort(final Cursor cursor, final String column) {
        return cursor.getShort(cursor.getColumnIndex(column));
    }

}
