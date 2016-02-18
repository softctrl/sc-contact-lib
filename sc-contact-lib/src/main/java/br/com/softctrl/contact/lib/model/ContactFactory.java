package br.com.softctrl.contact.lib.model;

import java.io.Serializable;

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
public final class ContactFactory implements Serializable {

    private static final long serialVersionUID = -7581097009387485752L;

    /**
     * Private constructor.
     */
    private ContactFactory() {}

    /**
     * Create a new IContact instance.
     * @param name the contact name.
     * @param number the contact phone number. 
     * @return the instance of IContact.
     */
    public static final synchronized IContact newContact(final String name, final String number) {
        return newContact(null, name, number);
    }

    /**
     * Create a new IContact instance.
     * @param id the contact id.
     * @param name the contact name.
     * @param number the contact phone number. 
     * @return the instance of IContact.
     */
    public static final synchronized IContact newContact(final Long id, final String name, final String number) {
        return new IContact() {
            private static final long serialVersionUID = 3370304794264376963L;
            @Override
            public Long getId() {
                return id;
            }
            @Override
            public String getName() {
                return name;
            }
            @Override
            public String getNumber() {
                return number;
            }
            @Override
            public int hashCode() {
                return ("" + number).hashCode() + 73;
            }
            @Override
            public String toString() {
                return String.format("Contact()[id:%s, name:%s, number:%s]", (id + ""), name, number);
            }
            @Override
            public boolean equals(Object obj) {
                return ((obj != null) && (obj instanceof IContact) && (this.hashCode() == ((IContact) obj).hashCode()));
            }
        };
    }

}
