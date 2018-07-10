Malt
----

Malt is a Java library that handles serial communication, it wraps
[jSerialComm](https://github.com/Fazecast/jSerialComm).
It can be used to send and receive messages between Java and Arduino (or other microcontrollers).

## Maven
```
<dependency>
    <groupId>com.harium.malt</groupId>
    <artifactId>malt</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Generating a local jar
```
mvn package -Dmaven.test.skip=true
```

## Examples

See: [malt-examples](https://github.com/Harium/malt-examples)

### Troubleshout

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
