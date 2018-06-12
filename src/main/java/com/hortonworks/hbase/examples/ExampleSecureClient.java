package com.hortonworks.hbase.examples;

import java.io.IOException;
import java.util.Objects;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Write and read data from HBase, expects HBASE_CONF_DIR and HADOOP_CONF_DIR on the classpath and a valid Kerberos
 * ticket in a ticket cache (e.g. kinit).
 */
public class ExampleSecureClient implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleSecureClient.class);
  private static final TableName TABLE_NAME = TableName.valueOf("example_secure_client");
  private static final byte[] CF = Bytes.toBytes("f1");

  private final Configuration conf;

  public ExampleSecureClient(Configuration conf) {
    this.conf = Objects.requireNonNull(conf);
  }

  @Override public void run() {
    try (Connection conn = ConnectionFactory.createConnection(conf)) { 
      writeAndRead(conn, TABLE_NAME, CF);
      LOG.info("Success!");
    } catch (Exception e) {
      LOG.error("Uncaught exception running example", e);
      throw new RuntimeException(e);
    }
  }

  void writeAndRead(Connection conn, TableName tn, byte[] family) throws IOException {
    final Admin admin = conn.getAdmin();

    // Delete the table if it already exists
    if (admin.tableExists(tn)) {
      admin.disableTable(tn);
      admin.deleteTable(tn);
    }

    // Create our table
    admin.createTable(TableDescriptorBuilder.newBuilder(tn).setColumnFamily(
        ColumnFamilyDescriptorBuilder.of(family)).build());

    final Table table = conn.getTable(tn);
    Put p = new Put(Bytes.toBytes("row1"));
    p.addColumn(family, Bytes.toBytes("q1"), Bytes.toBytes("value"));
    LOG.info("Writing update: row1 -> value");
    table.put(p);

    Result r = table.get(new Get(Bytes.toBytes("row1")));
    assert r.size() == 1;
    LOG.info("Read row1: {}", r);
  }

  public static void main(String[] args) {
    final Configuration conf = HBaseConfiguration.create();
    new ExampleSecureClient(conf).run();
  }
}
