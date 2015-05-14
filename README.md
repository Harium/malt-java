Java Serial
-----------

#### How to start

In Eclipse IDE
- Add the RXTXcomm.jar in libs folder
- Point the native directory (of RXTXcomm.jar) to `~/git/{project}/libs/natives/{your_os}/{your_os_arch}/`

#### Common issues

##### Avoid /var/lock permission issues

- Install uucp library:

In Fedora:
```
sudo yum install -y uucp
```

In Ubuntu:
```
sudo apt-get install -y uucp
```

- Add permission to edit /var/lock

```
sudo chown uucp /var/lock
sudo usermod -aG uucp $USER
sudo nano /etc/group (change uucp:x:#:{username}  to uucp:x:#:uucp,{username})
sudo chgrp uucp /var/lock
sudo chmod 775 /var/lock
```

- Don't forget to logout!

#### References

- [http://playground.arduino.cc/Interfacing/Java](http://playground.arduino.cc/Interfacing/Java)

- [http://ubuntuforums.org/showthread.php?t=547410](http://ubuntuforums.org/showthread.php?t=547410)
