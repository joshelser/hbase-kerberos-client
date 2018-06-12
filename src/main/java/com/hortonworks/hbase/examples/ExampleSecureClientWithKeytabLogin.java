package com.hortonworks.hbase.examples;

import java.io.File;
import java.security.PrivilegedAction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.security.UserGroupInformation;

public class ExampleSecureClientWithKeytabLogin {

  public static void main(String[] args) throws Exception {
    final Configuration conf = HBaseConfiguration.create();

    final String principal = "FILL_ME_IN@REALM.COM";
    final File keytab = new File("/etc/security/keytabs/principal.keytab");

    assert keytab.isFile() : "Provided keytab '" + keytab + "' is not a regular file.";

    UserGroupInformation.setConfiguration(conf);
    UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(
        principal, keytab.getAbsolutePath());

    ugi.doAs(new PrivilegedAction<Void>() {
      @Override public Void run() {
        new ExampleSecureClient(conf).run();
        return null;
      }
    });
  }
}
