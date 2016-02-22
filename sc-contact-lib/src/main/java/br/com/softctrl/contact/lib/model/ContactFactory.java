package br.com.softctrl.contact.lib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    // private static final String[] EMPTY_STRING_ARRAY = new String[] {};
    private static final List<String> EMPTY_STRING_LIST = new ArrayList<String>();
    private static final List<IPhoneNumber> EMPTY_PHONE_LIST = new ArrayList<IPhoneNumber>();

    /**
     * Private constructor.
     */
    private ContactFactory() {
    }

//    /**
//     * @param numbers
//     * @return
//     */
//    private static synchronized List<String> get(final List<String> numbers) {
//        return (numbers == null ? EMPTY_STRING_LIST : numbers);
//    }

    /**
     * @param numbers
     * @return
     */
    private static synchronized List<IPhoneNumber> get(final List<IPhoneNumber> numbers) {
        return (numbers == null ? EMPTY_PHONE_LIST : numbers);
    }

    /**
     * Create a new IContact instance.
     *
     * @param name   the contact name.
     * @param number the contact phone number.
     * @return the instance of IContact.
     */
    public static final synchronized IContact newContact(final String name, final String number) {
        return newContact(null, name, new String[]{number});
    }

    /**
     * @param name
     * @param numbers
     * @return
     */
    public static final synchronized IContact newContact(final String name, final String[] numbers) {
        return newContact(null, name, asList(numbers));
    }

    /**
     * @param name
     * @param numbers
     * @return
     */
    public static final synchronized IContact newContact(final String name, final List<String> numbers) {
        return newContact(null, name, asList(numbers));
    }

    /**
     * @param id
     * @param name
     * @param number
     * @return
     */
    public static final synchronized IContact newContact(final Long id, final String name, final String number) {
        return newContact(id, name, new String[]{number});
    }

    /**
     * @param id
     * @param name
     * @param numbers
     * @return
     */
    public static final synchronized IContact newContact(final Long id, final String name, final String[] numbers) {
        return newContact(id, name, asList(numbers));
    }

    /**
     *
     * @param id
     * @param name
     * @param number
     * @return
     */
    public static final synchronized IContact newContact(final Long id, final String name, final IPhoneNumber number) {
        List<IPhoneNumber> list = new ArrayList<IPhoneNumber>();
        list.add(number);
        return newContact(id, name, list);
    }

    public static final synchronized IContact newContact(final Long id, final IContact contact) {
        return newContact(id, contact.getName(), contact.getNumbers());
    }

    /**
     * Create a new IContact instance.
     *
     * @param id      the contact id.
     * @param name    the contact name.
     * @param numbers the contact phone number.
     * @return the instance of IContact.
     */
    public static final synchronized IContact newContact(final Long id, final String name, final List<IPhoneNumber> numbers) {

        final List<IPhoneNumber> numberss = get(numbers);
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
            public IPhoneNumber getMainNumber() {
                return (getNumbers().size() == 0 ? null : getNumbers().get(0));
            }

            @Override
            public List<IPhoneNumber> getNumbers() {
                return numbers;
            }

            @Override
            public int hashCode() {
                return numberss.hashCode() + 73;
            }

            @Override
            public String toString() {
                return String.format("Contact()[id:%s, name:%s, number:%s]", (id + ""), name,
                        Arrays.toString(numberss.toArray(new String[]{})));
            }

            @Override
            public boolean equals(Object obj) {
                return ((obj != null) && (obj instanceof IContact) && (this.hashCode() == obj.hashCode()));
            }
        };
    }

    /**
     * @param id
     * @param number
     * @return
     */
    public static final synchronized IPhoneNumber newPhoneNumber(final Long id, final String number) {

        return new IPhoneNumber() {
            @Override
            public Long getId() {
                return id;
            }

            @Override
            public String getNumber() {
                return number;
            }

            @Override
            public int hashCode() {
                return (getNumber() + "").hashCode();
            }

            @Override
            public boolean contains(String number) {
                return (getNumber() + "").contains(number);
            }

            @Override
            public boolean equals(Object obj) {
                return ((obj != null) && (obj instanceof IPhoneNumber) && (this.hashCode() == obj.hashCode()));
            }
            @Override
            public String toString() {
                return String.format("PhoneNumber()[id:%s, number:%s]", (id + ""), number);
            }
        };

    }

    /**
     * @param numbers
     * @return
     */
    private static final synchronized List<IPhoneNumber> asList(final String[] numbers) {
        List<IPhoneNumber> list = new ArrayList<IPhoneNumber>();
        for (String number : numbers) {
            list.add(newPhoneNumber(null, number));
        }
        return list;
    }

    /**
     * @param numbers
     * @return
     */
    private static final synchronized List<IPhoneNumber> asList(final List<String> numbers) {
        List<IPhoneNumber> list = new ArrayList<IPhoneNumber>();
        for (String number : numbers) {
            list.add(newPhoneNumber(null, number));
        }
        return list;
    }

}
