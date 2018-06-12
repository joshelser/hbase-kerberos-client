Create a Kerberos principal:

```
$ sudo kadmin.local
kadmin.local: addprinc myself
WARNING: no policy specified for myself@EXAMPLE.COM; defaulting to no policy
Enter password for principal "myself@EXAMPLE.COM":
Re-enter password for principal "myself@EXAMPLE.COM":
Principal "myself@EXAMPLE.COM" created.
kadmin.local:  xst -k /etc/security/keytabs/myself.keytab -norandkey myself
Entry for principal myself with kvno 1, encryption type aes256-cts-hmac-sha1-96 added to keytab
WRFILE:/etc/security/keytabs/myself.keytab.
Entry for principal myself with kvno 1, encryption type aes128-cts-hmac-sha1-96 added to keytab
WRFILE:/etc/security/keytabs/myself.keytab.
```
