/**
 * 
 */
package br.com.softctrl.contact.lib.util;

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
public final class Constants {

    private Constants() {}
    /**
     * All constants to work with Contact provider.
     * @author carlostimoshenkorodrigueslopes@gmail.com
     *
     */
    public static final class Contact {
        private Contact() {}
        public static final String CONTACTS_ID = android.provider.ContactsContract.Contacts._ID;
        public static final String CONTACTS_DISPLAY_NAME = android.provider.ContactsContract.Contacts.DISPLAY_NAME;
        public static final String CONTACTS_HAS_PHONE_NUMBER = android.provider.ContactsContract.Contacts.HAS_PHONE_NUMBER;
    }

    /**
     * All constants to work with Phone provider.
     * @author carlostimoshenkorodrigueslopes@gmail.com
     *
     */
    public static final class Phone {
        private Phone() {}
        public static final String PHONE_ID = android.provider.ContactsContract.CommonDataKinds.Phone._ID;
        public static final String PHONE_NUMBER = android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER;
    }

}
