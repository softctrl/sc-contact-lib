package br.com.softctrl.contact.lib;

import java.util.List;

public interface IMultiListener<T> {

    void onSuccess(List<T> results);
    void onError(Throwable throwable);
}
