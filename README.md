# sc-contact-lib
Just a little project to manage contacts in android devices

To use, just make those imports:

```java
import br.com.softctrl.contact.lib.ContactManager;
```

Then, don't forget to call the "setup(Context)" method first.

```java
// Just a sample, imagine that i am calling this method on an activity onCreate method:
ContactManager.setup(this);
```

And finally, we can call this methods:

```java
// Create Or update:
ContactManager.getInstance().persist("Contact name", "00000000000");
// Find a contact by phone number:
Toast.makeText(MainActivity.this, String.format("Contact:[%s]", "" + ContactManager.getInstance().find("00000000000")), Toast.LENGTH_LONG).show();
// Remove a contact by phone number:
ContactManager.getInstance().remove("00000000000");
```

Thanks.
