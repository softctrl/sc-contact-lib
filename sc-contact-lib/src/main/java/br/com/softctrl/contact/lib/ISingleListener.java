package br.com.softctrl.contact.lib;

public interface ISingleListener<T> {

    void onSuccess(T result);
    void onError(Throwable throwable);
}
