package br.com.softctrl.contact.lib.model;

/**
 * Created by timoshenko on 2/20/16.
 */
public interface IPhoneNumber {

    Long getId();
    String getNumber();
    boolean contains(final String number);


}
